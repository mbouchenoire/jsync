package com.mbouchenoire.jsync;

/**
 * Represents a function that accepts one argument and produces a result.
 *
 * @author mbouchenoire
 */
public interface Function<T, R> {

    /**
     * Apply this function to the given argument.
     *
     * @param arg the function argument
     * @return the function result
     */
    R apply(T arg);
}
