/*
 * Copyright (c) 2017. Jamie Thompson <bishbashboshjt@gmail.com>
 */

package com.github.bishabosha.cuppajoe.higher.functions;

import com.github.bishabosha.cuppajoe.annotation.NonNull;
import com.github.bishabosha.cuppajoe.higher.value.Value1.Value;

import java.util.Objects;
import java.util.function.Function;
import java.util.function.Supplier;

@FunctionalInterface
public interface Func1<A, R> extends Function<A, R> {

    static <X> Func1<X, X> identity() {
        return x -> x;
    }

    static <X, R> Func1<X, R> of(@NonNull Function<X, R> reference) {
        Objects.requireNonNull(reference, "reference");
        return reference::apply;
    }

    static <X, R> Func1<X, R> narrow(@NonNull Function<? super X, ? extends R> func) {
        Objects.requireNonNull(func, "func");
        return func::apply;
    }

    static <X, R> Func1<X, Value<R>> lift(@NonNull Function<? super X, ? extends R> func) {
        Objects.requireNonNull(func, "func");
        return CheckedFunc1.lift(func::apply);
    }

    default Func1<Supplier<A>, R> lazyInput() {
        return x -> apply(x.get());
    }

    default Func0<R> capture(A a) {
        return () -> apply(a);
    }

    @Override
    default <U> Func1<U, R> compose(@NonNull Function<? super U, ? extends A> before) {
        Objects.requireNonNull(before, "before");
        return u -> apply(before.apply(u));
    }

    @Override
    default <U> Func1<A, U> andThen(@NonNull Function<? super R, ? extends U> after) {
        Objects.requireNonNull(after, "after");
        return a -> after.apply(apply(a));
    }
}
