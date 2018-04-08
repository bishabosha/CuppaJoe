/*
 * Copyright (c) 2017. Jamie Thompson <bishbashboshjt@gmail.com>
 */

package com.github.bishabosha.cuppajoe.higher.functions;

import com.github.bishabosha.cuppajoe.annotation.NonNull;
import com.github.bishabosha.cuppajoe.higher.value.Value1.Value;

import java.util.Objects;

@FunctionalInterface
public interface CheckedFunc8<A, B, C, D, E, F, G, H, R> {

    static <S, T, U, V, W, X, Y, Z, R> CheckedFunc8<S, T, U, V, W, X, Y, Z, R> of(@NonNull CheckedFunc8<S, T, U, V, W, X, Y, Z, R> reference) {
        return Objects.requireNonNull(reference, "reference");
    }

    static <S, T, U, V, W, X, Y, Z, R> CheckedFunc8<S, T, U, V, W, X, Y, Z, R> narrow(@NonNull CheckedFunc8<? super S, ? super T, ? super U, ? super V, ? super W, ? super X, ? super Y, ? super Z, ? extends R> func) {
        Objects.requireNonNull(func, "func");
        return func::apply;
    }

    static <S, T, U, V, W, X, Y, Z, R> Func8<S, T, U, V, W, X, Y, Z, Value<R>> lift(@NonNull CheckedFunc8<? super S, ? super T, ? super U, ? super V, ? super W, ? super X, ? super Y, ? super Z, ? extends R> func) {
        Objects.requireNonNull(func, "func");
        return (s, t, u, v, w, x, y, z) -> LiftOps.liftFunction(() -> func.apply(s, t, u, v, w, x, y, z));
    }

    default CheckedFunc1<A, CheckedFunc1<B, CheckedFunc1<C, CheckedFunc1<D, CheckedFunc1<E, CheckedFunc1<F, CheckedFunc1<G, CheckedFunc1<H, R>>>>>>>> curried() {
        return s -> t -> u -> v -> w -> x -> y -> z -> apply(s, t, u, v, w, x, y, z);
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
