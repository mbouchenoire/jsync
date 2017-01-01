package com.mbouchenoire.jsync;

/**
 * Represents an operation that accepts a single input argument and returns no result.
 *
 * Unlike the {@link Function} interface, {@link Consumer} is expected to operate via
 * side-effects (e.g. output operations).
 *
 * @author mbouchenoire
 */
public interface Consumer<T> {

    /**
     * Performs this operation on the given argument.
     *
     * @param arg the input argument
     */
    void accept(T arg);
}
