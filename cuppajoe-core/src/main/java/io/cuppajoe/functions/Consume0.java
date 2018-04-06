/*
 * Copyright (c) 2017. Jamie Thompson <bishbashboshjt@gmail.com>
 */

package io.cuppajoe.functions;

import io.cuppajoe.annotation.NonNull;
import io.cuppajoe.control.Try;
import io.cuppajoe.tuples.Unit;

import java.util.Objects;

@FunctionalInterface
public interface Consume0 extends Runnable {

    default void apply() {
        run();
    }

    static Consume0 of(@NonNull Runnable reference) {
        Objects.requireNonNull(reference);
        return reference::run;
    }

    static Func0<Try<Unit>> lift(@NonNull Runnable func) {
        Objects.requireNonNull(func);
        return CheckedConsume0.lift(func::run);
    }

    default Consume1<Unit> tupled() {
        return x -> apply();
    }
}
