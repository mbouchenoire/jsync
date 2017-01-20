package com.mbouchenoire.jsync;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author mbouchenoire
 */
public final class ConfigurableJsyncFactory {

    public static final long DEFAULT_TIMEOUT_SECONDS = 60;
    public static final ExecutorService DEFAULT_PARALLEL_EXECUTOR = Executors.newCachedThreadPool();

    public static final ConfigurableJsync createDefault() {
        return new ConfigurableJsyncBuilder(DEFAULT_PARALLEL_EXECUTOR)
                    .timeout(DEFAULT_TIMEOUT_SECONDS)
                    .build();
    }
}
