package com.example.slezica.dailyself.ui.view;


import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.OnClick;
import com.example.slezica.dailyself.R;
import com.example.slezica.dailyself.model.Pursuit;
import com.example.slezica.dailyself.model.PursuitEntry;
import com.example.slezica.dailyself.utils.Callback1;
import com.example.slezica.dailyself.utils.RichText;

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
            text = TextUtils.concat(
                    new RichText("Latest: ").setBold(),
                    entry.getComment()
            );
        } else {
            text = new RichText("No entries").setItalic();
        }
        System.out.println("jere setting " + text.toString());
        latestEntry.setText(text);
    }

    public void setOnAddEntryClick(Callback1<Pursuit> onAddEntryClick) {
        this.onAddEntryClick = onAddEntryClick;
    }
}
