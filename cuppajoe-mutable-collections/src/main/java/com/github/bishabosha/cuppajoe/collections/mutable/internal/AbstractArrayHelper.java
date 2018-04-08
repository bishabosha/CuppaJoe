/*
 * Copyright (c) 2017. Jamie Thompson <bishbashboshjt@gmail.com>
 */

package com.github.bishabosha.cuppajoe.collections.mutable.internal;

import java.util.Arrays;

public abstract class AbstractArrayHelper<E> extends AbstractBase<E> {

    public Object[] toArray() {
        var a = new Object[size()];
        var i = 0;
        for (var element : this) {
            a[i++] = element;
        }
        return a;
    }

    @SuppressWarnings("unchecked")
    public <T> T[] toArray(T[] a) {
        if (a.length < size()) {
            a = (T[]) Arrays.copyOf(a, size(), a.getClass());
        }
        var i = 0;
        for (var element : this) {
            a[i++] = (T) element;
        }
        if (a.length > size()) {
            a[size()] = null;
        }
        return a;
    }
}
