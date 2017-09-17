/*
 * Copyright (c) 2017. Jamie Thompson <bishbashboshjt@gmail.com>
 */

package com.bishabosha.cuppajoe.collections.mutable.base;

import java.util.Collection;
import java.util.Iterator;

public class BasicSet<E> extends AbstractSet<E> {

    protected AbstractSet<? extends E> store;

    @Override
    public boolean add(E e) {
        throw new UnsupportedOperationException("add() is not supported");
    }

    @Override
    protected E replace(E e) {
        throw new UnsupportedOperationException("replace() is not supported");
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        throw new UnsupportedOperationException("addAll() is not supported");
    }

    @Override
    public boolean contains(Object o) {
        return store.contains(o);
    }

    @Override
    public boolean remove(Object o) {
        return store.remove(o);
    }

    @Override
    protected E pull(Object entry) {
        return store.pull(entry);
    }

    @Override
    public int size() {
        return store.size();
    }

    @Override
    public boolean isEmpty() {
        return size() == 0;
    }

    @Override
    public void clear() {
        store.clear();
    }

    @Override
    public Iterator<E> iterator() {
        return (Iterator<E>) store.iterator();
    }
}
