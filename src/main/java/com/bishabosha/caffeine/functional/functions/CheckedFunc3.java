/*
 * Copyright (c) 2017. Jamie Thompson <bishbashboshjt@gmail.com>
 */

package com.bishabosha.caffeine.functional.functions;

import com.bishabosha.caffeine.functional.API;
import com.bishabosha.caffeine.functional.control.Option;
import com.bishabosha.caffeine.functional.tuples.Tuple3;
import org.jetbrains.annotations.Contract;

import static com.bishabosha.caffeine.functional.API.Nothing;
import static com.bishabosha.caffeine.functional.API.Option;

public interface CheckedFunc3<A, B, C, R> {
    R apply(A a, B b, C c) throws Throwable;

    @Contract(pure = true)
    static <X,Y,Z,R> CheckedFunc3<X,Y,Z,R> of(CheckedFunc3<X, Y, Z, R> func) {
        return func;
    }

    @Contract(pure = true)
    default Func3<A, B, C, Option<R>> lifted() {
        return (x, y, z) -> {
            try {
                return Option(apply(x, y, z));
            } catch (Throwable e) {
                return Nothing();
            }
        };
    }

    @Contract(pure = true)
    default CheckedFunc1<A, CheckedFunc1<B, CheckedFunc1<C, R>>> curried() {
        return x -> y -> z -> apply(x, y, z);
    }

    @Contract(pure = true)
    default CheckedFunc1<Tuple3<A, B, C>, R> tupled() {
        return x -> apply(x.$1(), x.$2(), x.$3());
    }
}
