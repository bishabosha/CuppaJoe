/*
 * Copyright (c) 2017. Jamie Thompson <bishbashboshjt@gmail.com>
 */

package com.bishabosha.caffeine.base;

import java.util.Iterator;

public interface SequenceHelper {

    static <C, E> boolean equals(Iterable<E> base, Object obj, Class<C> clazz) {
        if (!(clazz.isInstance(obj))) {
            return false;
        }
        Iterator<E> it = ((Iterable) obj).iterator();
        for (E term: base) {
            if (!it.hasNext() || !term.equals(it.next())) {
                return false;
            }
        }
        return true;
    }
}
