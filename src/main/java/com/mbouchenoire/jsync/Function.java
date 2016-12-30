package com.mbouchenoire.jsync;

/**
 * @author mbouchenoire
 */
public interface Function<T, R> {

    R apply(T item);
}
