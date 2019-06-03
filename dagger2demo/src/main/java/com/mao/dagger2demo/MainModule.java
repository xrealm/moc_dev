package com.mao.dagger2demo;

import android.content.Context;

import javax.inject.Named;
import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Mao on 15/7/18.
 */
@Module
public class MainModule {

//    private Context mContext;
//
//    public MainModule(Context context) {
//        mContext = context;
//    }
//
//    @Provides
//    public Context providesContext() {
//        return mContext;
//    }

    @Provides
    Person providesPerson(Context context) {
        return new Person(context);
    }

    @ActivityScope
    @PersonWithContext
    @Provides
    public Person providesPersonWithContext(Context context) {
        return new Person(context);
    }

    @ActivityScope
    @PersonWithName
    @Provides
    public Person providesPersonWithName() {
        return new Person("hhh");
    }
}
