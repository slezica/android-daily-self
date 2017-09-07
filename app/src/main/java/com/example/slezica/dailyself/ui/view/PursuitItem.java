package com.example.slezica.dailyself.ui.view;


import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.text.format.DateUtils;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.OnClick;
import com.example.slezica.dailyself.R;
import com.example.slezica.dailyself.model.Pursuit;
import com.example.slezica.dailyself.model.PursuitEntry;
import com.example.slezica.dailyself.utils.Callback1;
import com.example.slezica.dailyself.utils.RichText;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import org.threeten.bp.ZonedDateTime;

import java.util.ArrayList;
import java.util.List;

public class PursuitItem extends BaseView {

    @BindView(R.id.name)
    TextView name;

    @BindView(R.id.latest_entry)
    TextView latestEntry;

    @BindView(R.id.chart)
    LineChart chart;

    Pursuit pursuit;
    Callback1<Pursuit> onAddEntryClick;

    public PursuitItem(@NonNull Context context) {
        super(context);
    }

    public PursuitItem(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public PursuitItem(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.view_pursuit_item;
    }

    @OnClick(R.id.add_entry)
    public void onAddEntryClick() {
        if (onAddEntryClick != null) onAddEntryClick.call(pursuit);
    }

    public void setPursuit(Pursuit pursuit) {
        this.pursuit = pursuit;
        name.setText(pursuit.getName());
    }

    public void setPursuitEntries(List<PursuitEntry> entries) {
        if (entries.size() == 0) return;
        if (! entries.get(0).getPursuit().equals(pursuit)) return;

        setChartEntries(entries);
        setLatestEntry(entries.get(entries.size() - 1));
    }

    private void setChartEntries(List<PursuitEntry> entries) {
        final List<Entry> chartEntries = new ArrayList<Entry>();

        int index = 0;
        for (PursuitEntry entry: entries) {
            chartEntries.add(new Entry(index++, entry.getScore()));
        }

        final LineDataSet dataSet = new LineDataSet(chartEntries, "Scores");
        dataSet.setLineWidth(3f);

        final LineData data = new LineData(dataSet);
        data.setDrawValues(false);

        chart.setData(data);
        chart.setDescription(null);
        chart.setDrawGridBackground(false);
        chart.getAxisLeft().setEnabled(false);
        chart.getAxisRight().setEnabled(false);
        chart.getXAxis().setEnabled(false);
        chart.getLegend().setEnabled(false);

        chart.setVisibility(View.VISIBLE);
        chart.invalidate();
    }

    private void setLatestEntry(PursuitEntry entry) {
        final CharSequence text;

        if (entry != null) {
            final String when = formatDatetime(entry.getDatetime());

            text = TextUtils.concat(
                    new RichText(when).setBold(),
                    ": " + entry.getScore() + " ",
                    new RichText(entry.getComment()).setItalic()
            );

        } else {
            text = new RichText("No entries").setItalic();
        }

        System.out.println("Setting text" + text.toString());
        latestEntry.setText(text);
    }

    public void setOnAddEntryClick(Callback1<Pursuit> onAddEntryClick) {
        this.onAddEntryClick = onAddEntryClick;
    }

    private String formatDatetime(ZonedDateTime datetime) {
        return DateUtils.getRelativeTimeSpanString(datetime.toEpochSecond() * 1000).toString();
    }
}
