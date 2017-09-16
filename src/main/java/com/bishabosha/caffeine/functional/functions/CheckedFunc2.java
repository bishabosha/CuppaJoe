/*
 * Copyright (c) 2017. Jamie Thompson <bishbashboshjt@gmail.com>
 */

package com.bishabosha.caffeine.functional.functions;

import com.bishabosha.caffeine.functional.control.Option;
import com.bishabosha.caffeine.functional.control.Try;
import com.bishabosha.caffeine.functional.tuples.Product2;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

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
    static <X, Y, R> Func2<X, Y, Option<R>> lift(CheckedFunc2<? super X, ? super Y, ? extends R> func) {
        return (x, y) -> Try.<R>narrow(Try.of(() -> func.apply(x, y))).get();
    }

    @Contract(pure = true)
    default CheckedFunc1<A, CheckedFunc1<B, R>> curried() {
        return x -> y -> apply(x, y);
    }

    @Contract(pure = true)
    default CheckedFunc1<Product2<A, B>, R> tupled() {
        return x -> apply(x.$1(), x.$2());
    }

    R apply(A a, B b) throws Throwable;

    default CheckedFunc1<B, R> apply(A a) {
        return b -> apply(a, b);
    }
}
