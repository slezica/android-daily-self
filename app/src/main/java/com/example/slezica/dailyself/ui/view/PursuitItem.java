package com.example.slezica.dailyself.ui.view;


import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.text.format.DateUtils;
import android.util.AttributeSet;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.OnClick;
import com.example.slezica.dailyself.R;
import com.example.slezica.dailyself.model.Pursuit;
import com.example.slezica.dailyself.model.PursuitEntry;
import com.example.slezica.dailyself.utils.Callback1;
import com.example.slezica.dailyself.utils.RichText;
import org.threeten.bp.ZonedDateTime;

public class PursuitItem extends BaseView {

    @BindView(R.id.name)
    TextView name;

    @BindView(R.id.latest_entry)
    TextView latestEntry;

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

    public void setLatestEntry(PursuitEntry entry) {
        final CharSequence text;

        if (entry != null) {
            final String when = formatDatetime(entry.getDatetime());

            text = TextUtils.concat(
                    new RichText(when).setBold(),
                    ": ",
                    entry.getComment()
            );

        } else {
            text = new RichText("No entries").setItalic();
        }

        latestEntry.setText(text);
    }

    public void setOnAddEntryClick(Callback1<Pursuit> onAddEntryClick) {
        this.onAddEntryClick = onAddEntryClick;
    }

    private String formatDatetime(ZonedDateTime datetime) {
        return DateUtils.getRelativeTimeSpanString(datetime.toEpochSecond() * 1000).toString();
    }
}
