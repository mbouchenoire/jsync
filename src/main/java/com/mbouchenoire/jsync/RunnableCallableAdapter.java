package com.mbouchenoire.jsync;

import java.util.concurrent.Callable;

/**
 * @author mbouchenoire
 */
class RunnableCallableAdapter implements Callable<Runnable> {

    private final Runnable runnable;

    public RunnableCallableAdapter(Runnable runnable) {
        if (runnable == null)
            throw new IllegalArgumentException("runnable");

        this.runnable = runnable;
    }

    public Runnable call() throws Exception {
        this.runnable.run();
        return runnable;
    }
}
