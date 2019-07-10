package com.mao.dev.fuction.threadpool;

import android.os.Process;
import androidx.annotation.NonNull;

import java.util.concurrent.ThreadFactory;

/**
 * Created by Mao on 2016/12/11.
 */

public class PriorityThreadFactory implements ThreadFactory {

    private final int mThreadPriotiry;

    public PriorityThreadFactory(int threadPriotiry) {
        mThreadPriotiry = threadPriotiry;
    }

    @Override
    public Thread newThread(@NonNull final Runnable runnable) {
        Runnable wrapperRunnable = new Runnable() {
            @Override
            public void run() {
                Process.setThreadPriority(mThreadPriotiry);
                runnable.run();
            }
        };
        return new Thread(wrapperRunnable);
    }
}
