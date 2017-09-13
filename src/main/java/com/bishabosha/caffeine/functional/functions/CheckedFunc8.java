/*
 * Copyright (c) 2017. Jamie Thompson <bishbashboshjt@gmail.com>
 */

package com.bishabosha.caffeine.functional.functions;

import com.bishabosha.caffeine.functional.Option;
import com.bishabosha.caffeine.functional.tuples.Tuple8;

public interface CheckedFunc8<A, B, C, D, E, F, G, H, R> {
    R apply(A a, B b, C c, D d, E e, F f, G g, H h) throws Throwable;

    static <S,T,U,V,W,X,Y,Z,R> CheckedFunc8<S,T,U,V,W,X,Y,Z,R> of(CheckedFunc8<S, T, U, V, W, X, Y, Z, R> func) {
        return func;
    }

    default Func8<A, B, C, D, E, F, G, H, Option<R>> lifted() {
        return (s, t, u, v, w, x, y, z) -> {
            try {
                return Option.ofUnknown(apply(s, t, u, v, w, x, y, z));
            } catch (Throwable e) {
                return Option.nothing();
            }
        };
    }

    default CheckedFunc1<A, CheckedFunc1<B, CheckedFunc1<C, CheckedFunc1<D, CheckedFunc1<E, CheckedFunc1<F, CheckedFunc1<G, CheckedFunc1<H, R>>>>>>>> curried() {
        return s -> t -> u -> v -> w -> x -> y -> z -> apply(s, t, u, v, w, x, y, z);
    }

    default CheckedFunc1<Tuple8<A, B, C, D, E, F, G, H>, R> tupled() {
        return x -> apply(x.$1(), x.$2(), x.$3(), x.$4(), x.$5(), x.$6(), x.$7(), x.$8());
    }
}
