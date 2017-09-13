/*
 * Copyright (c) 2017. Jamie Thompson <bishbashboshjt@gmail.com>
 */
package com.bishabosha.caffeine.functional.functions;

import com.bishabosha.caffeine.functional.control.Option;
import com.bishabosha.caffeine.functional.tuples.Tuple4;
import org.jetbrains.annotations.Contract;

public interface Func4<A, B, C, D, R> {
    R apply(A a, B b, C c, D d);

    @Contract(pure = true)
    static <W,X,Y,Z,R> Func4<W,X,Y,Z,R> of(Func4<W,X,Y,Z,R> func) {
        return func;
    }

    @Contract(pure = true)
    default Func4<A, B, C, D, Option<R>> lifted() {
        return CheckedFunc4.of(this::apply).lifted();
    }

    @Contract(pure = true)
    default Func1<A, Func1<B, Func1<C, Func1<D, R>>>> curried() {
        return w -> x -> y -> z -> apply(w, x, y, z);
    }

    @Contract(pure = true)
    default Func1<Tuple4<A, B, C, D>, R> tupled() {
        return x -> apply(x.$1(), x.$2(), x.$3(), x.$4());
    }
}
