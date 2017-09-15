/*
 * Copyright (c) 2017. Jamie Thompson <bishbashboshjt@gmail.com>
 */
package com.bishabosha.caffeine.functional.functions;

import com.bishabosha.caffeine.functional.API;
import com.bishabosha.caffeine.functional.control.Option;
import com.bishabosha.caffeine.functional.tuples.Tuple4;
import org.jetbrains.annotations.Contract;

import static com.bishabosha.caffeine.functional.API.Nothing;
import static com.bishabosha.caffeine.functional.API.Option;

public interface CheckedFunc4<A, B, C, D, R> {
    R apply(A a, B b, C c, D d) throws Throwable;

    @Contract(pure = true)
    static <W,X,Y,Z,R> CheckedFunc4<W,X,Y,Z,R> of(CheckedFunc4<W, X, Y, Z, R> func) {
        return func;
    }

    @Contract(pure = true)
    default Func4<A, B, C, D, Option<R>> lifted() {
        return (w, x, y, z) -> {
            try {
                return Option(apply(w, x, y, z));
            } catch (Throwable e) {
                return Nothing();
            }
        };
    }

    @Contract(pure = true)
    default CheckedFunc1<A, CheckedFunc1<B, CheckedFunc1<C, CheckedFunc1<D, R>>>> curried() {
        return w -> x -> y -> z -> apply(w, x, y, z);
    }

    @Contract(pure = true)
    default CheckedFunc1<Tuple4<A, B, C, D>, R> tupled() {
        return x -> apply(x.$1(), x.$2(), x.$3(), x.$4());
    }
}
