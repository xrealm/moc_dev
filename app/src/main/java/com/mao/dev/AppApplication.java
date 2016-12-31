package com.mao.dev;

import android.app.Application;

import com.orhanobut.logger.Logger;

/**
 * Created by Mao on 2016/11/18.
 */

public class AppApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        AppKit.setApplication(this);

        Logger.init("mao")
                .methodCount(1)
                .methodOffset(0);

    }
}
