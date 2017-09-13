/*
 * Copyright (c) 2017. Jamie Thompson <bishbashboshjt@gmail.com>
 */

package com.bishabosha.caffeine.functional.functions;

import com.bishabosha.caffeine.functional.Option;
import com.bishabosha.caffeine.functional.tuples.Tuple6;

public interface CheckedFunc6<A, B, C, D, E, F, R> {
    R apply(A a, B b, C c, D d, E e, F f) throws Throwable;

    static <U,V,W,X,Y,Z,R> CheckedFunc6<U,V,W,X,Y,Z,R> of(CheckedFunc6<U, V, W, X, Y, Z, R> func) {
        return func;
    }

    default Func6<A, B, C, D, E, F, Option<R>> lifted() {
        return (u, v, w, x, y, z) -> {
            try {
                return Option.ofUnknown(apply(u, v, w, x, y, z));
            } catch (Throwable e) {
                return Option.nothing();
            }
        };
    }

    default CheckedFunc1<A, CheckedFunc1<B, CheckedFunc1<C, CheckedFunc1<D, CheckedFunc1<E, CheckedFunc1<F, R>>>>>> curried() {
        return u -> v -> w -> x -> y -> z -> apply(u, v, w, x, y, z);
    }

    default CheckedFunc1<Tuple6<A, B, C, D, E, F>, R> tupled() {
        return x -> apply(x.$1(), x.$2(), x.$3(), x.$4(), x.$5(), x.$6());
    }
}
