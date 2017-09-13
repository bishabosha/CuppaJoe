/*
 * Copyright (c) 2017. Jamie Thompson <bishbashboshjt@gmail.com>
 */
package com.bishabosha.caffeine.functional.functions;

import com.bishabosha.caffeine.functional.Option;
import com.bishabosha.caffeine.functional.tuples.Tuple4;

public interface CheckedFunc4<A, B, C, D, R> {
    R apply(A a, B b, C c, D d) throws Throwable;

    static <W,X,Y,Z,R> CheckedFunc4<W,X,Y,Z,R> of(CheckedFunc4<W, X, Y, Z, R> func) {
        return func;
    }

    default Func4<A, B, C, D, Option<R>> lifted() {
        return (w, x, y, z) -> {
            try {
                return Option.ofUnknown(apply(w, x, y, z));
            } catch (Throwable e) {
                return Option.nothing();
            }
        };
    }

    default CheckedFunc1<A, CheckedFunc1<B, CheckedFunc1<C, CheckedFunc1<D, R>>>> curried() {
        return w -> x -> y -> z -> apply(w, x, y, z);
    }

    default CheckedFunc1<Tuple4<A, B, C, D>, R> tupled() {
        return x -> apply(x.$1(), x.$2(), x.$3(), x.$4());
    }
}
