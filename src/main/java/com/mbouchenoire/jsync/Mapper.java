package com.mbouchenoire.jsync;

import java.lang.reflect.Array;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author mbouchenoire
 */
class Mapper {

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

        final R[] mappedItemsArray = this.map((T[])items.toArray(), function);

        return Arrays.asList(mappedItemsArray);
    }

    public <T, R> R[] map(T[] items, Function<T, R> function) {
        if (items == null)
            throw new IllegalArgumentException("items");

        final List<FunctionRunnableAdapter<T, R>> adapters = new ArrayList<FunctionRunnableAdapter<T, R>>(items.length);

        for(T item: items) {
            final FunctionRunnableAdapter<T, R> functionRunnableAdapter = new FunctionRunnableAdapter<T, R>(function, item);
            adapters.add(functionRunnableAdapter);
        }

        final Runnable[] runnables = adapters.toArray(new Runnable[adapters.size()]);
        this.parallel.invoke(runnables);

        R[] mappedItemsArray = null;

        for(int i = 0; i < adapters.size(); i++) {
            final FunctionRunnableAdapter<T, R> adapter = adapters.get(i);
            final R mappedItem = adapter.getRunResult();

            if (mappedItemsArray == null) {
                mappedItemsArray = (R[])Array.newInstance(mappedItem.getClass(), items.length);
            }

            mappedItemsArray[i] = mappedItem;
        }

        return mappedItemsArray;
    }
}
