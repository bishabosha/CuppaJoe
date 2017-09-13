/*
 * Copyright (c) 2017. Jamie Thompson <bishbashboshjt@gmail.com>
 */

package com.bishabosha.caffeine.functional.functions;

import com.bishabosha.caffeine.functional.Option;
import com.bishabosha.caffeine.functional.tuples.Tuple7;

public interface CheckedFunc7<A, B, C, D, E, F, G, R> {
    R apply(A a, B b, C c, D d, E e, F f, G g) throws Throwable;

    static <T,U,V,W,X,Y,Z,R> CheckedFunc7<T,U,V,W,X,Y,Z,R> of(CheckedFunc7<T, U, V, W, X, Y, Z, R> func) {
        return func;
    }

    default Func7<A, B, C, D, E, F, G, Option<R>> lifted() {
        return (t, u, v, w, x, y, z) -> {
            try {
                return Option.ofUnknown(apply(t, u, v, w, x, y, z));
            } catch (Throwable e) {
                return Option.nothing();
            }
        };
    }

    default CheckedFunc1<A, CheckedFunc1<B, CheckedFunc1<C, CheckedFunc1<D, CheckedFunc1<E, CheckedFunc1<F, CheckedFunc1<G, R>>>>>>> curried() {
        return t -> u -> v -> w -> x -> y -> z -> apply(t, u, v, w, x, y, z);
    }

    default CheckedFunc1<Tuple7<A, B, C, D, E, F, G>, R> tupled() {
        return x -> apply(x.$1(), x.$2(), x.$3(), x.$4(), x.$5(), x.$6(), x.$7());
    }
}
