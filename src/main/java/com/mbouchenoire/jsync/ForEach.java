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

    public <T> Map<T, Throwable> invoke(Collection<T> items, Consumer<T> consumer) {
        if (items == null)
            throw new IllegalArgumentException("items");

        return this.invoke((T[])items.toArray(), consumer);
    }

    public <T> Map<T, Throwable> invoke(T[] items, Consumer<T> consumer) {
        if (items == null)
            throw new IllegalArgumentException("items");

        if (consumer == null)
            throw new IllegalArgumentException("consumer");

        final Map<Runnable,  T> runnableItemMap = new ConcurrentHashMap<Runnable, T>(items.length);

        for(T item: items) {
            if (item == null) {
                throw new IllegalArgumentException("Cannot consume a null item.");
            }

            final Runnable runnable = new ConsumerRunnableAdapter<T>(consumer, item);
            runnableItemMap.put(runnable, item);
        }

        final Map<Runnable, Throwable> parallelErrors = this.parallel.invoke(runnableItemMap.keySet());
        final Map<T, Throwable> forEachErrors = new ConcurrentHashMap<T, Throwable>(parallelErrors.size());

        for(Runnable errorRunnable: parallelErrors.keySet()) {
            final T errorItem = runnableItemMap.get(errorRunnable);
            forEachErrors.put(errorItem, parallelErrors.get(errorRunnable));
        }

        return forEachErrors;
    }
}
