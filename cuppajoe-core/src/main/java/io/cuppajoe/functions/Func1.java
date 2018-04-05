/*
 * Copyright (c) 2017. Jamie Thompson <bishbashboshjt@gmail.com>
 */

package io.cuppajoe.functions;

import io.cuppajoe.control.Try;
import io.cuppajoe.tuples.Tuple1;

import java.util.function.Function;
import java.util.function.Supplier;

@FunctionalInterface
public interface Func1<A, R> extends Function<A, R> {

    static <X, R> Func1<X, R> of(Function<X, R> reference) {
        return reference::apply;
    }

    static <X, R> Func1<X, R> narrow(Function<? super X, ? extends R> func) {
        return func::apply;
    }

    static <X, R> Func1<X, Try<R>> lift(Function<? super X, ? extends R> func) {
        return CheckedFunc1.lift(func::apply);
    }

    default Func1<Supplier<A>, R> lazyInput() {
        return x -> apply(x.get());
    }

    default Func1<Tuple1<A>, R> tupled() {
        return x -> apply(x.$1);
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
