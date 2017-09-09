package com.example.slezica.dailyself.utils;


import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;
import com.example.slezica.dailyself.app.ApplicationClass;
import com.example.slezica.dailyself.ui.view.ViewHolder;
import io.reactivex.Observable;
import io.reactivex.Scheduler;
import io.reactivex.disposables.Disposable;
import io.requery.reactivex.ReactiveResult;
import io.requery.sql.ResultSetIterator;

import java.sql.SQLException;

public abstract class ReactiveAdapter<ItemT, ItemDataT, ViewT extends View>
        extends RecyclerView.Adapter<ViewHolder<ViewT>> {

    final Context context;

    private Scheduler backgroundScheduler;
    private Scheduler mainScheduler;

    private Disposable listSub;
    private SparseArray<Disposable> itemSubs;
    
    private ResultSetIterator<ItemT> iterator;

    public ReactiveAdapter(Context context) {
        this.context = context;

        final ApplicationClass app = (ApplicationClass) context.getApplicationContext();
        backgroundScheduler = app.getBackgroundScheduler();
        mainScheduler = app.getMainScheduler();

        itemSubs = new SparseArray<>();
    }

    public abstract ReactiveResult<ItemT> getItems();
    public abstract Observable<ItemDataT> getItemData(ItemT item);

    public abstract ViewT onCreateView(ViewGroup parent);
    public abstract void onBindView(ViewT itemView, ItemDataT itemData);
    public void onUnbindView(ViewT itemView) {}

    @Override
    public int getItemCount() {
        try {
            return (iterator != null) ? iterator.unwrap(Cursor.class).getCount() : 0;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public long getItemId(int position) {
        return iterator.get(position).hashCode();
    }

    @Override
    public ViewHolder<ViewT> onCreateViewHolder(ViewGroup parent, int viewType) {
        final ViewT view = onCreateView(parent);

        if (view.getLayoutParams() == null) {
            view.setLayoutParams(new RecyclerView.LayoutParams(
                    RecyclerView.LayoutParams.MATCH_PARENT,
                    RecyclerView.LayoutParams.WRAP_CONTENT
            ));
        }

        return new ViewHolder<>(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder<ViewT> holder, int position) {
        final ItemT item = iterator.get(position);

        removeItemSub(position);

        final Disposable newSub = getItemData(item)
                .subscribeOn(backgroundScheduler)
                .observeOn(mainScheduler)
                .subscribe(itemData -> onBindView(holder.getView(), itemData));

        addItemSub(position, newSub);
    }

    @Override
    public void onViewRecycled(ViewHolder<ViewT> holder) {
        removeItemSub(holder.getAdapterPosition());
        onUnbindView(holder.getView());
    }

    @Override
    public void onDetachedFromRecyclerView(RecyclerView recyclerView) {
        super.onDetachedFromRecyclerView(recyclerView);
        stop();
    }

    public void start() {
        if (listSub == null) {
            listSub = getItems()
                    .observableResult()
                    .subscribeOn(backgroundScheduler)
                    .observeOn(mainScheduler)
                    .subscribe(this::onResultChanged);
        }
    }

    public void stop() {
        if (listSub != null) {
            listSub.dispose();
            listSub = null;
        }

        if (iterator != null) {
            iterator.close();
            iterator = null;
        }
    }

    private boolean isActive() {
        return (listSub != null);
    }

    private void onResultChanged(ReactiveResult<ItemT> newResult) {
        if (! isActive()) {
            return;
        }

        if (iterator != null) {
            iterator.close();
        }

        iterator = (ResultSetIterator<ItemT>) newResult.iterator();
        notifyDataSetChanged();
    }

    private void addItemSub(int position, Disposable sub) {
        removeItemSub(position);
        itemSubs.setValueAt(position, sub);
    }

    private void removeItemSub(int position) {
        final Disposable sub = itemSubs.get(position);

        if (sub != null) {
            sub.dispose();
        }

        itemSubs.remove(position);
    }
}
