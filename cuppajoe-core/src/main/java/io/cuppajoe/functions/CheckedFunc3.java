/*
 * Copyright (c) 2017. Jamie Thompson <bishbashboshjt@gmail.com>
 */

package io.cuppajoe.functions;

import io.cuppajoe.control.Try;
import io.cuppajoe.tuples.Tuple3;

@FunctionalInterface
public interface CheckedFunc3<A, B, C, R> {

    static <X, Y, Z, R> CheckedFunc3<X, Y, Z, R> of(CheckedFunc3<X, Y, Z, R> reference) {
        return reference;
    }

    static <X, Y, Z, R> CheckedFunc3<X, Y, Z, R> narrow(CheckedFunc3<? super X, ? super Y, ? super Z, ? extends R> func) {
        return func::apply;
    }

    static <X, Y, Z, R> Func3<X, Y, Z, Try<R>> lift(CheckedFunc3<? super X, ? super Y, ? super Z, ? extends R> func) {
        return (x, y, z) -> Try.of(() -> func.apply(x, y, z));
    }

    default CheckedFunc1<A, CheckedFunc1<B, CheckedFunc1<C, R>>> curried() {
        return x -> y -> z -> apply(x, y, z);
    }

    default CheckedFunc1<Tuple3<A, B, C>, R> tupled() {
        return x -> apply(x.$1, x.$2, x.$3);
    }

    R apply(A a, B b, C c) throws Exception;

    default CheckedFunc2<B, C, R> apply(A a) {
        return (b, c) -> apply(a, b, c);
    }

    default CheckedFunc1<C, R> apply(A a, B b) {
        return c -> apply(a, b, c);
    }
}
