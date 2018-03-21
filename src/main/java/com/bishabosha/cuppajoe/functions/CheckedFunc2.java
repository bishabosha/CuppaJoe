/*
 * Copyright (c) 2017. Jamie Thompson <bishbashboshjt@gmail.com>
 */

package com.bishabosha.cuppajoe.functions;

import com.bishabosha.cuppajoe.control.Try;
import com.bishabosha.cuppajoe.tuples.Product2;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

@FunctionalInterface
public interface CheckedFunc2<A, B, R> {

    @Contract(pure = true)
    static <X,Y,R> CheckedFunc2<X,Y,R> of(CheckedFunc2<X, Y, R> reference) {
        return reference;
    }

    @Contract(pure = true)
    static <X,Y,R> CheckedFunc2<X,Y,R> narrow(CheckedFunc2<? super X, ? super Y, ? extends R> func) {
        return func::apply;
    }

    @NotNull
    @Contract(pure = true)
    static <X, Y, R> Func2<X, Y, Try<R>> lift(CheckedFunc2<? super X, ? super Y, ? extends R> func) {
        return (x, y) -> Try.of(() -> func.apply(x, y));
    }

    @Contract(pure = true)
    default CheckedFunc1<A, CheckedFunc1<B, R>> curried() {
        return x -> y -> apply(x, y);
    }

    @Contract(pure = true)
    default CheckedFunc1<Product2<A, B>, R> tupled() {
        return x -> apply(x.$1(), x.$2());
    }

    R apply(A a, B b) throws Exception;

    default CheckedFunc1<B, R> apply(A a) {
        return b -> apply(a, b);
    }
}
