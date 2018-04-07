/*
 * Copyright (c) 2017. Jamie Thompson <bishbashboshjt@gmail.com>
 */

package com.github.bishabosha.cuppajoe.functions;

import com.github.bishabosha.cuppajoe.annotation.NonNull;
import com.github.bishabosha.cuppajoe.control.Try;
import com.github.bishabosha.cuppajoe.tuples.Tuple5;

import java.util.Objects;

@FunctionalInterface
public interface CheckedFunc5<A, B, C, D, E, R> {

    static <V, W, X, Y, Z, R> CheckedFunc5<V, W, X, Y, Z, R> of(@NonNull CheckedFunc5<V, W, X, Y, Z, R> reference) {
        return Objects.requireNonNull(reference, "reference");
    }

    static <V, W, X, Y, Z, R> CheckedFunc5<V, W, X, Y, Z, R> narrow(@NonNull CheckedFunc5<? super V, ? super W, ? super X, ? super Y, ? super Z, ? extends R> func) {
        Objects.requireNonNull(func, "func");
        return func::apply;
    }

    static <V, W, X, Y, Z, R> Func5<V, W, X, Y, Z, Try<R>> lift(@NonNull CheckedFunc5<? super V, ? super W, ? super X, ? super Y, ? super Z, ? extends R> func) {
        Objects.requireNonNull(func, "func");
        return (v, w, x, y, z) -> Try.of(() -> func.apply(v, w, x, y, z));
    }

    default CheckedFunc1<A, CheckedFunc1<B, CheckedFunc1<C, CheckedFunc1<D, CheckedFunc1<E, R>>>>> curried() {
        return v -> w -> x -> y -> z -> apply(v, w, x, y, z);
    }

    default CheckedFunc1<Tuple5<A, B, C, D, E>, R> tupled() {
        return x -> apply(x.$1, x.$2, x.$3, x.$4, x.$5);
    }

    R apply(A a, B b, C c, D d, E e) throws Exception;

    default CheckedFunc4<B, C, D, E, R> apply(A a) {
        return (b, c, d, e) -> apply(a, b, c, d, e);
    }

    default CheckedFunc3<C, D, E, R> apply(A a, B b) {
        return (c, d, e) -> apply(a, b, c, d, e);
    }

    default CheckedFunc2<D, E, R> apply(A a, B b, C c) {
        return (d, e) -> apply(a, b, c, d, e);
    }

    default CheckedFunc1<E, R> apply(A a, B b, C c, D d) {
        return e -> apply(a, b, c, d, e);
    }
}
