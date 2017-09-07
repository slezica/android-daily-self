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
import com.example.slezica.dailyself.ui.view.ReactiveQueryAdapter;
import com.example.slezica.dailyself.ui.view.ViewHolder;
import io.requery.reactivex.ReactiveResult;

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
        pursuitAdapter.pause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        pursuitAdapter.stop();
    }

    protected void onPursuitAddEntryClick(Pursuit pursuit) {
        final Intent intent = new Intent(this, NewPursuitEntryActivity.class)
                .putExtra(NewPursuitEntryActivity.PURSUIT_ID, pursuit.getId());

        startActivity(intent);
    }

    private class PursuitAdapter extends ReactiveQueryAdapter<Pursuit, PursuitItem> {

        PursuitAdapter(Context context) {
            super(context);
        }

        @Override
        public ReactiveResult<Pursuit> performQuery() {
            return dataStore.select(Pursuit.class).get();
        }

        @Override
        public void onBindViewHolder(Pursuit item, ViewHolder<PursuitItem> holder, int position) {
            final PursuitItem itemView = holder.getView();

            itemView.setLayoutParams(new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT
            ));

            itemView.setPursuit(item);
            itemView.setOnAddEntryClick(MainActivity.this::onPursuitAddEntryClick);

            final PursuitEntry latestEntry = dataStore.select(PursuitEntry.class)
                    .where(PursuitEntry.PURSUIT.eq(item))
                    .orderBy(PursuitEntry.DATETIME.desc())
                    .limit(1)
                    .get()
                    .firstOrNull();

            itemView.setLatestEntry(latestEntry);
        }

        @Override
        public ViewHolder<PursuitItem> onCreateViewHolder(ViewGroup parent, int viewType) {
            return new ViewHolder<>(new PursuitItem(parent.getContext()));
        }
    }
}
