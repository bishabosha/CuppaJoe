/*
 * Copyright (c) 2017. Jamie Thompson <bishbashboshjt@gmail.com>
 */

package io.cuppajoe.functions;

import io.cuppajoe.control.Try;
import io.cuppajoe.tuples.Unit;

@FunctionalInterface
public interface CheckedConsume0 {

    void apply() throws Exception;

    static CheckedConsume0 of(CheckedConsume0 reference) {
        return reference;
    }

    static Func0<Try<Unit>> lift(CheckedConsume0 func) {
        return () -> Try.of(() -> {
            func.apply();
            return Unit.INSTANCE;
        });
    }

    default CheckedConsume1<Unit> tupled() {
        return t -> apply();
    }
}
