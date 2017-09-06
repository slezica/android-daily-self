package com.example.slezica.dailyself.ui.view;


import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.TextView;
import butterknife.BindView;
import com.example.slezica.dailyself.R;
import com.example.slezica.dailyself.model.Pursuit;

public class PursuitItem extends BaseView {

    @BindView(R.id.name)
    TextView name;

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

    public void setContent(Pursuit content) {
        name.setText(content.getName());
    }
}
