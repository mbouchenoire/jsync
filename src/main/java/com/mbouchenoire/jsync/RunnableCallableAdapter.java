package com.mbouchenoire.jsync;

import java.util.concurrent.Callable;

/**
 * Adapts the {@link Runnable} interface into the {@link Callable} interface,
 * easing {@link Runnable} usage with the {@link java.util.concurrent} api.
 *
 * @author mbouchenoire
 */
class RunnableCallableAdapter implements Callable<Runnable> {

    private final Runnable runnable;

    public RunnableCallableAdapter(Runnable runnable) {
        if (runnable == null)
            throw new IllegalArgumentException("runnable");

        this.runnable = runnable;
    }

    /**
     * Performs the contained {@link Runnable} {@link Runnable#run()} method.
     *
     * @return the instance's {@link Runnable runnable} attribute
     * @throws Exception never, as the {@link Runnable#run()} method cannot
     *                      throw checked exceptions
     */
    public Runnable call() throws Exception {
        this.runnable.run();
        return runnable;
    }
}
