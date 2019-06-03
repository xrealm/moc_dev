package com.mao.dagger2demo;

import android.content.Context;

/**
 * Created by Mao on 15/7/18.
 */
public class Person {

    public Person() {
        System.out.println("Person ");
    }

    public Person(String name) {
        System.out.println("Person with name : " + name);
    }

    public Person(Context context) {
        System.out.println("Person with context " + context);
    }
}
