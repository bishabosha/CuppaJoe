/*
 * Copyright (c) 2017. Jamie Thompson <bishbashboshjt@gmail.com>
 */

package io.cuppajoe.functions;

import io.cuppajoe.annotation.NonNull;
import io.cuppajoe.control.Try;
import io.cuppajoe.tuples.Tuple2;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;

@FunctionalInterface
public interface Func2<A, B, R> extends BiFunction<A, B, R> {

    static <X, Y, R> Func2<X, Y, R> of(@NonNull BiFunction<X, Y, R> reference) {
        Objects.requireNonNull(reference);
        return reference::apply;
    }

    static <X, Y, R> Func2<X, Y, R> narrow(@NonNull BiFunction<? super X, ? super Y, ? extends R> func) {
        Objects.requireNonNull(func);
        return func::apply;
    }

    static <X, Y, R> Func2<X, Y, Try<R>> lift(@NonNull BiFunction<? super X, ? super Y, ? extends R> func) {
        Objects.requireNonNull(func);
        return CheckedFunc2.lift(func::apply);
    }

    default Func1<A, Func1<B, R>> curried() {
        return x -> y -> apply(x, y);
    }

    default Func1<Tuple2<A, B>, R> tupled() {
        return x -> apply(x.$1, x.$2);
    }

    default Func2<Supplier<A>, Supplier<B>, R> lazyInput() {
        return (a, b) -> apply(a.get(), b.get());
    }

    default <Q, W> Func2<Q, W, R> compose2(@NonNull Function<? super Q, ? extends A> f1, @NonNull Function<? super W, ? extends B> f2) {
        Objects.requireNonNull(f1);
        Objects.requireNonNull(f2);
        return (q, w) -> apply(f1.apply(q), f2.apply(w));
    }

    @Override
    default <U> Func2<A, B, U> andThen(@NonNull Function<? super R, ? extends U> next) {
        Objects.requireNonNull(next);
        return (a, b) -> next.apply(apply(a, b));
    }

    default Func1<B, R> apply(A a) {
        return b -> apply(a, b);
    }
}
