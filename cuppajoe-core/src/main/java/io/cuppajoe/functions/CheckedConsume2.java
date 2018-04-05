/*
 * Copyright (c) 2017. Jamie Thompson <bishbashboshjt@gmail.com>
 */

package io.cuppajoe.functions;

import io.cuppajoe.control.Try;
import io.cuppajoe.tuples.Tuple2;
import io.cuppajoe.tuples.Unit;

import java.util.function.Supplier;

@FunctionalInterface
public interface CheckedConsume2<A, B> {

    void apply(A a, B b) throws Exception;

    static <X, Y> CheckedConsume2<X, Y> of(CheckedConsume2<X, Y> reference) {
        return reference;
    }

    static <X, Y> CheckedConsume2<X, Y> narrow(CheckedConsume2<? super X, ? super Y> func) {
        return func::apply;
    }

    static <X, Y> Func2<X, Y, Try<Unit>> lift(CheckedConsume2<? super X, ? super Y> func) {
        return (x, y) -> Try.of(() -> {
            func.apply(x, y);
            return Unit.INSTANCE;
        });
    }

    default Func1<A, CheckedConsume1<B>> curried() {
        return x -> y -> apply(x, y);
    }

    default CheckedConsume1<Tuple2<A, B>> tupled() {
        return x -> apply(x.$1, x.$2);
    }

    default CheckedConsume2<Supplier<A>, Supplier<B>> lazyInput() {
        return (a, b) -> apply(a.get(), b.get());
    }

    default CheckedConsume1<B> apply(A a) {
        return b -> apply(a, b);
    }
}
