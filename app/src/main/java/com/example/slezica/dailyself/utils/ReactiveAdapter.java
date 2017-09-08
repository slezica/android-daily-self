package com.example.slezica.dailyself.utils;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;
import com.example.slezica.dailyself.app.ApplicationClass;
import com.example.slezica.dailyself.ui.view.ViewHolder;
import io.reactivex.Observable;
import io.reactivex.Scheduler;
import io.reactivex.disposables.Disposable;

import java.util.ArrayList;
import java.util.List;

public abstract class ReactiveAdapter<ItemT, ItemDataT, ViewT extends View>
        extends RecyclerView.Adapter<ViewHolder<ViewT>> {

    final Context context;

    private Scheduler backgroundScheduler;
    private Scheduler mainScheduler;

    private Disposable listSub;
    private SparseArray<Disposable> itemSubs;
    
    private List<ItemT> list;

    public ReactiveAdapter(Context context) {
        this.context = context;

        final ApplicationClass app = (ApplicationClass) context.getApplicationContext();
        backgroundScheduler = app.getBackgroundScheduler();
        mainScheduler = app.getMainScheduler();

        itemSubs = new SparseArray<>();
        list = new ArrayList<>();
    }

    public abstract Observable<List<ItemT>> getItems();
    public abstract Observable<ItemDataT> getItemData(ItemT item);

    public abstract ViewT onCreateView(ViewGroup parent);
    public abstract void onBindView(ViewT itemView, ItemDataT itemData);
    public void onUnbindView(ViewT itemView) {}

    @Override
    public int getItemCount() {
        return list.size();
    }

    @Override
    public ViewHolder<ViewT> onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder<>(onCreateView(parent));
    }

    @Override
    public void onBindViewHolder(ViewHolder<ViewT> holder, int position) {
        final ItemT item = list.get(position);

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

    public void start() {
        if (listSub == null) {
            listSub = getItems()
                    .subscribeOn(backgroundScheduler)
                    .observeOn(mainScheduler)
                    .subscribe(this::onListReady);
        }
    }

    public void stop() {
        if (listSub != null) {
            listSub.dispose();
            listSub = null;
        }
    }

    private void onListReady(List<ItemT> newList) {
        list = newList;
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
