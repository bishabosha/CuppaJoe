/*
 * Copyright (c) 2017. Jamie Thompson <bishbashboshjt@gmail.com>
 */

package com.bishabosha.caffeine.functional.functions;

public interface Func3<A, B, C, R> {
    R apply(A a, B b, C c);
}
