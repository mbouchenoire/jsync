package com.mbouchenoire.jsync;

/**
 * @author mbouchenoire
 */
public final class ConsumerRunnableAdapter<T> implements Consumer<T>, Runnable {

    private final Consumer<T> consumer;
    private final T consumed;

    public ConsumerRunnableAdapter(Consumer<T> consumer, T consumed) {
        super();

        if (consumer == null)
            throw new IllegalArgumentException("consumer");

        this.consumer = consumer;
        this.consumed = consumed;
    }

    public void accept(T t) {
        this.consumer.accept(t);
    }

    public void run() {
        this.accept(this.consumed);
    }
}
