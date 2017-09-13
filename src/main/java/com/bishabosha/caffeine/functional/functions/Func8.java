/*
 * Copyright (c) 2017. Jamie Thompson <bishbashboshjt@gmail.com>
 */

package com.bishabosha.caffeine.functional.functions;

import com.bishabosha.caffeine.functional.control.Option;
import com.bishabosha.caffeine.functional.tuples.Tuple8;
import org.jetbrains.annotations.Contract;

public interface Func8<A, B, C, D, E, F, G, H, R> {
    R apply(A a, B b, C c, D d, E e, F f, G g, H h);

    @Contract(pure = true)
    static <S,T,U,V,W,X,Y,Z,R> Func8<S,T,U,V,W,X,Y,Z,R> of(Func8<S,T,U,V,W,X,Y,Z,R> func) {
        return func;
    }

    @Contract(pure = true)
    default Func8<A, B, C, D, E, F, G, H, Option<R>> lifted() {
        return CheckedFunc8.of(this::apply).lifted();
    }

    @Contract(pure = true)
    default Func1<A, Func1<B, Func1<C, Func1<D, Func1<E, Func1<F, Func1<G, Func1<H, R>>>>>>>> curried() {
        return s -> t -> u -> v -> w -> x -> y -> z -> apply(s, t, u, v, w, x, y, z);
    }

    @Contract(pure = true)
    default Func1<Tuple8<A, B, C, D, E, F, G, H>, R> tupled() {
        return x -> apply(x.$1(), x.$2(), x.$3(), x.$4(), x.$5(), x.$6(), x.$7(), x.$8());
    }
}
