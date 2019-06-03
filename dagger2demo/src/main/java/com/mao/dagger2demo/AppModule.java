package com.mao.dagger2demo;

import android.content.Context;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Mao on 16/7/18.
 */
@Module
public class AppModule {

    private Context mContext;

    public AppModule(Context context) {
        mContext = context;
    }

    @ApplicationScope
    @Provides
    public Context providesContext() {
        return mContext;
    }
}
