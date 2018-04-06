/*
 * Copyright (c) 2017. Jamie Thompson <bishbashboshjt@gmail.com>
 */
package io.cuppajoe.functions;

import io.cuppajoe.annotation.NonNull;
import io.cuppajoe.control.Try;
import io.cuppajoe.tuples.Tuple4;

import java.util.Objects;

@FunctionalInterface
public interface CheckedFunc4<A, B, C, D, R> {

    static <W, X, Y, Z, R> CheckedFunc4<W, X, Y, Z, R> of(@NonNull CheckedFunc4<W, X, Y, Z, R> reference) {
        return Objects.requireNonNull(reference);
    }

    static <W, X, Y, Z, R> CheckedFunc4<W, X, Y, Z, R> narrow(@NonNull CheckedFunc4<? super W, ? super X, ? super Y, ? super Z, ? extends R> func) {
        Objects.requireNonNull(func);
        return func::apply;
    }

    static <W, X, Y, Z, R> Func4<W, X, Y, Z, Try<R>> lift(@NonNull CheckedFunc4<? super W, ? super X, ? super Y, ? super Z, ? extends R> func) {
        Objects.requireNonNull(func);
        return (w, x, y, z) -> Try.of(() -> func.apply(w, x, y, z));
    }

    default CheckedFunc1<A, CheckedFunc1<B, CheckedFunc1<C, CheckedFunc1<D, R>>>> curried() {
        return w -> x -> y -> z -> apply(w, x, y, z);
    }

    default CheckedFunc1<Tuple4<A, B, C, D>, R> tupled() {
        return x -> apply(x.$1, x.$2, x.$3, x.$4);
    }

    R apply(A a, B b, C c, D d) throws Exception;

    default CheckedFunc3<B, C, D, R> apply(A a) {
        return (b, c, d) -> apply(a, b, c, d);
    }

    default CheckedFunc2<C, D, R> apply(A a, B b) {
        return (c, d) -> apply(a, b, c, d);
    }

    default CheckedFunc1<D, R> apply(A a, B b, C c) {
        return d -> apply(a, b, c, d);
    }
}
