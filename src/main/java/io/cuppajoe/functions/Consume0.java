/*
 * Copyright (c) 2017. Jamie Thompson <bishbashboshjt@gmail.com>
 */

package io.cuppajoe.functions;

import io.cuppajoe.control.Try;
import io.cuppajoe.tuples.Unit;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

@FunctionalInterface
public interface Consume0 extends Runnable {

    default void apply() {
        run();
    }

    @NotNull
    @Contract(pure = true)
    static Consume0 of(Runnable reference) {
        return reference::run;
    }

    @Contract(pure = true)
    static Func0<Try<Unit>> lift(Runnable func) {
        return CheckedConsume0.lift(func::run);
    }

    @Contract(pure = true)
    default Consume1<Unit> tupled() {
        return x -> apply();
    }
}
