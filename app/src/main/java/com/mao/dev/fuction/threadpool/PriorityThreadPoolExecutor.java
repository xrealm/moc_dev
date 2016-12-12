package com.mao.dev.fuction.threadpool;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Created by Mao on 2016/12/11.
 */

public class PriorityThreadPoolExecutor extends ThreadPoolExecutor {

    public PriorityThreadPoolExecutor(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit, BlockingQueue<Runnable> workQueue, ThreadFactory threadFactory) {
        super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue, threadFactory);
    }

    @Override
    public Future<?> submit(Runnable task) {
        PriorityFetureTask fetureTask = new PriorityFetureTask((PriorityRunnable) task);
        execute(fetureTask);
        return fetureTask;
    }

    private static final class PriorityFetureTask extends FutureTask<PriorityRunnable>
            implements Comparable<PriorityFetureTask> {

        private final PriorityRunnable mPriorityRunnable;

        public PriorityFetureTask(PriorityRunnable runnable) {
            super(runnable, null);
            this.mPriorityRunnable = runnable;
        }

        @Override
        public int compareTo(PriorityFetureTask o) {
            return o.mPriorityRunnable.getPriority().ordinal() - mPriorityRunnable.getPriority().ordinal();
        }
    }
}
