package com.mbouchenoire.jsync;

/**
 * @author mbouchenoire
 */
public final class PredicateFunctionAdapter<T> implements Predicate<T>, Function<T, Boolean> {

    private final Predicate<T> predicate;

    public PredicateFunctionAdapter(Predicate<T> predicate) {
        super();

        if (predicate == null)
            throw new IllegalArgumentException("predicate");

        this.predicate = predicate;
    }

    public boolean test(T arg) {
        return this.predicate.test(arg);
    }

    public Boolean apply(T arg) {
        return this.test(arg);
    }
}
