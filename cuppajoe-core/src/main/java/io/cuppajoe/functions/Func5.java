/*
 * Copyright (c) 2017. Jamie Thompson <bishbashboshjt@gmail.com>
 */

package io.cuppajoe.functions;

import io.cuppajoe.annotation.NonNull;
import io.cuppajoe.control.Try;
import io.cuppajoe.tuples.Tuple5;

import java.util.Objects;
import java.util.function.Function;
import java.util.function.Supplier;

@FunctionalInterface
public interface Func5<A, B, C, D, E, R> {

    static <V, W, X, Y, Z, R> Func5<V, W, X, Y, Z, R> of(@NonNull Func5<V, W, X, Y, Z, R> reference) {
        return Objects.requireNonNull(reference);
    }

    static <V, W, X, Y, Z, R> Func5<V, W, X, Y, Z, R> narrow(@NonNull Func5<? super V, ? super W, ? super X, ? super Y, ? super Z, ? extends R> func) {
        Objects.requireNonNull(func);
        return func::apply;
    }

    static <V, W, X, Y, Z, R> Func5<V, W, X, Y, Z, Try<R>> lift(@NonNull Func5<? super V, ? super W, ? super X, ? super Y, ? super Z, ? extends R> func) {
        Objects.requireNonNull(func);
        return CheckedFunc5.lift(func::apply);
    }

    default Func1<A, Func1<B, Func1<C, Func1<D, Func1<E, R>>>>> curried() {
        return v -> w -> x -> y -> z -> apply(v, w, x, y, z);
    }

    default Func1<Tuple5<A, B, C, D, E>, R> tupled() {
        return x -> apply(x.$1, x.$2, x.$3, x.$4, x.$5);
    }

    default <U> Func5<A, B, C, D, E, U> andThen(@NonNull Function<? super R, ? extends U> next) {
        Objects.requireNonNull(next);
        return (s, t, u, v, w) -> next.apply(apply(s, t, u, v, w));
    }

    default Func5<Supplier<A>, Supplier<B>, Supplier<C>, Supplier<D>, Supplier<E>, R> lazyInput() {
        return (a, b, c, d, e) -> apply(a.get(), b.get(), c.get(), d.get(), e.get());
    }

    R apply(A a, B b, C c, D d, E e);

    default Func4<B, C, D, E, R> apply(A a) {
        return (b, c, d, e) -> apply(a, b, c, d, e);
    }

    default Func3<C, D, E, R> apply(A a, B b) {
        return (c, d, e) -> apply(a, b, c, d, e);
    }

    default Func2<D, E, R> apply(A a, B b, C c) {
        return (d, e) -> apply(a, b, c, d, e);
    }

    default Func1<E, R> apply(A a, B b, C c, D d) {
        return e -> apply(a, b, c, d, e);
    }
}
