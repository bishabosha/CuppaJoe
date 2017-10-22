/*
 * Copyright (c) 2017. Jamie Thompson <bishbashboshjt@gmail.com>
 */

package com.bishabosha.cuppajoe.collections.mutable.base;

import java.util.Set;

/**
 * Created by Jamie on 10/07/2017.
 */
public abstract class AbstractSet<E> extends AbstractCollection<E> implements Set<E>{
    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof Set<?>)) {
            return false;
        }
        Set<?> collection = (Set<?>) obj;
        if (collection.size() != size()) {
            return false;
        }
        if (!containsAll(collection)) {
            return false;
        }
        if (!collection.containsAll(this)) {
            return false;
        }
        return true;
    }
}
