/*
 * Copyright (c) 2017. Jamie Thompson <bishbashboshjt@gmail.com>
 */

package com.bishabosha.caffeine.functional.functions;

import com.bishabosha.caffeine.functional.Option;
import com.bishabosha.caffeine.functional.tuples.Tuple2;

public interface Func2<A, B, R> {
    R apply(A a, B b);

    static <X,Y,R> Func2<X,Y,R> of(Func2<X,Y,R> func) {
        return func;
    }

    default Func2<A, B, Option<R>> lifted() {
        return (x, y) -> {
            try {
                return Option.ofUnknown(apply(x, y));
            } catch (Throwable e) {
                return Option.nothing();
            }
        };
    }

    default Func1<A, Func1<B, R>> curried() {
        return x -> y -> apply(x, y);
    }

    default Func1<Tuple2<A, B>, R> tupled() {
        return x -> apply(x.$1(), x.$2());
    }
}
