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

    private static final ConfigurableJsync INSTANCE = ConfigurableJsyncFactory.createDefault();

    public static ConfigurableJsyncBuilder builder(ExecutorService executorService) {
        return new ConfigurableJsyncBuilder(executorService);
    }

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
