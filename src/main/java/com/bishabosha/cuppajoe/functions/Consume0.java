/*
 * Copyright (c) 2017. Jamie Thompson <bishbashboshjt@gmail.com>
 */

package com.bishabosha.cuppajoe.functions;

import com.bishabosha.cuppajoe.control.Try;
import com.bishabosha.cuppajoe.tuples.Product0;
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
    static Func0<Try<Void>> lift(Runnable func) {
        return CheckedConsume0.lift(func::run);
    }

    @Contract(pure = true)
    default Consume1<Product0> tupled() {
        return x -> apply();
    }
}
