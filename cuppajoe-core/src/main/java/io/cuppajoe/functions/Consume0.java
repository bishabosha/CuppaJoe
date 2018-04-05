/*
 * Copyright (c) 2017. Jamie Thompson <bishbashboshjt@gmail.com>
 */

package io.cuppajoe.functions;

import io.cuppajoe.control.Try;
import io.cuppajoe.tuples.Unit;

@FunctionalInterface
public interface Consume0 extends Runnable {

    default void apply() {
        run();
    }

    static Consume0 of(Runnable reference) {
        return reference::run;
    }

    static Func0<Try<Unit>> lift(Runnable func) {
        return CheckedConsume0.lift(func::run);
    }

    default Consume1<Unit> tupled() {
        return x -> apply();
    }
}
