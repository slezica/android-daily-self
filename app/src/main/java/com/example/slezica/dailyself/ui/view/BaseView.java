package com.example.slezica.dailyself.ui.view;

import android.content.Context;
import android.support.annotation.AttrRes;
import android.support.annotation.CallSuper;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.FrameLayout;
import butterknife.ButterKnife;

public abstract class BaseView extends FrameLayout {

    public BaseView(@NonNull Context context) {
        super(context);
        setUp(context, null);
    }

    public BaseView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        setUp(context, attrs);
    }

    public BaseView(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setUp(context, attrs);
    }

    @CallSuper
    protected void setUp(Context context, @Nullable AttributeSet attrs) {
        LayoutInflater.from(context)
                .inflate(getLayoutResource(), this, true);

        ButterKnife.bind(this);
    }

    @LayoutRes
    protected abstract int getLayoutResource();
}
