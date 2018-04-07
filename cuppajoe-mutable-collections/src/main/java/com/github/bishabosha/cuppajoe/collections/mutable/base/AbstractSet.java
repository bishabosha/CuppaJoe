/*
 * Copyright (c) 2017. Jamie Thompson <bishbashboshjt@gmail.com>
 */

package com.github.bishabosha.cuppajoe.collections.mutable.base;

import java.util.Set;

/**
 * Created by Jamie on 10/07/2017.
 */
public abstract class AbstractSet<E> extends AbstractCollection<E> implements Set<E> {
    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof Set<?>)) {
            return false;
        }
        var collection = (Set<?>) obj;
        if (collection.size() != size()) {
            return false;
        }
        if (!containsAll(collection)) {
            return false;
        }
        return collection.containsAll(this);
    }
}
