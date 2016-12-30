package com.mbouchenoire.jsync;

/**
 * @author mbouchenoire
 */
public final class FunctionRunnableAdapter<T, V> implements Function<T, V>, Runnable {

    private final Function<T, V> function;
    private final T argument;

    private V runResult;

    public FunctionRunnableAdapter(Function<T, V> function, T argument) {
        super();

        if (function == null)
            throw new IllegalArgumentException("function");

        this.function = function;
        this.argument = argument;
    }

    public V apply(T item) {
        return this.function.apply(item);
    }

    public void run() {
        this.runResult = this.apply(this.argument);
    }

    public V getRunResult() {
        return runResult;
    }
}
