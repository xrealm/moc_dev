package com.mao.dagger2demo;

import android.content.Context;

import dagger.Component;

/**
 * Created by Mao on 16/7/18.
 */
@ApplicationScope
@Component(modules = {AppModule.class})
public interface AppComponent {

    Context getContext();
}
