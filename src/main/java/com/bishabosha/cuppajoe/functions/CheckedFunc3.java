/*
 * Copyright (c) 2017. Jamie Thompson <bishbashboshjt@gmail.com>
 */

package com.bishabosha.cuppajoe.functions;

import com.bishabosha.cuppajoe.control.Option;
import com.bishabosha.cuppajoe.control.Try;
import com.bishabosha.cuppajoe.tuples.Product3;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

public interface CheckedFunc3<A, B, C, R> {

    @Contract(pure = true)
    static <X,Y,Z,R> CheckedFunc3<X,Y,Z,R> of(CheckedFunc3<X, Y, Z, R> reference) {
        return reference;
    }

    @Contract(pure = true)
    static <X,Y,Z,R> CheckedFunc3<X,Y,Z,R> narrow(CheckedFunc3<? super X, ? super Y, ? super Z, ? extends R> func) {
        return func::apply;
    }

    @NotNull
    @Contract(pure = true)
    static <X, Y, Z, R> Func3<X, Y, Z, Option<R>> lift(CheckedFunc3<? super X, ? super Y, ? super Z, ? extends R> func) {
        return (x, y, z) -> Try.<R>of(() -> func.apply(x, y, z)).lift();
    }

    @Contract(pure = true)
    default CheckedFunc1<A, CheckedFunc1<B, CheckedFunc1<C, R>>> curried() {
        return x -> y -> z -> apply(x, y, z);
    }

    @Contract(pure = true)
    default CheckedFunc1<Product3<A, B, C>, R> tupled() {
        return x -> apply(x.$1(), x.$2(), x.$3());
    }

    R apply(A a, B b, C c) throws Throwable;

    default CheckedFunc2<B, C, R> apply(A a) {
        return (b, c) -> apply(a, b, c);
    }

    default CheckedFunc1<C, R> apply(A a, B b) {
        return c -> apply(a, b, c);
    }
}
