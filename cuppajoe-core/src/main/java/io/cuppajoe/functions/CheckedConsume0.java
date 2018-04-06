/*
 * Copyright (c) 2017. Jamie Thompson <bishbashboshjt@gmail.com>
 */

package io.cuppajoe.functions;

import io.cuppajoe.annotation.NonNull;
import io.cuppajoe.control.Try;
import io.cuppajoe.tuples.Unit;

import java.util.Objects;

@FunctionalInterface
public interface CheckedConsume0 {

    void apply() throws Exception;

    static CheckedConsume0 of(@NonNull CheckedConsume0 reference) {
        return Objects.requireNonNull(reference, "reference");
    }

    static Func0<Try<Unit>> lift(@NonNull CheckedConsume0 func) {
        Objects.requireNonNull(func, "func");
        return () -> Try.of(() -> {
            func.apply();
            return Unit.INSTANCE;
        });
    }

    default CheckedConsume1<Unit> tupled() {
        return t -> apply();
    }
}
