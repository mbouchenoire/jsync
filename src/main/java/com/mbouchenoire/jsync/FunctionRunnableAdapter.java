package com.mbouchenoire.jsync;

/**
 * Adapts the {@link Function} interface into the {@link Runnable} interface, allowing
 * {@link Function} instances to be manipulated by the {@link java.util.concurrent} api.
 *
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

    /**
     * Apply the contained {@link Function} {@link Function#apply(Object)} function
     * on the given argument.
     *
     * @param arg the input argument
     * @return the function result
     */
    public V apply(T arg) {
        return this.function.apply(arg);
    }

    /**
     * Apply the contained {@link Function} {@link Function#apply(Object)} function
     * on the instance's {@link T argument} attribute, and store the result within the instance.
     *
     * @see FunctionRunnableAdapter#getRunResult()
     */
    public void run() {
        this.runResult = this.apply(this.argument);
    }

    /**
     *  Returns the last result of this instance's {@link FunctionRunnableAdapter#run()} method.
     *
     * @return the last result of this instance's {@link FunctionRunnableAdapter#run()} method
     */
    public V getRunResult() {
        return runResult;
    }
}
