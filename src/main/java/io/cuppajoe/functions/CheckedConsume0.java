/*
 * Copyright (c) 2017. Jamie Thompson <bishbashboshjt@gmail.com>
 */

package io.cuppajoe.functions;

import io.cuppajoe.Unit;
import io.cuppajoe.control.Try;
import io.cuppajoe.tuples.Product0;
import org.jetbrains.annotations.Contract;

@FunctionalInterface
public interface CheckedConsume0 {

    void apply() throws Exception;

    @Contract(pure = true)
    static CheckedConsume0 of(CheckedConsume0 reference) {
        return reference;
    }

    @Contract(pure = true)
    static Func0<Try<Unit>> lift(CheckedConsume0 func) {
        return () -> Try.of(() -> {
            func.apply();
            return Unit.INSTANCE;
        });
    }

    @Contract(pure = true)
    default CheckedConsume1<Product0> tupled() {
        return t -> apply();
    }
}
