package com.mbouchenoire.jsync;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author mbouchenoire
 */
public final class Jsync {

    private static final long DEFAULT_TIMEOUT_SECONDS = 60;
    private static final ExecutorService DEFAULT_PARALLEL_EXECUTOR = Executors.newCachedThreadPool();

    private static final ConfigurableJsync INSTANCE
            = new ConfigurableJsync(DEFAULT_TIMEOUT_SECONDS, DEFAULT_PARALLEL_EXECUTOR);

    public static Set<ExecutionException> parallel(Runnable... commands) {
        return INSTANCE.parallel(commands);
    }

    public static Set<ExecutionException> parallel(Collection<Runnable> commands) {
        return INSTANCE.parallel(commands);
    }

    public static <T> void forEach(T[] items, Consumer<T> consumer) {
        INSTANCE.forEach(items, consumer);
    }

    public static <T> void forEach(Collection<T> items, Consumer<T> consumer) {
        INSTANCE.forEach(items, consumer);
    }

    public static <T, R> R[] map(T[] items, Function<T, R> function) {
        return INSTANCE.map(items, function);
    }

    public static <T, R> List<R> map(List<T> items, Function<T, R> function) {
        return INSTANCE.map(items, function);
    }

    public static <T> T[] filter(T[] items, Predicate<T> predicate) {
        return INSTANCE.filter(items, predicate);
    }

    public static <T> Collection<T> filter(Collection<T> items, Predicate<T> predicate) {
        return INSTANCE.filter(items, predicate);
    }
}
