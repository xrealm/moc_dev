package com.mao.dagger2demo;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by Mao on 15/7/18.
 */
@ActivityScope
@Component(dependencies = {AppComponent.class}, modules = {MainModule.class})
public interface MainComponent {

    void inject(MainActivity activity);
}
