/*
 * Copyright (c) 2017. Jamie Thompson <bishbashboshjt@gmail.com>
 */

package com.github.bishabosha.cuppajoe.collections.mutable.internal;

import com.github.bishabosha.cuppajoe.util.Iterators;

public abstract class AbstractBase<E> implements Iterable<E> {

    protected int size = 0;

    /**
     * It is recommended that you override this
     *
     * @return the size of this Iterable
     */
    public int size() {
        return size;
    }

    public boolean isEmpty() {
        return size() == 0;
    }

    public abstract boolean contains(Object o);

    @Override
    public int hashCode() {
        return Iterators.hash(iterator());
    }

    public String toString() {
        return Iterators.toString('[', ']', iterator());
    }
}
