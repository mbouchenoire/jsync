package com.mbouchenoire.jsync;

import java.lang.reflect.Array;
import java.util.*;

/**
 * @author mbouchenoire
 */
class Filter {

    private final Mapper mapper;

    public Filter(Mapper mapper) {
        super();

        if (mapper == null)
            throw new IllegalArgumentException("mapper");

        this.mapper = mapper;
    }

    public <T> Collection<T> filter(Collection<T> items, Predicate<T> predicate) {
        if (items == null)
            throw new IllegalArgumentException("items");

        final T[] filteredItemsArray = this.filter((T[])items.toArray(), predicate);

        return Arrays.asList(filteredItemsArray);
    }

    public <T> T[] filter(T[] items, Predicate<T> predicate) {
        if (items == null)
            throw new IllegalArgumentException("items");

        if (items.length == 0)
            return emptyGenericArray();

        final List<T> listItems = Arrays.asList(items);
        final Function<T, Boolean> functionAdapter = new PredicateFunctionAdapter<T>(predicate);

        final List<Boolean> testResults = this.mapper.map(listItems, functionAdapter);

        final ArrayList<T> filteredItems = new ArrayList<T>(listItems.size());

        for(int i = 0; i < listItems.size(); i++) {
            final T item = listItems.get(i);
            final Boolean test = testResults.get(i);

            if (test) {
                filteredItems.add(item);
            }
        }

        if (filteredItems.isEmpty()) {
            return emptyGenericArray();
        }

        final T[] filteredItemsArray = (T[])Array.newInstance(filteredItems.get(0).getClass(), filteredItems.size());

        for(int i = 0; i < filteredItems.size(); i++) {
            filteredItemsArray[i] = filteredItems.get(i);
        }

        return filteredItemsArray;
    }

    private static <T> T[] emptyGenericArray()  {
        return (T[])new Object[0];
    }
}
