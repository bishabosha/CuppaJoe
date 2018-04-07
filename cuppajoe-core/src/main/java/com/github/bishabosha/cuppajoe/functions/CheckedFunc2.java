/*
 * Copyright (c) 2017. Jamie Thompson <bishbashboshjt@gmail.com>
 */

package com.github.bishabosha.cuppajoe.functions;

import com.github.bishabosha.cuppajoe.annotation.NonNull;
import com.github.bishabosha.cuppajoe.control.Try;
import com.github.bishabosha.cuppajoe.tuples.Tuple2;

import java.util.Objects;

@FunctionalInterface
public interface CheckedFunc2<A, B, R> {

    static <X, Y, R> CheckedFunc2<X, Y, R> of(@NonNull CheckedFunc2<X, Y, R> reference) {
        return Objects.requireNonNull(reference, "reference");
    }

    static <X, Y, R> CheckedFunc2<X, Y, R> narrow(@NonNull CheckedFunc2<? super X, ? super Y, ? extends R> func) {
        Objects.requireNonNull(func, "func");
        return func::apply;
    }

    static <X, Y, R> Func2<X, Y, Try<R>> lift(@NonNull CheckedFunc2<? super X, ? super Y, ? extends R> func) {
        Objects.requireNonNull(func, "func");
        return (x, y) -> Try.of(() -> func.apply(x, y));
    }

    default CheckedFunc1<A, CheckedFunc1<B, R>> curried() {
        return x -> y -> apply(x, y);
    }

    default CheckedFunc1<Tuple2<A, B>, R> tupled() {
        return x -> apply(x.$1, x.$2);
    }

    R apply(A a, B b) throws Exception;

    default CheckedFunc1<B, R> apply(A a) {
        return b -> apply(a, b);
    }
}
