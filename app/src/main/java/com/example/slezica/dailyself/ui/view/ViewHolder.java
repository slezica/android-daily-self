package com.example.slezica.dailyself.ui.view;

import android.support.v7.widget.RecyclerView;
import android.view.View;


public class ViewHolder<T extends View> extends RecyclerView.ViewHolder {

    private T view;

    public ViewHolder(T itemView) {
        super(itemView);
        view = itemView;
    }

    public T getView() {
        return view;
    }
}
