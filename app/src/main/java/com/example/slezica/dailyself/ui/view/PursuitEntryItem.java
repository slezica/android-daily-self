package com.example.slezica.dailyself.ui.view;


import android.content.Context;
import android.support.annotation.NonNull;
import android.text.format.DateUtils;
import android.util.AttributeSet;
import android.widget.TextView;
import butterknife.BindView;
import com.example.slezica.dailyself.R;
import com.example.slezica.dailyself.model.PursuitEntry;
import org.threeten.bp.ZonedDateTime;

public class PursuitEntryItem extends BaseView {

    @BindView(R.id.score)
    TextView score;

    @BindView(R.id.datetime)
    TextView datetime;

    @BindView(R.id.comment)
    TextView comment;

    private PursuitEntry entry;

    public PursuitEntryItem(@NonNull Context context) {
        super(context);
    }

    public PursuitEntryItem(@NonNull Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public PursuitEntryItem(@NonNull Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.view_pursuit_entry_item;
    }

    public void setPursuitEntry(PursuitEntry newEntry) {
        entry = newEntry;

        if (entry != null) {
            score.setText("" + entry.getScore());
            datetime.setText(formatDatetime(entry.getDatetime()));
            comment.setText(entry.getComment());
        }
    }

    private String formatDatetime(ZonedDateTime datetime) {
        return DateUtils.getRelativeTimeSpanString(datetime.toEpochSecond() * 1000).toString();
    }
}
