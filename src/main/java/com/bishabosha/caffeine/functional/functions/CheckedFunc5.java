/*
 * Copyright (c) 2017. Jamie Thompson <bishbashboshjt@gmail.com>
 */

package com.bishabosha.caffeine.functional.functions;

import com.bishabosha.caffeine.functional.Option;
import com.bishabosha.caffeine.functional.tuples.Tuple5;

public interface CheckedFunc5<A, B, C, D, E, R> {
    R apply(A a, B b, C c, D d, E e) throws Throwable;

    static <V,W,X,Y,Z,R> CheckedFunc5<V,W,X,Y,Z,R> of(CheckedFunc5<V, W, X, Y, Z, R> func) {
        return func;
    }

    default Func5<A, B, C, D, E, Option<R>> lifted() {
        return (v, w, x, y, z) -> {
            try {
                return Option.ofUnknown(apply(v, w, x, y, z));
            } catch (Throwable e) {
                return Option.nothing();
            }
        };
    }

    default CheckedFunc1<A, CheckedFunc1<B, CheckedFunc1<C, CheckedFunc1<D, CheckedFunc1<E, R>>>>> curried() {
        return v -> w -> x -> y -> z -> apply(v, w, x, y, z);
    }

    default CheckedFunc1<Tuple5<A, B, C, D, E>, R> tupled() {
        return x -> apply(x.$1(), x.$2(), x.$3(), x.$4(), x.$5());
    }
}
