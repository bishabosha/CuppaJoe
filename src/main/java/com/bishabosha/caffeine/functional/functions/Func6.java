/*
 * Copyright (c) 2017. Jamie Thompson <bishbashboshjt@gmail.com>
 */

package com.bishabosha.caffeine.functional.functions;

import com.bishabosha.caffeine.functional.control.Option;
import com.bishabosha.caffeine.functional.tuples.Tuple6;
import org.jetbrains.annotations.Contract;

public interface Func6<A, B, C, D, E, F, R> {
    R apply(A a, B b, C c, D d, E e, F f);

    @Contract(pure = true)
    static <U,V,W,X,Y,Z,R> Func6<U,V,W,X,Y,Z,R> of(Func6<U,V,W,X,Y,Z,R> func) {
        return func;
    }

    @Contract(pure = true)
    default Func6<A, B, C, D, E, F, Option<R>> lifted() {
        return CheckedFunc6.of(this::apply).lifted();
    }

    @Contract(pure = true)
    default Func1<A, Func1<B, Func1<C, Func1<D, Func1<E, Func1<F, R>>>>>> curried() {
        return u -> v -> w -> x -> y -> z -> apply(u, v, w, x, y, z);
    }

    @Contract(pure = true)
    default Func1<Tuple6<A, B, C, D, E, F>, R> tupled() {
        return x -> apply(x.$1(), x.$2(), x.$3(), x.$4(), x.$5(), x.$6());
    }
}
