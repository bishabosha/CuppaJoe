/*
 * Copyright (c) 2017. Jamie Thompson <bishbashboshjt@gmail.com>
 */

package com.github.bishabosha.cuppajoe.higher.functions;

import com.github.bishabosha.cuppajoe.annotation.NonNull;
import com.github.bishabosha.cuppajoe.higher.value.Value1.Value;

import java.util.Objects;
import java.util.function.Supplier;

@FunctionalInterface
public interface CheckedConsume7<A, B, C, D, E, F, G> {

    void apply(A a, B b, C c, D d, E e, F f, G g) throws Exception;

    static <T, U, V, W, X, Y, Z> CheckedConsume7<T, U, V, W, X, Y, Z> of(@NonNull CheckedConsume7<T, U, V, W, X, Y, Z> reference) {
        return Objects.requireNonNull(reference, "reference");
    }

    static <T, U, V, W, X, Y, Z> CheckedConsume7<T, U, V, W, X, Y, Z> narrow(@NonNull CheckedConsume7<? super T, ? super U, ? super V, ? super W, ? super X, ? super Y, ? super Z> func) {
        Objects.requireNonNull(func, "func");
        return func::apply;
    }

    static <T, U, V, W, X, Y, Z> Func7<T, U, V, W, X, Y, Z, Value<Void>> lift(@NonNull CheckedConsume7<? super T, ? super U, ? super V, ? super W, ? super X, ? super Y, ? super Z> func) {
        Objects.requireNonNull(func, "func");
        return (t, u, v, w, x, y, z) -> LiftOps.liftConsumer(() -> func.apply(t, u, v, w, x, y, z));
    }

    default Func1<A, Func1<B, Func1<C, Func1<D, Func1<E, Func1<F, CheckedConsume1<G>>>>>>> curried() {
        return t -> u -> v -> w -> x -> y -> z -> apply(t, u, v, w, x, y, z);
    }

    default CheckedConsume7<Supplier<A>, Supplier<B>, Supplier<C>, Supplier<D>, Supplier<E>, Supplier<F>, Supplier<G>> lazyInput() {
        return (a, b, c, d, e, f, g) -> apply(a.get(), b.get(), c.get(), d.get(), e.get(), f.get(), g.get());
    }

    default CheckedConsume1<G> apply(A a, B b, C c, D d, E e, F f) {
        return g -> apply(a, b, c, d, e, f, g);
    }

    default CheckedConsume2<F, G> apply(A a, B b, C c, D d, E e) {
        return (f, g) -> apply(a, b, c, d, e, f, g);
    }

    default CheckedConsume3<E, F, G> apply(A a, B b, C c, D d) {
        return (e, f, g) -> apply(a, b, c, d, e, f, g);
    }

    default CheckedConsume4<D, E, F, G> apply(A a, B b, C c) {
        return (d, e, f, g) -> apply(a, b, c, d, e, f, g);
    }

    default CheckedConsume5<C, D, E, F, G> apply(A a, B b) {
        return (c, d, e, f, g) -> apply(a, b, c, d, e, f, g);
    }

    default CheckedConsume6<B, C, D, E, F, G> apply(A a) {
        return (b, c, d, e, f, g) -> apply(a, b, c, d, e, f, g);
    }
}
