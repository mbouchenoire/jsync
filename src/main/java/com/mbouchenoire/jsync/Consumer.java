package com.mbouchenoire.jsync;

/**
 * @author mbouchenoire
 */
public interface Consumer<T> {

    void accept(T t);
}
