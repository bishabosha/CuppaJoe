/*
 * Copyright (c) 2017. Jamie Thompson <bishbashboshjt@gmail.com>
 */

package io.cuppajoe.collections.mutable.base;

import io.cuppajoe.Iterables;

public abstract class AbstractBase<E> implements Iterable<E>{

    protected int size = 0;

    /**
     * It is recommended that you override this
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
        return Iterables.hash(this);
    }

    public String toString() {
        return Iterables.toString('[', ']', iterator());
    }
}
