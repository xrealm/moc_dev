package com.mao.dev.fuction.threadpool;

/**
 * Created by Mao on 2016/12/11.
 */

public class PriorityRunnable implements Runnable {

    private final Priority mPriority;

    public PriorityRunnable(Priority priority) {
        mPriority = priority;
    }

    @Override
    public void run() {

    }

    public Priority getPriority() {
        return mPriority;
    }
}
