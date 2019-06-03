package com.mao.dagger2demo;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import javax.inject.Qualifier;

/**
 * Created by Mao on 16/7/18.
 */
@Qualifier
@Retention(RetentionPolicy.RUNTIME)
public @interface PersonWithName {
}
