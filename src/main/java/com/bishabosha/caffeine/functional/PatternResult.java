/*
 * Copyright (c) 2017. Jamie Thompson <bishbashboshjt@gmail.com>
 */

package com.bishabosha.caffeine.functional;

import java.util.*;

public class PatternResult implements Iterable<Object> {
    List list;

    public PatternResult() {
        list = new ArrayList();
    }

    private PatternResult(Object... objects) {
        list = Arrays.asList(objects);
    }

    public static PatternResult of(Object... objects) {
        return new PatternResult(objects);
    }

    public int size() {
        return list.size();
    }

    public boolean isEmpty() {
        return list.isEmpty();
    }

    public Object get(int index) {
        return list.get(index);
    }

    public boolean add(Object obj) {
        return list.add(obj);
    }

    @Override
    public Iterator<Object> iterator() {
        return list.iterator();
    }

    /**
     * Standard Iterative, In-order, Depth First Search
     * @return A Tuple fromList unknown type that contains only singleton values
     */
    public PatternResult flatten() {
        list = Library.flatten(PatternResult.class, this);
        return this;
    }
}
