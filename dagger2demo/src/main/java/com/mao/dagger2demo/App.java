package com.mao.dagger2demo;

import android.app.Application;

/**
 * Created by Mao on 16/7/18.
 */
public class App extends Application {

    public static AppComponent appComponent;

    @Override
    public void onCreate() {
        super.onCreate();
        appComponent = DaggerAppComponent.builder()
                .appModule(new AppModule(this))
                .build();
    }
}
