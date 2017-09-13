/*
 * Copyright (c) 2017. Jamie Thompson <bishbashboshjt@gmail.com>
 */

package com.bishabosha.caffeine.functional.functions;

import com.bishabosha.caffeine.functional.Option;
import com.bishabosha.caffeine.functional.tuples.Tuple1;

public interface Func1<A, R> {
    R apply(A a);

    static <X,R> Func1<X,R> of(Func1<X,R> func) {
        return func;
    }

    default Func1<A, Option<R>> lifted() {
        return CheckedFunc1.of(this::apply).lifted();
    }

    default Func1<Tuple1<A>, R> tupled() {
        return x -> apply(x.$1());
    }
}
