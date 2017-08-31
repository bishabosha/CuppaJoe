/*
 * Copyright (c) 2017. Jamie Thompson <bishbashboshjt@gmail.com>
 */

package com.bishabosha.caffeine.base;

import java.util.Objects;

public interface Equator<E> {

    static <E> boolean standardTest(Object x, E y) {
        return Objects.equals(x, y);
    }

    boolean apply(Object newValue, E stored);
}