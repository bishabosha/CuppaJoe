/*
 * Copyright (c) 2017. Jamie Thompson <bishbashboshjt@gmail.com>
 */

package io.cuppajoe.functions;

import io.cuppajoe.annotation.NonNull;
import io.cuppajoe.control.Try;
import io.cuppajoe.tuples.Tuple6;
import io.cuppajoe.tuples.Unit;

import java.util.Objects;
import java.util.function.Supplier;

@FunctionalInterface
public interface CheckedConsume6<A, B, C, D, E, F> {

    void apply(A a, B b, C c, D d, E e, F f) throws Exception;

    static <U, V, W, X, Y, Z> CheckedConsume6<U, V, W, X, Y, Z> of(@NonNull CheckedConsume6<U, V, W, X, Y, Z> reference) {
        return Objects.requireNonNull(reference);
    }

    static <U, V, W, X, Y, Z> CheckedConsume6<U, V, W, X, Y, Z> narrow(@NonNull CheckedConsume6<? super U, ? super V, ? super W, ? super X, ? super Y, ? super Z> func) {
        Objects.requireNonNull(func);
        return func::apply;
    }

    static <U, V, W, X, Y, Z> Func6<U, V, W, X, Y, Z, Try<Unit>> lift(@NonNull CheckedConsume6<? super U, ? super V, ? super W, ? super X, ? super Y, ? super Z> func) {
        Objects.requireNonNull(func);
        return (u, v, w, x, y, z) -> Try.of(() -> {
            func.apply(u, v, w, x, y, z);
            return Unit.INSTANCE;
        });
    }

    default Func1<A, Func1<B, Func1<C, Func1<D, Func1<E, CheckedConsume1<F>>>>>> curried() {
        return u -> v -> w -> x -> y -> z -> apply(u, v, w, x, y, z);
    }

    default CheckedConsume1<Tuple6<A, B, C, D, E, F>> tupled() {
        return x -> apply(x.$1, x.$2, x.$3, x.$4, x.$5, x.$6);
    }

    default CheckedConsume6<Supplier<A>, Supplier<B>, Supplier<C>, Supplier<D>, Supplier<E>, Supplier<F>> lazyInput() {
        return (a, b, c, d, e, f) -> apply(a.get(), b.get(), c.get(), d.get(), e.get(), f.get());
    }

    default CheckedConsume1<F> apply(A a, B b, C c, D d, E e) {
        return f -> apply(a, b, c, d, e, f);
    }

    default CheckedConsume2<E, F> apply(A a, B b, C c, D d) {
        return (e, f) -> apply(a, b, c, d, e, f);
    }

    default CheckedConsume3<D, E, F> apply(A a, B b, C c) {
        return (d, e, f) -> apply(a, b, c, d, e, f);
    }

    default CheckedConsume4<C, D, E, F> apply(A a, B b) {
        return (c, d, e, f) -> apply(a, b, c, d, e, f);
    }

    default CheckedConsume5<B, C, D, E, F> apply(A a) {
        return (b, c, d, e, f) -> apply(a, b, c, d, e, f);
    }
}
