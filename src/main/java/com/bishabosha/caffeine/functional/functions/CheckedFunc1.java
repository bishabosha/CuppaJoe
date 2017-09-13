/*
 * Copyright (c) 2017. Jamie Thompson <bishbashboshjt@gmail.com>
 */

package com.bishabosha.caffeine.functional.functions;

import com.bishabosha.caffeine.functional.Option;
import com.bishabosha.caffeine.functional.tuples.Tuple1;

public interface CheckedFunc1<A, R> {
    R apply(A a) throws Throwable;

    static <X,R> CheckedFunc1<X,R> of(CheckedFunc1<X, R> func) {
        return func;
    }

    default Func1<A, Option<R>> lifted() {
        return x -> {
            try {
                return Option.ofUnknown(apply(x));
            } catch (Throwable e) {
                return Option.nothing();
            }
        };
    }

    default CheckedFunc1<Tuple1<A>, R> tupled() {
        return x -> apply(x.$1());
    }
}
