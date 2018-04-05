/*
 * Copyright (c) 2017. Jamie Thompson <bishbashboshjt@gmail.com>
 */

package io.cuppajoe.functions;

import io.cuppajoe.control.Try;
import io.cuppajoe.tuples.Tuple2;

@FunctionalInterface
public interface CheckedFunc2<A, B, R> {

    static <X, Y, R> CheckedFunc2<X, Y, R> of(CheckedFunc2<X, Y, R> reference) {
        return reference;
    }

    static <X, Y, R> CheckedFunc2<X, Y, R> narrow(CheckedFunc2<? super X, ? super Y, ? extends R> func) {
        return func::apply;
    }

    static <X, Y, R> Func2<X, Y, Try<R>> lift(CheckedFunc2<? super X, ? super Y, ? extends R> func) {
        return (x, y) -> Try.of(() -> func.apply(x, y));
    }

    default CheckedFunc1<A, CheckedFunc1<B, R>> curried() {
        return x -> y -> apply(x, y);
    }

    default CheckedFunc1<Tuple2<A, B>, R> tupled() {
        return x -> apply(x.$1, x.$2);
    }

    R apply(A a, B b) throws Exception;

    default CheckedFunc1<B, R> apply(A a) {
        return b -> apply(a, b);
    }
}
