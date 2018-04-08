/*
 * Copyright (c) 2017. Jamie Thompson <bishbashboshjt@gmail.com>
 */

package com.github.bishabosha.cuppajoe.functions;

import com.github.bishabosha.cuppajoe.higher.value.Value1.Value;

import java.util.Objects;

@FunctionalInterface
public interface CheckedFunc7<A, B, C, D, E, F, G, R> {

    static <T, U, V, W, X, Y, Z, R> CheckedFunc7<T, U, V, W, X, Y, Z, R> of(CheckedFunc7<T, U, V, W, X, Y, Z, R> reference) {
        return Objects.requireNonNull(reference, "reference");
    }

    static <T, U, V, W, X, Y, Z, R> CheckedFunc7<T, U, V, W, X, Y, Z, R> narrow(CheckedFunc7<? super T, ? super U, ? super V, ? super W, ? super X, ? super Y, ? super Z, ? extends R> func) {
        Objects.requireNonNull(func, "func");
        return func::apply;
    }

    static <T, U, V, W, X, Y, Z, R> Func7<T, U, V, W, X, Y, Z, Value<R>> lift(CheckedFunc7<? super T, ? super U, ? super V, ? super W, ? super X, ? super Y, ? super Z, ? extends R> func) {
        Objects.requireNonNull(func, "func");
        return (t, u, v, w, x, y, z) -> LiftOps.liftFunction(() -> func.apply(t, u, v, w, x, y, z));
    }

    default CheckedFunc1<A, CheckedFunc1<B, CheckedFunc1<C, CheckedFunc1<D, CheckedFunc1<E, CheckedFunc1<F, CheckedFunc1<G, R>>>>>>> curried() {
        return t -> u -> v -> w -> x -> y -> z -> apply(t, u, v, w, x, y, z);
    }

    R apply(A a, B b, C c, D d, E e, F f, G g) throws Exception;

    default CheckedFunc6<B, C, D, E, F, G, R> apply(A a) {
        return (b, c, d, e, f, g) -> apply(a, b, c, d, e, f, g);
    }

    default CheckedFunc5<C, D, E, F, G, R> apply(A a, B b) {
        return (c, d, e, f, g) -> apply(a, b, c, d, e, f, g);
    }

    default CheckedFunc4<D, E, F, G, R> apply(A a, B b, C c) {
        return (d, e, f, g) -> apply(a, b, c, d, e, f, g);
    }

    default CheckedFunc3<E, F, G, R> apply(A a, B b, C c, D d) {
        return (e, f, g) -> apply(a, b, c, d, e, f, g);
    }

    default CheckedFunc2<F, G, R> apply(A a, B b, C c, D d, E e) {
        return (f, g) -> apply(a, b, c, d, e, f, g);
    }

    default CheckedFunc1<G, R> apply(A a, B b, C c, D d, E e, F f) {
        return g -> apply(a, b, c, d, e, f, g);
    }
}
