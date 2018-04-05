/*
 * Copyright (c) 2017. Jamie Thompson <bishbashboshjt@gmail.com>
 */

package io.cuppajoe.functions;

import io.cuppajoe.control.Try;
import io.cuppajoe.tuples.Tuple1;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

@FunctionalInterface
public interface CheckedFunc1<A, R> {

    @Contract(pure = true)
    static <X, R> CheckedFunc1<X, R> of(CheckedFunc1<X, R> reference) {
        return reference;
    }

    @Contract(pure = true)
    static <X, R> CheckedFunc1<X, R> narrow(CheckedFunc1<? super X, ? extends R> func) {
        return func::apply;
    }

    @NotNull
    @Contract(pure = true)
    static <X, R> Func1<X, Try<R>> lift(CheckedFunc1<? super X, ? extends R> func) {
        return x -> Try.of(() -> func.apply(x));
    }

    @Contract(pure = true)
    default CheckedFunc1<Tuple1<A>, R> tupled() {
        return x -> apply(x.$1);
    }

    R apply(A a) throws Exception;
}
