/*
 * Copyright (c) 2017. Jamie Thompson <bishbashboshjt@gmail.com>
 */

package com.bishabosha.caffeine.functional.functions;

import com.bishabosha.caffeine.functional.Option;
import com.bishabosha.caffeine.functional.tuples.Tuple7;

public interface Func7<A, B, C, D, E, F, G, R> {
    R apply(A a, B b, C c, D d, E e, F f, G g);

    static <T,U,V,W,X,Y,Z,R> Func7<T,U,V,W,X,Y,Z,R> of(Func7<T,U,V,W,X,Y,Z,R> func) {
        return func;
    }

    default Func7<A, B, C, D, E, F, G, Option<R>> lifted() {
        return CheckedFunc7.of(this::apply).lifted();
    }

    default Func1<A, Func1<B, Func1<C, Func1<D, Func1<E, Func1<F, Func1<G, R>>>>>>> curried() {
        return t -> u -> v -> w -> x -> y -> z -> apply(t, u, v, w, x, y, z);
    }

    default Func1<Tuple7<A, B, C, D, E, F, G>, R> tupled() {
        return x -> apply(x.$1(), x.$2(), x.$3(), x.$4(), x.$5(), x.$6(), x.$7());
    }
}
