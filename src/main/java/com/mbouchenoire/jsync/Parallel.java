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

    public Map<Runnable, Throwable> invoke(Collection<Runnable> commands) {
        if (commands == null)
            throw new IllegalArgumentException("commands");

        return invoke(commands.toArray(new Runnable[commands.size()]));
    }

    public Map<Runnable, Throwable> invoke(Runnable... commands) {
        if (commands == null)
            throw new IllegalArgumentException("commands");

        final Map<Runnable, Throwable> errors = new ConcurrentHashMap<Runnable, Throwable>(commands.length);

        if (commands.length == 0)
            return errors;

        final Set<Callable<Runnable>> callables = new HashSet<Callable<Runnable>>();

        for(Runnable runnable: commands) {
            final RunnableCallableAdapter adapter = new RunnableCallableAdapter(runnable);
            callables.add(adapter);
        }

        try {
            final List<Future<Runnable>> futures = this.executorService.invokeAll(callables);

            for(int futureIndex = 0; futureIndex < futures.size(); futureIndex++) {
                final Future<Runnable> future = futures.get(futureIndex);

                try {
                    future.get(this.timeoutSeconds, TimeUnit.SECONDS);
                } catch (InterruptedException ie) {
                    errors.put(commands[futureIndex], ie);
                } catch (ExecutionException ee) {
                    errors.put(commands[futureIndex], ee.getCause());
                } catch (TimeoutException toe) {
                    errors.put(commands[futureIndex], toe);
                }
            }
        } catch (InterruptedException ie) {
            throw new IllegalStateException("This should never happen (what if though ?).");
        }

        return errors;
    }
}
