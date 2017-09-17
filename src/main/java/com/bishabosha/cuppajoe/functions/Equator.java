/*
 * Copyright (c) 2017. Jamie Thompson <bishbashboshjt@gmail.com>
 */

package com.bishabosha.cuppajoe.functions;

import java.util.Objects;
import java.util.function.BiPredicate;

public interface Equator<E> extends BiPredicate<Object, E> {

    static <E> boolean standardTest(Object x, E y) {
        return Objects.equals(x, y);
    }
}