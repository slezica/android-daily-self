package com.example.slezica.dailyself.utils;

import android.content.Context;
import android.view.View;
import io.reactivex.Observable;

public abstract class SimpleReactiveAdapter<ItemT, ViewT extends View>
        extends ReactiveAdapter<ItemT, ItemT, ViewT> {

    public SimpleReactiveAdapter(Context context) {
        super(context);
    }

    @Override
    public Observable<ItemT> getItemData(ItemT item) {
        return Observable.just(item);
    }
}
