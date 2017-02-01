package com.mbouchenoire.jsync;

import java.util.concurrent.ExecutionException;

/**
 * @author mbouchenoire
 */
public interface ExecutionExceptionHandler {

    void handle(ExecutionException executionException);
}
