package com.mbouchenoire.jsync;

import java.util.concurrent.ExecutorService;

/**
 * @author mbouchenoire
 */
public final class ConfigurableJsyncBuilder {

    private long timeout = 0;
    private ExecutorService executorService = null;

    public ConfigurableJsyncBuilder(ExecutorService executorService) {
        super();

        if (executorService == null)
            throw new IllegalArgumentException("executorService");

        this.executorService = executorService;
    }

    public ConfigurableJsyncBuilder timeout(long timeout) {
        this.timeout = timeout;
        return this;
    }

    public ConfigurableJsyncBuilder executorService(ExecutorService executorService) {
        if (executorService == null)
            throw new IllegalArgumentException("executorService");

        this.executorService = executorService;
        return this;
    }

    public ConfigurableJsync build() {
        return new ConfigurableJsync(timeout, executorService);
    }
}
