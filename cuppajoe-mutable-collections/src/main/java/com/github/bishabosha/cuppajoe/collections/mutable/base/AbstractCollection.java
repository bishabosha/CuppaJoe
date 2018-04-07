/*
 * Copyright (c) 2017. Jamie Thompson <bishbashboshjt@gmail.com>
 */

package com.github.bishabosha.cuppajoe.collections.mutable.base;

import java.util.Collection;
import java.util.Objects;

public abstract class AbstractCollection<E> extends AbstractArrayHelper<E> implements Collection<E> {

    @Override
    public boolean add(E e) {
        var before = size();
        replace(e);
        return size() > before;
    }

    protected abstract E replace(E e);

    public boolean remove(Object entry) {
        var before = size();
        pull(entry);
        return size() < before;
    }

    protected abstract E pull(Object entry);

    @Override
    public boolean containsAll(Collection<?> c) {
        for (var o : c) {
            if (!contains(o)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        Objects.requireNonNull(c, "c");
        var before = size();
        c.forEach(this::replace);
        return size() > before;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        Objects.requireNonNull(c, "c");
        var before = size();
        c.forEach(this::pull);
        return size() < before;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        Objects.requireNonNull(c, "c");
        var before = size();
        removeIf(x -> !c.contains(x));
        return size() < before;
    }
}
