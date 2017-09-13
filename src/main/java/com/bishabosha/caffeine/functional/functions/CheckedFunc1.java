/*
 * Copyright (c) 2017. Jamie Thompson <bishbashboshjt@gmail.com>
 */

package com.bishabosha.caffeine.functional.functions;

import com.bishabosha.caffeine.functional.control.Option;
import com.bishabosha.caffeine.functional.tuples.Tuple1;
import org.jetbrains.annotations.Contract;

public interface CheckedFunc1<A, R> {
    R apply(A a) throws Throwable;

    @Contract(pure = true)
    static <X,R> CheckedFunc1<X,R> of(CheckedFunc1<X, R> func) {
        return func;
    }

    @Contract(pure = true)
    default Func1<A, Option<R>> lifted() {
        return x -> {
            try {
                return Option.ofUnknown(apply(x));
            } catch (Throwable e) {
                return Option.nothing();
            }
        };
    }

    @Contract(pure = true)
    default CheckedFunc1<Tuple1<A>, R> tupled() {
        return x -> apply(x.$1());
    }
}
