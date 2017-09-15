/*
 * Copyright (c) 2017. Jamie Thompson <bishbashboshjt@gmail.com>
 */

package com.bishabosha.caffeine.functional.functions;

import com.bishabosha.caffeine.functional.control.Option;
import com.bishabosha.caffeine.functional.tuples.Tuple1;
import org.jetbrains.annotations.Contract;

public interface Func1<A, R> {

    R apply(A a);

    @Contract(pure = true)
    static <X,R> Func1<X,R> of(Func1<X,R> func) {
        return func;
    }

    @Contract(pure = true)
    default Func1<A, Option<R>> lifted() {
        return CheckedFunc1.of(this::apply).lifted();
    }

    @Contract(pure = true)
    default Func1<Tuple1<A>, R> tupled() {
        return x -> apply(x.$1());
    }
}
