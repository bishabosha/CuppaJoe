/*
 * Copyright (c) 2017. Jamie Thompson <bishbashboshjt@gmail.com>
 */

package com.bishabosha.caffeine.functional.functions;

import com.bishabosha.caffeine.functional.control.Option;
import com.bishabosha.caffeine.functional.tuples.Tuple2;
import org.jetbrains.annotations.Contract;

public interface CheckedFunc2<A, B, R> {
    R apply(A a, B b) throws Throwable;

    @Contract(pure = true)
    static <X,Y,R> CheckedFunc2<X,Y,R> of(CheckedFunc2<X, Y, R> func) {
        return func;
    }

    @Contract(pure = true)
    default Func2<A, B, Option<R>> lifted() {
        return (x, y) -> {
            try {
                return Option.ofUnknown(apply(x, y));
            } catch (Throwable e) {
                return Option.nothing();
            }
        };
    }

    @Contract(pure = true)
    default CheckedFunc1<A, CheckedFunc1<B, R>> curried() {
        return x -> y -> apply(x, y);
    }

    @Contract(pure = true)
    default CheckedFunc1<Tuple2<A, B>, R> tupled() {
        return x -> apply(x.$1(), x.$2());
    }
}
