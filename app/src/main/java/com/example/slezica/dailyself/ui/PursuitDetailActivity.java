package com.example.slezica.dailyself.ui;


import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import butterknife.BindView;
import com.example.slezica.dailyself.R;
import com.example.slezica.dailyself.model.Pursuit;
import com.example.slezica.dailyself.model.PursuitEntry;
import com.example.slezica.dailyself.ui.view.PursuitEntryItem;
import com.example.slezica.dailyself.utils.ReactiveAdapter;
import com.example.slezica.dailyself.utils.SimpleReactiveAdapter;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import io.reactivex.Observable;
import io.requery.reactivex.ReactiveResult;

import java.util.ArrayList;
import java.util.List;

public class PursuitDetailActivity extends BaseActivity {

    public static final String PURSUIT_ID = "pursuitId";

    @BindView(R.id.chart)
    LineChart chart;

    @BindView(R.id.entry_list)
    RecyclerView entryList;

    private ReactiveAdapter entryAdapter;

    private Pursuit pursuit;

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_pursuit_detail;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final int pursuitId = getIntent().getIntExtra(PURSUIT_ID, 0);
        loadPursuit(pursuitId);

        entryAdapter = new EntryAdapter(this);

        entryList.setAdapter(entryAdapter);
        entryList.setLayoutManager(new LinearLayoutManager(this));
        entryList.addItemDecoration(new DividerItemDecoration(this, LinearLayout.VERTICAL));
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (pursuit != null) {
            entryAdapter.start();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        entryAdapter.stop();
    }

    protected void loadPursuit(int pursuitId) {
        final Observable<Pursuit> selectPursuit = dataStore.select(Pursuit.class)
                .where(Pursuit.ID.eq(pursuitId))
                .get()
                .observable();

        subscribeTo(selectPursuit, this::onPursuitLoaded);

        final Observable<List<PursuitEntry>> selectEntries = dataStore
                .select(PursuitEntry.class)
                .where(PursuitEntry.PURSUIT_ID.eq(pursuitId))
                .orderBy(PursuitEntry.DATETIME.asc())
                .limit(30)
                .get()
                .observableResult()
                .map(ReactiveResult::toList);

        subscribeTo(selectEntries, this::onPursuitEntriesLoaded);
    }

    private void onPursuitLoaded(Pursuit pursuit) {
        this.pursuit = pursuit;

        setTitle(pursuit.getName());
        entryAdapter.start();
    }

    private void onPursuitEntriesLoaded(List<PursuitEntry> entries) {
        if (entries == null || entries.size() < 2) {
            chart.clear();
            return;
        }

        final List<Entry> chartEntries = new ArrayList<Entry>();

        int index = 0;
        for (PursuitEntry entry: entries) {
            chartEntries.add(new Entry(index++, entry.getScore()));
        }

        final LineDataSet dataSet = new LineDataSet(chartEntries, "Scores");
        dataSet.setLineWidth(3f);

        final LineData data = new LineData(dataSet);

        chart.setData(data);
        chart.setDescription(null);
        chart.getAxisRight().setEnabled(false);
        chart.getXAxis().setEnabled(false);
        chart.getLegend().setEnabled(false);

        chart.notifyDataSetChanged();
        chart.invalidate();
    }

    class EntryAdapter extends SimpleReactiveAdapter<PursuitEntry, PursuitEntryItem> {

        public EntryAdapter(Context context) {
            super(context);
        }

        @Override
        public ReactiveResult<PursuitEntry> getItems() {
            return dataStore
                    .select(PursuitEntry.class)
                    .where(PursuitEntry.PURSUIT.eq(pursuit))
                    .orderBy(PursuitEntry.DATETIME.desc())
                    .get();
        }

        @Override
        public PursuitEntryItem onCreateView(ViewGroup parent) {
            return new PursuitEntryItem(parent.getContext());
        }

        @Override
        public void onBindView(PursuitEntryItem itemView, PursuitEntry itemData) {
            itemView.setPursuitEntry(itemData);
        }
    }
}
