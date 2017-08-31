/*
 * Copyright (c) 2017. Jamie Thompson <bishbashboshjt@gmail.com>
 */

package com.bishabosha.caffeine.functional.functions;

public interface Func2<A, B, R> {
    R apply(A a, B b);
}
