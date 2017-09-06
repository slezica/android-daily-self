package com.example.slezica.dailyself.ui.view;

import android.content.Context;
import android.view.View;
import com.example.slezica.dailyself.app.ApplicationClass;
import io.reactivex.Scheduler;
import io.reactivex.disposables.Disposable;
import io.requery.android.QueryRecyclerAdapter;
import io.requery.query.Result;
import io.requery.reactivex.ReactiveResult;
import io.requery.sql.ResultSetIterator;


public abstract class ReactiveQueryAdapter<T, V extends View>
        extends QueryRecyclerAdapter<T, ViewHolder<V>> {

    private Scheduler backgroundScheduler;
    private Scheduler mainScheduler;

    private Disposable subscription;

    public ReactiveQueryAdapter(Context context) {
        super();

        final ApplicationClass app = (ApplicationClass) context.getApplicationContext();
        backgroundScheduler = app.getBackgroundScheduler();
        mainScheduler = app.getMainScheduler();
    }

    public void start() {
        if (subscription == null) {
            subscription = performQuery()
                    .observableResult()
                    .subscribeOn(backgroundScheduler)
                    .observeOn(mainScheduler)
                    .subscribe(this::onResult);
        }
    }

    public void pause() {
        if (subscription != null) {
            subscription.dispose();
            subscription = null;
        }
    }

    public void stop() {
        pause();
        close();
    }

    private void onResult(Result<T> result) {
        setResult((ResultSetIterator<T>) result.iterator());
    }

    @Override
    public ReactiveResult<T> performQuery() {
        return null;
    }
}
