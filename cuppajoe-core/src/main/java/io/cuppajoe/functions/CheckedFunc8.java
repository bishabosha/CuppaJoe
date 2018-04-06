/*
 * Copyright (c) 2017. Jamie Thompson <bishbashboshjt@gmail.com>
 */

package io.cuppajoe.functions;

import io.cuppajoe.annotation.NonNull;
import io.cuppajoe.control.Try;
import io.cuppajoe.tuples.Tuple8;

import java.util.Objects;

@FunctionalInterface
public interface CheckedFunc8<A, B, C, D, E, F, G, H, R> {

    static <S, T, U, V, W, X, Y, Z, R> CheckedFunc8<S, T, U, V, W, X, Y, Z, R> of(@NonNull CheckedFunc8<S, T, U, V, W, X, Y, Z, R> reference) {
        return Objects.requireNonNull(reference);
    }

    static <S, T, U, V, W, X, Y, Z, R> CheckedFunc8<S, T, U, V, W, X, Y, Z, R> narrow(@NonNull CheckedFunc8<? super S, ? super T, ? super U, ? super V, ? super W, ? super X, ? super Y, ? super Z, ? extends R> func) {
        Objects.requireNonNull(func);
        return func::apply;
    }

    static <S, T, U, V, W, X, Y, Z, R> Func8<S, T, U, V, W, X, Y, Z, Try<R>> lift(@NonNull CheckedFunc8<? super S, ? super T, ? super U, ? super V, ? super W, ? super X, ? super Y, ? super Z, ? extends R> func) {
        Objects.requireNonNull(func);
        return (s, t, u, v, w, x, y, z) -> Try.of(() -> func.apply(s, t, u, v, w, x, y, z));
    }

    default CheckedFunc1<A, CheckedFunc1<B, CheckedFunc1<C, CheckedFunc1<D, CheckedFunc1<E, CheckedFunc1<F, CheckedFunc1<G, CheckedFunc1<H, R>>>>>>>> curried() {
        return s -> t -> u -> v -> w -> x -> y -> z -> apply(s, t, u, v, w, x, y, z);
    }

    default CheckedFunc1<Tuple8<A, B, C, D, E, F, G, H>, R> tupled() {
        return x -> apply(x.$1, x.$2, x.$3, x.$4, x.$5, x.$6, x.$7, x.$8);
    }

    R apply(A a, B b, C c, D d, E e, F f, G g, H h) throws Exception;

    default CheckedFunc7<B, C, D, E, F, G, H, R> apply(A a) {
        return (b, c, d, e, f, g, h) -> apply(a, b, c, d, e, f, g, h);
    }

    default CheckedFunc6<C, D, E, F, G, H, R> apply(A a, B b) {
        return (c, d, e, f, g, h) -> apply(a, b, c, d, e, f, g, h);
    }

    default CheckedFunc5<D, E, F, G, H, R> apply(A a, B b, C c) {
        return (d, e, f, g, h) -> apply(a, b, c, d, e, f, g, h);
    }

    default CheckedFunc4<E, F, G, H, R> apply(A a, B b, C c, D d) {
        return (e, f, g, h) -> apply(a, b, c, d, e, f, g, h);
    }

    default CheckedFunc3<F, G, H, R> apply(A a, B b, C c, D d, E e) {
        return (f, g, h) -> apply(a, b, c, d, e, f, g, h);
    }

    default CheckedFunc2<G, H, R> apply(A a, B b, C c, D d, E e, F f) {
        return (g, h) -> apply(a, b, c, d, e, f, g, h);
    }

    default CheckedFunc1<H, R> apply(A a, B b, C c, D d, E e, F f, G g) {
        return h -> apply(a, b, c, d, e, f, g, h);
    }
}
