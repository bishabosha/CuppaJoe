/*
 * Copyright (c) 2017. Jamie Thompson <bishbashboshjt@gmail.com>
 */

package com.bishabosha.cuppajoe.collections.mutable.base;

import java.util.Arrays;

public abstract class AbstractArrayHelper<E> extends AbstractBase<E> {

    public Object[] toArray() {
        Object[] a = new Object[size()];
        int i = 0;
        for (E element: this) {
            a[i++] = element;
        }
        return a;
    }

    @SuppressWarnings("unchecked")
    public <T> T[] toArray(T[] a) {
        if (a.length < size()) {
            a = (T[]) Arrays.copyOf(a, size(), a.getClass());
        }
        int i = 0;
        for (E element: this) {
            a[i++] = (T)element;
        }
        if (a.length > size()) {
            a[size()] = null;
        }
        return a;
    }
}
