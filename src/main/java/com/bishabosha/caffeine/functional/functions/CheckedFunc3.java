/*
 * Copyright (c) 2017. Jamie Thompson <bishbashboshjt@gmail.com>
 */

package com.bishabosha.caffeine.functional.functions;

import com.bishabosha.caffeine.functional.Option;
import com.bishabosha.caffeine.functional.tuples.Tuple3;

public interface CheckedFunc3<A, B, C, R> {
    R apply(A a, B b, C c) throws Throwable;

    static <X,Y,Z,R> CheckedFunc3<X,Y,Z,R> of(CheckedFunc3<X, Y, Z, R> func) {
        return func;
    }

    default Func3<A, B, C, Option<R>> lifted() {
        return (x, y, z) -> {
            try {
                return Option.ofUnknown(apply(x, y, z));
            } catch (Throwable e) {
                return Option.nothing();
            }
        };
    }

    default CheckedFunc1<A, CheckedFunc1<B, CheckedFunc1<C, R>>> curried() {
        return x -> y -> z -> apply(x, y, z);
    }

    default CheckedFunc1<Tuple3<A, B, C>, R> tupled() {
        return x -> apply(x.$1(), x.$2(), x.$3());
    }
}
