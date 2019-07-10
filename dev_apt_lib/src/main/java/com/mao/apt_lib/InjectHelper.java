package com.mao.apt_lib;

import android.app.Activity;

import java.lang.reflect.Method;

/**
 * Created by Mao on 2019-07-10.
 */
public class InjectHelper {

    public static void bind(Activity activity) {
        Class<? extends Activity> clazz = activity.getClass();
        try {
            Class<?> bindViewClass = Class.forName(clazz.getName() + "_ViewBinding");
            Method method = bindViewClass.getMethod("bind", clazz);
            method.invoke(bindViewClass.newInstance(), activity);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
