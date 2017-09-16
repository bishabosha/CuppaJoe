/*
 * Copyright (c) 2017. Jamie Thompson <bishbashboshjt@gmail.com>
 */

package com.bishabosha.caffeine.functional.functions;

import com.bishabosha.caffeine.functional.control.Option;
import com.bishabosha.caffeine.functional.control.Try;
import com.bishabosha.caffeine.functional.tuples.Product1;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

public interface CheckedFunc1<A, R> {

    @Contract(pure = true)
    static <X,R> CheckedFunc1<X,R> of(CheckedFunc1<X, R> reference) {
        return reference;
    }

    @Contract(pure = true)
    static <X,R> CheckedFunc1<X,R> narrow(CheckedFunc1<? super X, ? extends R> func) {
        return func::apply;
    }

    @NotNull
    @Contract(pure = true)
    static <X, R> Func1<X, Option<R>> lift(CheckedFunc1<? super X, ? extends R> func) {
        return x -> Try.<R>narrow(Try.of(() -> func.apply(x))).get();
    }

    @Contract(pure = true)
    default CheckedFunc1<Product1<A>, R> tupled() {
        return x -> apply(x.$1());
    }

    R apply(A a) throws Throwable;
}
