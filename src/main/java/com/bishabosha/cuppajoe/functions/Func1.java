/*
 * Copyright (c) 2017. Jamie Thompson <bishbashboshjt@gmail.com>
 */

package com.bishabosha.cuppajoe.functions;

import com.bishabosha.cuppajoe.control.Option;
import com.bishabosha.cuppajoe.control.Try;
import com.bishabosha.cuppajoe.tuples.Apply1;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.function.Function;
import java.util.function.Supplier;

public interface Func1<A, R> extends Function<A, R> {

    @NotNull
    @Contract(pure = true)
    static <X, R> Func1<X, R> of(Function<X, R> reference) {
        return reference::apply;
    }

    @Contract(pure = true)
    static <X,R> Func1<X,R> narrow(Function<? super X, ? extends R> func) {
        return func::apply;
    }

    @Contract(pure = true)
    static <X, R> Func1<X, Option<R>> lift(Function<? super X, ? extends R> func) {
        return x -> Try.<R>of(() -> func.apply(x)).lift();
    }

    default Func1<Supplier<A>, R> lazyInput() {
        return x -> apply(x.get());
    }

    default Apply1<A, R> applied() {
        return x -> apply(x.$1());
    }

    @Override
    default <U> Func1<U, R> compose(Function<? super U, ? extends A> before) {
        return u -> apply(before.apply(u));
    }

    @Override
    default <U> Func1<A, U> andThen(Function<? super R, ? extends U> after) {
        return a -> after.apply(apply(a));
    }
}
