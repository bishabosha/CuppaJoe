/*
 * Copyright (c) 2017. Jamie Thompson <bishbashboshjt@gmail.com>
 */

package com.github.bishabosha.cuppajoe.functions;

import com.github.bishabosha.cuppajoe.annotation.NonNull;
import com.github.bishabosha.cuppajoe.control.Try;
import com.github.bishabosha.cuppajoe.tuples.Tuple2;
import com.github.bishabosha.cuppajoe.tuples.Unit;

import java.util.Objects;
import java.util.function.Supplier;

@FunctionalInterface
public interface CheckedConsume2<A, B> {

    void apply(A a, B b) throws Exception;

    static <X, Y> CheckedConsume2<X, Y> of(@NonNull CheckedConsume2<X, Y> reference) {
        return Objects.requireNonNull(reference, "reference");
    }

    static <X, Y> CheckedConsume2<X, Y> narrow(@NonNull CheckedConsume2<? super X, ? super Y> func) {
        Objects.requireNonNull(func, "func");
        return func::apply;
    }

    static <X, Y> Func2<X, Y, Try<Unit>> lift(@NonNull CheckedConsume2<? super X, ? super Y> func) {
        Objects.requireNonNull(func, "func");
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
