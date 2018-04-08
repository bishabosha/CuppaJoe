/*
 * Copyright (c) 2017. Jamie Thompson <bishbashboshjt@gmail.com>
 */

package com.github.bishabosha.cuppajoe.higher.functions;

import com.github.bishabosha.cuppajoe.annotation.NonNull;
import com.github.bishabosha.cuppajoe.higher.value.Value1.Value;

import java.util.Objects;

@FunctionalInterface
public interface Consume0 extends Runnable {

    default void apply() {
        run();
    }

    static Consume0 of(@NonNull Runnable reference) {
        Objects.requireNonNull(reference, "reference");
        return reference::run;
    }

    static Func0<Value<Void>> lift(@NonNull Runnable func) {
        Objects.requireNonNull(func, "func");
        return CheckedConsume0.lift(func::run);
    }
}
