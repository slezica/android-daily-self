package com.example.slezica.dailyself.ui;

import android.os.Bundle;
import android.support.annotation.CallSuper;
import android.support.annotation.LayoutRes;
import android.support.v7.app.AppCompatActivity;
import butterknife.ButterKnife;
import com.example.slezica.dailyself.app.ApplicationClass;
import io.reactivex.Observable;
import io.requery.Persistable;
import io.requery.reactivex.ReactiveEntityStore;

public abstract class BaseActivity extends AppCompatActivity {

    protected ApplicationClass app;
    protected ReactiveEntityStore<Persistable> dataStore;

    @Override
    @CallSuper
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutResource());

        app = (ApplicationClass) getApplication();
        dataStore = app.getDataStore();

        ButterKnife.bind(this);
    }

    @LayoutRes
    protected abstract int getLayoutResource();

    protected void subscribeTo(Observable<?> observable) {
        observable
                .subscribeOn(app.getBackgroundScheduler())
                .observeOn(app.getMainScheduler())
                .subscribe(next -> {}, this::handleError);
    }

    protected void handleError(Throwable error) {}
}
