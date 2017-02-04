package com.mbouchenoire.jsync;

import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;

/**
 * @author mbouchenoire
 */
public final class ConfigurableJsync {

    private final Parallel parallel;
    private final ForEach forEach;
    private final Mapper mapper;
    private final Filter filter;

    public ConfigurableJsync(ExecutorService executorService, long timeout, ExecutionExceptionHandler executionExceptionHandler) {
        super();

        if (timeout <= 0)
            throw new IllegalArgumentException("timeout must be greater than 0");

        if (executorService == null)
            throw new IllegalArgumentException("executorService");

        this.parallel = new Parallel(executorService, timeout, executionExceptionHandler);
        this.forEach = new ForEach(parallel);
        this.mapper =  new Mapper(parallel);
        this.filter = new Filter(mapper);
    }

    public void parallel(Runnable... commands) {
        this.parallel.invoke(commands);
    }

    public void parallel(Collection<Runnable> commands) {
        this.parallel.invoke(commands);
    }

    public <T> void forEach(T[] items, Consumer<T> consumer) {
        this.forEach.invoke(items, consumer);
    }

    public <T> void forEach(Collection<T> items, Consumer<T> consumer) {
        this.forEach.invoke(items, consumer);
    }

    public <T, R> R[] map(T[] items, Function<T, R> function) {
        return this.mapper.map(items, function);
    }

    public <T, R> List<R> map(List<T> items, Function<T, R> function) {
        return this.mapper.map(items, function);
    }

    public <T> T[] filter(T[] items, Predicate<T> predicate) {
        return this.filter.filter(items, predicate);
    }

    public <T> Collection<T> filter(Collection<T> items, Predicate<T> predicate) {
        return this.filter.filter(items, predicate);
    }
}
