/*
 * Copyright (c) 2017. Jamie Thompson <bishbashboshjt@gmail.com>
 */

package com.bishabosha.caffeine.functional.functions;

import com.bishabosha.caffeine.functional.control.Option;
import com.bishabosha.caffeine.functional.tuples.Tuple3;
import org.jetbrains.annotations.Contract;

public interface Func3<A, B, C, R> {
    R apply(A a, B b, C c);

    @Contract(pure = true)
    static <X,Y,Z,R> Func3<X,Y,Z,R> of(Func3<X,Y,Z,R> func) {
        return func;
    }

    @Contract(pure = true)
    default Func3<A, B, C, Option<R>> lifted() {
        return CheckedFunc3.of(this::apply).lifted();
    }

    @Contract(pure = true)
    default Func1<A, Func1<B, Func1<C, R>>> curried() {
        return x -> y -> z -> apply(x, y, z);
    }

    @Contract(pure = true)
    default Func1<Tuple3<A, B, C>, R> tupled() {
        return x -> apply(x.$1(), x.$2(), x.$3());
    }
}
