package com.mao.dev;

import android.app.Application;

/**
 * Created by Mao on 2016/11/18.
 */

public class AppApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        AppKit.setApplication(this);
    }
}
