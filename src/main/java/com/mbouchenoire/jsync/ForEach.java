package com.mbouchenoire.jsync;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author mbouchenoire
 */
class ForEach {

    private final Parallel parallel;

    public ForEach(Parallel parallel) {
        super();

        if (parallel == null)
            throw new IllegalArgumentException("parallel");

        this.parallel = parallel;
    }

    public <T> void invoke(Collection<T> items, Consumer<T> consumer) {
        if (items == null)
            throw new IllegalArgumentException("items");

        this.invoke((T[])items.toArray(), consumer);
    }

    public <T> void invoke(T[] items, Consumer<T> consumer) {
        if (items == null)
            throw new IllegalArgumentException("items");

        if (consumer == null)
            throw new IllegalArgumentException("consumer");

        final Runnable[] runnables = new Runnable[items.length];

        for(int i = 0; i < items.length; i++) {
            final T item = items[i];

            if (item == null) {
                throw new IllegalArgumentException("Cannot consume a null item.");
            }

            runnables[i] = new ConsumerRunnableAdapter<T>(consumer, item);
        }

        this.parallel.invoke(runnables);
    }
}
