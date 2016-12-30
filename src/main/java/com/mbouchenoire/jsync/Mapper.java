package com.mbouchenoire.jsync;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author mbouchenoire
 */
public class Mapper {

    private final Parallel parallel;

    public Mapper(Parallel parallel) {
        super();

        if (parallel == null)
            throw new IllegalArgumentException("forEach");

        this.parallel = parallel;
    }

    public <T, R> List<R> map(List<T> items, Function<T, R> function) {
        if (items == null)
            throw new IllegalArgumentException("items");

        return this.map((T[])items.toArray(), function);
    }

    public <T, R> List<R> map(T[] items, Function<T, R> function) {
        if (items == null)
            throw new IllegalArgumentException("items");

        final FunctionRunnableAdapter<T, R>[] functionRunnables = new FunctionRunnableAdapter[items.length];

        for(int i = 0; i < items.length; i++) {
            functionRunnables[i] = new FunctionRunnableAdapter<T, R>(function, items[i]);
        }

        this.parallel.invoke(functionRunnables);

        final List<R> mappedItems = new ArrayList<R>(items.length);

        for(int i = 0; i < functionRunnables.length; i++) {
            mappedItems.add(functionRunnables[i].getRunResult());
        }

        return mappedItems;
    }
}
