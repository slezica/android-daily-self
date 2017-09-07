package com.example.slezica.dailyself.ui;

import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.annotation.CallSuper;
import android.support.annotation.LayoutRes;
import android.support.annotation.MenuRes;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import butterknife.ButterKnife;
import com.example.slezica.dailyself.app.ApplicationClass;
import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.requery.Persistable;
import io.requery.reactivex.ReactiveEntityStore;

public abstract class BaseActivity extends AppCompatActivity {

    protected ApplicationClass app;
    protected ReactiveEntityStore<Persistable> dataStore;
    protected CompositeDisposable subs;

    @LayoutRes
    protected abstract int getLayoutResource();

    @MenuRes
    protected int getMenuResource() {
        return 0;
    }

    @Override
    @CallSuper
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutResource());

        app = (ApplicationClass) getApplication();
        dataStore = app.getDataStore();
        subs = new CompositeDisposable();

        ButterKnife.bind(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        subs.dispose();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        final int menuRes = getMenuResource();

        if (menuRes != 0) {
            getMenuInflater().inflate(menuRes, menu);

            for (int i = 0; i < menu.size(); i++) {
                menu.getItem(i)
                        .getIcon()
                        .mutate()
                        .setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_ATOP);
            }
        }

        return super.onCreateOptionsMenu(menu);
    }

    protected void subscribeTo(Single<?> single) {
        subscribeTo(single.toObservable());
    }

    protected void subscribeTo(Observable<?> observable) {
        final Disposable sub = observable
                .subscribeOn(app.getBackgroundScheduler())
                .observeOn(app.getMainScheduler())
                .subscribe(next -> {}, this::handleError);

        subs.add(sub);
    }

    protected void handleError(Throwable error) {}
}
