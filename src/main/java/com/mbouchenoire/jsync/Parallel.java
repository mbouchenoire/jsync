package com.mbouchenoire.jsync;

import java.util.*;
import java.util.concurrent.*;

/**
 * @author mbouchenoire
 */
class Parallel {

    private final ExecutorService executorService;
    private final long timeoutSeconds;

    public Parallel(ExecutorService executorService, long timeoutSeconds) {
        super();

        if (executorService == null)
            throw new IllegalArgumentException("executorService");

        if (timeoutSeconds <= 0) {
            throw new IllegalArgumentException("timeoutSeconds must be greater than 0");
        }

        this.executorService = executorService;
        this.timeoutSeconds = timeoutSeconds;
    }

    public Set<ExecutionException> invoke(Collection<Runnable> commands) {
        if (commands == null)
            throw new IllegalArgumentException("commands");

        return this.invoke(commands.toArray(new Runnable[commands.size()]));
    }

    public Set<ExecutionException> invoke(Runnable... commands) {
        if (commands == null)
            throw new IllegalArgumentException("commands");

        if (commands.length == 0)
            return new HashSet<ExecutionException>(0);

        final Set<Callable<Runnable>> callables = new HashSet<Callable<Runnable>>();

        for(Runnable runnable: commands) {
            if (runnable == null) {
                throw new IllegalArgumentException("null runnable");
            }

            final Callable<Runnable> adapter = Executors.callable(runnable, runnable);
            callables.add(adapter);
        }

        final Set<ExecutionException> errors = new HashSet<ExecutionException>();

        try {
            final List<Future<Runnable>> futures = this.executorService.invokeAll(callables);

            for(int futureIndex = 0; futureIndex < futures.size(); futureIndex++) {
                final Future<Runnable> future = futures.get(futureIndex);

                try {
                    future.get(this.timeoutSeconds, TimeUnit.SECONDS);
                } catch (InterruptedException ie) {
                    throw new IllegalStateException(ie);
                } catch (ExecutionException ee) {
                    errors.add(ee);
                } catch (TimeoutException toe) {
                    throw new IllegalStateException(toe);
                }
            }
        } catch (InterruptedException ie) {
            throw new IllegalStateException("This should never happen (what if though ?).");
        }

        return errors;
    }
}
