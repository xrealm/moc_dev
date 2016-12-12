package com.mao.dev.fuction.threadpool;

import android.os.Process;

import java.util.concurrent.Executor;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Created by Mao on 2016/12/11.
 */

public class DefaultExecutorSupplier {

    /**
     * Number of cores to decide the number of threads
     */
    public static final int NUMBER_OF_CORES = Runtime.getRuntime().availableProcessors();

    private final PriorityThreadPoolExecutor mBackgroudTasks;

    private final ThreadPoolExecutor mLightWeightBackgroudTasks;

    /**
     * thread pool executor for main thread task
     */
    private final Executor mMainThreadExecutor;

    /**
     * an instance of DefaultExecutorSupplier
     */
    private static DefaultExecutorSupplier sInstance;

    private static DefaultExecutorSupplier getInstance() {
        if (sInstance == null) {
            synchronized (DefaultExecutorSupplier.class) {
                sInstance = new DefaultExecutorSupplier();
            }
        }
        return sInstance;
    }

    private DefaultExecutorSupplier() {
        ThreadFactory threadFactory = new PriorityThreadFactory(Process.THREAD_PRIORITY_BACKGROUND);

        mBackgroudTasks = new PriorityThreadPoolExecutor(
                NUMBER_OF_CORES + 1,
                NUMBER_OF_CORES * 2,
                30L,
                TimeUnit.SECONDS,
                new LinkedBlockingDeque<Runnable>(),
                threadFactory);

        mLightWeightBackgroudTasks = new ThreadPoolExecutor(
                NUMBER_OF_CORES + 1,
                NUMBER_OF_CORES * 2,
                30L,
                TimeUnit.SECONDS,
                new LinkedBlockingDeque<Runnable>(),
                threadFactory
        );

        mMainThreadExecutor = new MainThreadExecutor();
    }

    /**
     * @return returns the thread pool executor for background task
     */
    public ThreadPoolExecutor getBackgroundTasks() {
        return mBackgroudTasks;
    }

    public ThreadPoolExecutor getLightWeightBackgroudTasks() {
        return mLightWeightBackgroudTasks;
    }

    /**
     * @return returns the thread pool executor for main thread task
     */
    public Executor getMainThreadExecutor() {
        return mMainThreadExecutor;
    }
}
