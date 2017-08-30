/*
 * Copyright (c) 2017. Jamie Thompson <bishbashboshjt@gmail.com>
 */

package com.bishabosha.caffeine.base;

public abstract class AbstractBase<E> implements Iterable<E>{

    protected int size = 0;

    public int size() {
        return size;
    }

    public boolean isEmpty() {
        return size() == 0;
    }

    public abstract boolean contains(Object o);

    @Override
    public int hashCode() {
        int hash = 1;
        for (E element : this) {
            hash = 29 * hash + (element != null ? element.hashCode() : 0);
        }
        return hash;
    }

    public String toString() {
        return Iterables.toString('[', ']', iterator());
    }
}
