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
    private static final ExecutorService PARALLEL_EXECUTOR = Executors.newCachedThreadPool();

    private static final Parallel PARALLEL = new Parallel(PARALLEL_EXECUTOR, DEFAULT_TIMEOUT_SECONDS);
    private static final ForEach FOREACH = new ForEach(PARALLEL);
    private static final Mapper MAPPER = new Mapper(PARALLEL);
    private static final Filter FILTER = new Filter(MAPPER);

    public static Set<ExecutionException> parallel(Runnable... commands) {
        return  PARALLEL.invoke(commands);
    }

    public static Set<ExecutionException> parallel(Collection<Runnable> commands) {
        return PARALLEL.invoke(commands);
    }

    public static <T> void forEach(T[] items, Consumer<T> consumer) {
        FOREACH.invoke(items, consumer);
    }

    public static <T> void forEach(Collection<T> items, Consumer<T> consumer) {
        FOREACH.invoke(items, consumer);
    }

    public static <T, R> R[] map(T[] items, Function<T, R> function) {
        return MAPPER.map(items, function);
    }

    public static <T, R> List<R> map(List<T> items, Function<T, R> function) {
        return MAPPER.map(items, function);
    }

    public static <T> T[] filter(T[] items, Predicate<T> predicate) {
        return FILTER.filter(items, predicate);
    }

    public static <T> Collection<T> filter(Collection<T> items, Predicate<T> predicate) {
        return FILTER.filter(items, predicate);
    }
}
