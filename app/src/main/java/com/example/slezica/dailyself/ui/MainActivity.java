package com.example.slezica.dailyself.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import butterknife.BindView;
import com.example.slezica.dailyself.R;
import com.example.slezica.dailyself.model.Pursuit;
import com.example.slezica.dailyself.model.PursuitEntry;
import com.example.slezica.dailyself.ui.view.PursuitItem;
import com.example.slezica.dailyself.utils.ReactiveAdapter;
import io.reactivex.Observable;
import io.requery.reactivex.ReactiveResult;

import java.util.List;

public class MainActivity extends BaseActivity {

    @BindView(R.id.pursuit_list)
    RecyclerView pursuitList;

    PursuitAdapter pursuitAdapter;

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_main;
    }

    @Override
    protected int getMenuResource() {
        return R.menu.main;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        pursuitAdapter = new PursuitAdapter(this);

        pursuitList.setAdapter(pursuitAdapter);
        pursuitList.setLayoutManager(new LinearLayoutManager(this));
        pursuitList.addItemDecoration(new DividerItemDecoration(this, LinearLayout.VERTICAL));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.home_action_add:
                startActivity(new Intent(this, NewPursuitActivity.class));
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        pursuitAdapter.start();
    }

    @Override
    protected void onPause() {
        super.onPause();
        pursuitAdapter.stop();
    }

    protected void onPursuitAddEntryClick(Pursuit pursuit) {
        final Intent intent = new Intent(this, NewPursuitEntryActivity.class)
                .putExtra(NewPursuitEntryActivity.PURSUIT_ID, pursuit.getId());

        startActivity(intent);
    }

    private class PursuitData {
        public PursuitData(Pursuit pursuit, List<PursuitEntry> entries) {
            this.pursuit = pursuit;
            this.entries = entries;
        }

        Pursuit pursuit;
        List<PursuitEntry> entries;
    }

    private class PursuitAdapter extends ReactiveAdapter<Pursuit, PursuitData, PursuitItem> {

        PursuitAdapter(Context context) {
            super(context);
        }

        @Override
        public Observable<List<Pursuit>> getItems() {
            return dataStore.select(Pursuit.class)
                    .get()
                    .observableResult()
                    .map(ReactiveResult::toList);
        }

        @Override
        public Observable<PursuitData> getItemData(Pursuit item) {
            return dataStore
                    .select(PursuitEntry.class)
                    .where(PursuitEntry.PURSUIT.eq(item))
                    .orderBy(PursuitEntry.DATETIME.asc())
                    .limit(30)
                    .get()
                    .observableResult()
                    .map(ReactiveResult::toList)
                    .map(entries -> new PursuitData(item, entries));
        }

        @Override
        public PursuitItem onCreateView(ViewGroup parent) {
            return new PursuitItem(parent.getContext());
        }

        @Override
        public void onBindView(PursuitItem itemView, PursuitData itemData) {
            itemView.setPursuit(itemData.pursuit);
            itemView.setPursuitEntries(itemData.entries);

            itemView.setOnAddEntryClick(MainActivity.this::onPursuitAddEntryClick);
        }

        @Override
        public void onUnbindView(PursuitItem itemView) {
            itemView.setPursuit(null);
            itemView.setPursuitEntries(null);
        }
    }
}
