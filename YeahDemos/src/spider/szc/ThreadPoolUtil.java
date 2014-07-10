
package spider.szc;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class ThreadPoolUtil {

    public static final String TAG = ThreadPoolUtil.class.getSimpleName();
    private static ExecutorService pool;

    public static void init() {
        pool = new ThreadPoolExecutor(0, Integer.MAX_VALUE, 3L, TimeUnit.SECONDS,
                new SynchronousQueue<Runnable>());
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
}
