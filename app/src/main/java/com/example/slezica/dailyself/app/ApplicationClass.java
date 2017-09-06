package com.example.slezica.dailyself.app;

import android.app.Application;
import com.example.slezica.dailyself.BuildConfig;
import com.example.slezica.dailyself.model.Models;
import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import io.requery.Persistable;
import io.requery.android.sqlite.DatabaseSource;
import io.requery.reactivex.ReactiveEntityStore;
import io.requery.reactivex.ReactiveSupport;
import io.requery.sql.Configuration;
import io.requery.sql.EntityDataStore;
import io.requery.sql.TableCreationMode;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ApplicationClass extends Application {

    public static final int DB_VERSION = 2;

    protected ExecutorService backgroundExecutor;
    protected Scheduler backgroundScheduler;
    protected Scheduler mainScheduler;

    private ReactiveEntityStore<Persistable> dataStore;

    @Override
    public void onCreate() {
        super.onCreate();

        backgroundExecutor = Executors.newCachedThreadPool();
        backgroundScheduler = Schedulers.from(backgroundExecutor);
        mainScheduler = AndroidSchedulers.mainThread();

        DatabaseSource source = new DatabaseSource(
                this,
                Models.DEFAULT,
                DB_VERSION
        );

        if (BuildConfig.DEBUG) {
            source.setLoggingEnabled(true);
            source.setTableCreationMode(TableCreationMode.DROP_CREATE);
        }

        Configuration configuration = source.getConfiguration();

        dataStore = ReactiveSupport.toReactiveStore(
                new EntityDataStore<Persistable>(configuration)
        );
    }

    public Scheduler getBackgroundScheduler() {
        return backgroundScheduler;
    }

    public Scheduler getMainScheduler() {
        return mainScheduler;
    }

    public ReactiveEntityStore<Persistable> getDataStore() {
        return dataStore;
    }
}
