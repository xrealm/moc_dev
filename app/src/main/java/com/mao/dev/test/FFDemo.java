package com.mao.dev.test;

/**
 * Created by Mao on 2017/8/25.
 */

public class FFDemo {

    static {
        System.loadLibrary("ffmpeg");
        System.loadLibrary("myffmpeg");
    }

    public static native String avcodecinfo();
}
