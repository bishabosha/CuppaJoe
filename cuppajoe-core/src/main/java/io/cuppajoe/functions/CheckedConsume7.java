/*
 * Copyright (c) 2017. Jamie Thompson <bishbashboshjt@gmail.com>
 */

package io.cuppajoe.functions;

import io.cuppajoe.control.Try;
import io.cuppajoe.tuples.Tuple7;
import io.cuppajoe.tuples.Unit;

import java.util.function.Supplier;

@FunctionalInterface
public interface CheckedConsume7<A, B, C, D, E, F, G> {

    void apply(A a, B b, C c, D d, E e, F f, G g) throws Exception;

    static <T, U, V, W, X, Y, Z> CheckedConsume7<T, U, V, W, X, Y, Z> of(CheckedConsume7<T, U, V, W, X, Y, Z> reference) {
        return reference;
    }

    static <T, U, V, W, X, Y, Z> CheckedConsume7<T, U, V, W, X, Y, Z> narrow(CheckedConsume7<? super T, ? super U, ? super V, ? super W, ? super X, ? super Y, ? super Z> func) {
        return func::apply;
    }

    static <T, U, V, W, X, Y, Z> Func7<T, U, V, W, X, Y, Z, Try<Unit>> lift(CheckedConsume7<? super T, ? super U, ? super V, ? super W, ? super X, ? super Y, ? super Z> func) {
        return (t, u, v, w, x, y, z) -> Try.of(() -> {
            func.apply(t, u, v, w, x, y, z);
            return Unit.INSTANCE;
        });
    }

    default Func1<A, Func1<B, Func1<C, Func1<D, Func1<E, Func1<F, CheckedConsume1<G>>>>>>> curried() {
        return t -> u -> v -> w -> x -> y -> z -> apply(t, u, v, w, x, y, z);
    }

    default CheckedConsume1<Tuple7<A, B, C, D, E, F, G>> tupled() {
        return x -> apply(x.$1, x.$2, x.$3, x.$4, x.$5, x.$6, x.$7);
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
