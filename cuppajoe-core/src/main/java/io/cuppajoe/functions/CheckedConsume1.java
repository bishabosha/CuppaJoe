/*
 * Copyright (c) 2017. Jamie Thompson <bishbashboshjt@gmail.com>
 */

package io.cuppajoe.functions;

import io.cuppajoe.control.Try;
import io.cuppajoe.tuples.Tuple1;
import io.cuppajoe.tuples.Unit;

@FunctionalInterface
public interface CheckedConsume1<R> {

    void apply(R r) throws Exception;

    static <R> CheckedConsume1<R> of(CheckedConsume1<R> reference) {
        return reference;
    }

    static <R> Func1<R, Try<Unit>> lift(CheckedConsume1<? super R> func) {
        return x -> Try.of(() -> {
            func.apply(x);
            return Unit.INSTANCE;
        });
    }

    default CheckedConsume1<Tuple1<R>> tupled() {
        return t -> apply(t.$1);
    }
}
