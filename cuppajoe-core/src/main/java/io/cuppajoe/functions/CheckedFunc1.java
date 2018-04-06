/*
 * Copyright (c) 2017. Jamie Thompson <bishbashboshjt@gmail.com>
 */

package io.cuppajoe.functions;

import io.cuppajoe.annotation.NonNull;
import io.cuppajoe.control.Try;
import io.cuppajoe.tuples.Tuple1;

import java.util.Objects;

@FunctionalInterface
public interface CheckedFunc1<A, R> {

    static <X, R> CheckedFunc1<X, R> of(@NonNull CheckedFunc1<X, R> reference) {
        return Objects.requireNonNull(reference);
    }

    static <X, R> CheckedFunc1<X, R> narrow(@NonNull CheckedFunc1<? super X, ? extends R> func) {
        Objects.requireNonNull(func);
        return func::apply;
    }

    static <X, R> Func1<X, Try<R>> lift(@NonNull CheckedFunc1<? super X, ? extends R> func) {
        Objects.requireNonNull(func);
        return x -> Try.of(() -> func.apply(x));
    }

    default CheckedFunc1<Tuple1<A>, R> tupled() {
        return x -> apply(x.$1);
    }

    R apply(A a) throws Exception;
}
