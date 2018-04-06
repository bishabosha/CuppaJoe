/*
 * Copyright (c) 2017. Jamie Thompson <bishbashboshjt@gmail.com>
 */

package io.cuppajoe.functions;

import io.cuppajoe.annotation.NonNull;
import io.cuppajoe.control.Try;
import io.cuppajoe.tuples.Tuple3;

import java.util.Objects;
import java.util.function.Function;
import java.util.function.Supplier;

@FunctionalInterface
public interface Func3<A, B, C, R> {

    static <X, Y, Z, R> Func3<X, Y, Z, R> of(@NonNull Func3<X, Y, Z, R> reference) {
        return Objects.requireNonNull(reference);
    }

    static <X, Y, Z, R> Func3<X, Y, Z, R> narrow(@NonNull Func3<? super X, ? super Y, ? super Z, ? extends R> func) {
        Objects.requireNonNull(func);
        return func::apply;
    }

    static <X, Y, Z, R> Func3<X, Y, Z, Try<R>> lift(@NonNull Func3<? super X, ? super Y, ? super Z, ? extends R> func) {
        Objects.requireNonNull(func);
        return CheckedFunc3.lift(func::apply);
    }

    default Func1<A, Func1<B, Func1<C, R>>> curried() {
        return x -> y -> z -> apply(x, y, z);
    }

    default Func1<Tuple3<A, B, C>, R> tupled() {
        return x -> apply(x.$1, x.$2, x.$3);
    }

    default <U> Func3<A, B, C, U> andThen(@NonNull Function<? super R, ? extends U> next) {
        Objects.requireNonNull(next);
        return (s, t, u) -> next.apply(apply(s, t, u));
    }

    default Func3<Supplier<A>, Supplier<B>, Supplier<C>, R> lazyInput() {
        return (a, b, c) -> apply(a.get(), b.get(), c.get());
    }

    R apply(A a, B b, C c);

    default Func2<B, C, R> apply(A a) {
        return (b, c) -> apply(a, b, c);
    }

    default Func1<C, R> apply(A a, B b) {
        return c -> apply(a, b, c);
    }
}
