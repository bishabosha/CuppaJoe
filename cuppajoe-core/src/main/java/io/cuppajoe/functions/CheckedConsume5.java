/*
 * Copyright (c) 2017. Jamie Thompson <bishbashboshjt@gmail.com>
 */

package io.cuppajoe.functions;

import io.cuppajoe.annotation.NonNull;
import io.cuppajoe.control.Try;
import io.cuppajoe.tuples.Tuple5;
import io.cuppajoe.tuples.Unit;

import java.util.Objects;
import java.util.function.Supplier;

@FunctionalInterface
public interface CheckedConsume5<A, B, C, D, E> {

    void apply(A a, B b, C c, D d, E e) throws Exception;

    static <V, W, X, Y, Z> CheckedConsume5<V, W, X, Y, Z> of(@NonNull CheckedConsume5<V, W, X, Y, Z> reference) {
        return Objects.requireNonNull(reference);
    }

    static <V, W, X, Y, Z> CheckedConsume5<V, W, X, Y, Z> narrow(@NonNull CheckedConsume5<? super V, ? super W, ? super X, ? super Y, ? super Z> func) {
        Objects.requireNonNull(func);
        return func::apply;
    }

    static <V, W, X, Y, Z> Func5<V, W, X, Y, Z, Try<Unit>> lift(@NonNull CheckedConsume5<? super V, ? super W, ? super X, ? super Y, ? super Z> func) {
        Objects.requireNonNull(func);
        return (v, w, x, y, z) -> Try.of(() -> {
            func.apply(v, w, x, y, z);
            return Unit.INSTANCE;
        });
    }

    default Func1<A, Func1<B, Func1<C, Func1<D, CheckedConsume1<E>>>>> curried() {
        return v -> w -> x -> y -> z -> apply(v, w, x, y, z);
    }

    default CheckedConsume1<Tuple5<A, B, C, D, E>> tupled() {
        return x -> apply(x.$1, x.$2, x.$3, x.$4, x.$5);
    }

    default CheckedConsume5<Supplier<A>, Supplier<B>, Supplier<C>, Supplier<D>, Supplier<E>> lazyInput() {
        return (a, b, c, d, e) -> apply(a.get(), b.get(), c.get(), d.get(), e.get());
    }

    default CheckedConsume1<E> apply(A a, B b, C c, D d) {
        return e -> apply(a, b, c, d, e);
    }

    default CheckedConsume2<D, E> apply(A a, B b, C c) {
        return (d, e) -> apply(a, b, c, d, e);
    }

    default CheckedConsume3<C, D, E> apply(A a, B b) {
        return (c, d, e) -> apply(a, b, c, d, e);
    }

    default CheckedConsume4<B, C, D, E> apply(A a) {
        return (b, c, d, e) -> apply(a, b, c, d, e);
    }
}
