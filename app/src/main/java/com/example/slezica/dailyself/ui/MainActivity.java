package com.example.slezica.dailyself.ui;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;
import butterknife.BindView;
import com.example.slezica.dailyself.R;
import com.example.slezica.dailyself.model.Pursuit;
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
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        pursuitAdapter = new PursuitAdapter(this);

        pursuitList.setAdapter(pursuitAdapter);
        pursuitList.setLayoutManager(new LinearLayoutManager(this));
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
            holder.getView().setContent(item);
        }

        @Override
        public ViewHolder<PursuitItem> onCreateViewHolder(ViewGroup parent, int viewType) {
            return new ViewHolder<>(new PursuitItem(parent.getContext()));
        }
    }
}
