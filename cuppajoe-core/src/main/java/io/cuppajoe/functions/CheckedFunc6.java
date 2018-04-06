/*
 * Copyright (c) 2017. Jamie Thompson <bishbashboshjt@gmail.com>
 */

package io.cuppajoe.functions;

import io.cuppajoe.annotation.NonNull;
import io.cuppajoe.control.Try;
import io.cuppajoe.tuples.Tuple6;

import java.util.Objects;

@FunctionalInterface
public interface CheckedFunc6<A, B, C, D, E, F, R> {

    static <U, V, W, X, Y, Z, R> CheckedFunc6<U, V, W, X, Y, Z, R> of(@NonNull CheckedFunc6<U, V, W, X, Y, Z, R> reference) {
        return Objects.requireNonNull(reference);
    }

    static <T, U, V, W, X, Y, R> CheckedFunc6<T, U, V, W, X, Y, R> narrow(CheckedFunc6<? super T, ? super U, ? super V, ? super W, ? super X, ? super Y, ? extends R> func) {
        Objects.requireNonNull(func);
        return func::apply;
    }

    static <U, V, W, X, Y, Z, R> Func6<U, V, W, X, Y, Z, Try<R>> lift(@NonNull CheckedFunc6<? super U, ? super V, ? super W, ? super X, ? super Y, ? super Z, ? extends R> func) {
        Objects.requireNonNull(func);
        return (u, v, w, x, y, z) -> Try.of(() -> func.apply(u, v, w, x, y, z));
    }

    default CheckedFunc1<A, CheckedFunc1<B, CheckedFunc1<C, CheckedFunc1<D, CheckedFunc1<E, CheckedFunc1<F, R>>>>>> curried() {
        return u -> v -> w -> x -> y -> z -> apply(u, v, w, x, y, z);
    }

    default CheckedFunc1<Tuple6<A, B, C, D, E, F>, R> tupled() {
        return x -> apply(x.$1, x.$2, x.$3, x.$4, x.$5, x.$6);
    }

    R apply(A a, B b, C c, D d, E e, F f) throws Exception;

    default CheckedFunc5<B, C, D, E, F, R> apply(A a) {
        return (b, c, d, e, f) -> apply(a, b, c, d, e, f);
    }

    default CheckedFunc4<C, D, E, F, R> apply(A a, B b) {
        return (c, d, e, f) -> apply(a, b, c, d, e, f);
    }

    default CheckedFunc3<D, E, F, R> apply(A a, B b, C c) {
        return (d, e, f) -> apply(a, b, c, d, e, f);
    }

    default CheckedFunc2<E, F, R> apply(A a, B b, C c, D d) {
        return (e, f) -> apply(a, b, c, d, e, f);
    }

    default CheckedFunc1<F, R> apply(A a, B b, C c, D d, E e) {
        return f -> apply(a, b, c, d, e, f);
    }
}
