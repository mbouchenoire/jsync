package com.mbouchenoire.jsync;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author mbouchenoire
 */
public final class Jsync {

    private static final long DEFAULT_TIMEOUT_SECONDS = 60;
    private static final ExecutorService PARALLEL_EXECUTOR = Executors.newCachedThreadPool();

    private static final Parallel parallel = new Parallel(PARALLEL_EXECUTOR, DEFAULT_TIMEOUT_SECONDS);
    private static final ForEach forEach = new ForEach(parallel);

    public static Map<Runnable, Throwable> parallel(Runnable... commands) {
        return parallel.invoke(commands);
    }

    public static Map<Runnable, Throwable> parallel(Collection<Runnable> commands) {
        return parallel.invoke(commands);
    }

    public static <T> Map<T, Throwable> forEach(T[] items, Consumer<T> consumer) {
        return forEach.invoke(items, consumer);
    }

    public static <T> Map<T, Throwable> forEach(Collection<T> items, Consumer<T> consumer) {
        return forEach.invoke(items, consumer);
    }
}
