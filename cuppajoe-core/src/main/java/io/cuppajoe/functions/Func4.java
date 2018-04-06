/*
 * Copyright (c) 2017. Jamie Thompson <bishbashboshjt@gmail.com>
 */
package io.cuppajoe.functions;

import io.cuppajoe.annotation.NonNull;
import io.cuppajoe.control.Try;
import io.cuppajoe.tuples.Tuple4;

import java.util.Objects;
import java.util.function.Function;
import java.util.function.Supplier;

@FunctionalInterface
public interface Func4<A, B, C, D, R> {

    static <W, X, Y, Z, R> Func4<W, X, Y, Z, R> of(@NonNull Func4<W, X, Y, Z, R> reference) {
        return Objects.requireNonNull(reference);
    }

    static <W, X, Y, Z, R> Func4<W, X, Y, Z, R> narrow(@NonNull Func4<? super W, ? super X, ? super Y, ? super Z, ? extends R> func) {
        Objects.requireNonNull(func);
        return func::apply;
    }

    static <W, X, Y, Z, R> Func4<W, X, Y, Z, Try<R>> lift(@NonNull Func4<? super W, ? super X, ? super Y, ? super Z, ? extends R> func) {
        Objects.requireNonNull(func);
        return CheckedFunc4.lift(func::apply);
    }

    default Func1<A, Func1<B, Func1<C, Func1<D, R>>>> curried() {
        return w -> x -> y -> z -> apply(w, x, y, z);
    }

    default Func1<Tuple4<A, B, C, D>, R> tupled() {
        return x -> apply(x.$1, x.$2, x.$3, x.$4);
    }

    default <U> Func4<A, B, C, D, U> andThen(@NonNull Function<? super R, ? extends U> next) {
        Objects.requireNonNull(next);
        return (s, t, u, v) -> next.apply(apply(s, t, u, v));
    }

    default Func4<Supplier<A>, Supplier<B>, Supplier<C>, Supplier<D>, R> lazyInput() {
        return (a, b, c, d) -> apply(a.get(), b.get(), c.get(), d.get());
    }

    R apply(A a, B b, C c, D d);

    default Func3<B, C, D, R> apply(A a) {
        return (b, c, d) -> apply(a, b, c, d);
    }

    default Func2<C, D, R> apply(A a, B b) {
        return (c, d) -> apply(a, b, c, d);
    }

    default Func1<D, R> apply(A a, B b, C c) {
        return d -> apply(a, b, c, d);
    }
}
