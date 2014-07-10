
package org.yeah.util;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class ThreadPoolUtil {

    public static final String TAG = ThreadPoolUtil.class.getSimpleName();
    private static ExecutorService pool;

    private static final int POOL_SIZE = 3;

    public static void init() {
        // pool = new ThreadPoolExecutor(0, Integer.MAX_VALUE, 3L,
        // TimeUnit.SECONDS,
        // new SynchronousQueue<Runnable>());
        int cpuNums = Runtime.getRuntime().availableProcessors();
        pool = Executors.newFixedThreadPool(cpuNums * POOL_SIZE);
    }

    public static void uninit() {
        if (pool != null) {
            pool.shutdown();
            pool = null;
        }
    }

    public static ExecutorService getThreadPool() {
        return pool;
    }

    public static void execute(Runnable run) {
        if (pool == null) {
            init();
        }
        pool.execute(run);
    }
    
    public static <T> Future<T> submit(Callable<T> task) {
        if (pool == null) {
            init();
        }
       return pool.submit(task);
    }
}
