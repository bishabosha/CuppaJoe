/*
 * Copyright (c) 2017. Jamie Thompson <bishbashboshjt@gmail.com>
 */

package com.bishabosha.caffeine.functional;

import com.bishabosha.caffeine.base.AbstractBase;

import java.util.*;

public class PatternResult extends AbstractBase<Object> {
    private List<Object> list;

    private PatternResult(Object... objects) {
        list = Arrays.asList(objects);
    }

    private PatternResult() {
        list = new ArrayList<>();
    }

    public static PatternResult create() {
        return new PatternResult();
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

    @Override
    public boolean contains(Object o) {
        return list.contains(o);
    }

    public <T> T get(int index) {
        try {
            return (T) list.get(index);
        } catch (ClassCastException cce) {
            throw new ClassCastException("PatternResult.get(" + index + ") is not of the type requested.");
        } catch (IndexOutOfBoundsException ioobe) {
            throw new IllegalArgumentException("Not enough values contained in PatternResult.");
        }
    }

    public boolean add(Object obj) {
        return list.add(obj);
    }

    @Override
    public Iterator<Object> iterator() {
        return list.iterator();
    }

    @Override
    public boolean equals(Object obj) {
        return Option.of(obj)
                     .cast(PatternResult.class)
                     .map(x -> Objects.equals(x.list, list))
                     .orElse(false);
    }

    /**
     * Standard Iterative, In-order, Depth First Search
     * @return A Tuple4 fromList unknown type that contains only singleton values
     */
    public PatternResult flatten() {
        list = Library.flatten(PatternResult.class, this);
        return this;
    }
}
