/*
 * Copyright (c) 2017. Jamie Thompson <bishbashboshjt@gmail.com>
 */

package io.cuppajoe.functions;

import io.cuppajoe.annotation.NonNull;
import io.cuppajoe.control.Try;
import io.cuppajoe.tuples.Tuple1;

import java.util.Objects;
import java.util.function.Function;
import java.util.function.Supplier;

@FunctionalInterface
public interface Func1<A, R> extends Function<A, R> {

    static <X, R> Func1<X, R> of(@NonNull Function<X, R> reference) {
        Objects.requireNonNull(reference);
        return reference::apply;
    }

    static <X, R> Func1<X, R> narrow(@NonNull Function<? super X, ? extends R> func) {
        Objects.requireNonNull(func);
        return func::apply;
    }

    static <X, R> Func1<X, Try<R>> lift(@NonNull Function<? super X, ? extends R> func) {
        Objects.requireNonNull(func);
        return CheckedFunc1.lift(func::apply);
    }

    default Func1<Supplier<A>, R> lazyInput() {
        return x -> apply(x.get());
    }

    default Func1<Tuple1<A>, R> tupled() {
        return x -> apply(x.$1);
    }

    @Override
    default <U> Func1<U, R> compose(@NonNull Function<? super U, ? extends A> before) {
        Objects.requireNonNull(before);
        return u -> apply(before.apply(u));
    }

    @Override
    default <U> Func1<A, U> andThen(@NonNull Function<? super R, ? extends U> after) {
        Objects.requireNonNull(after);
        return a -> after.apply(apply(a));
    }
}
