package com.mbouchenoire.jsync;

/**
 * Adapts the {@link Consumer} interface into the {@link Runnable} interface, allowing
 * {@link Consumer} instances to be manipulated by the {@link java.util.concurrent} api.
 *
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

    /**
     * Performs the contained {@link Consumer} {@link Consumer#accept(Object)} method
     * on the given argument.
     *
     * @param arg the input argument
     */
    public void accept(T arg) {
        this.consumer.accept(arg);
    }

    /**
     * Performs the contained {@link Consumer consumer} {@link Consumer #accept(Object)} method
     * on the instance's {@link T consumed} attribute.
     */
    public void run() {
        this.accept(this.consumed);
    }
}
