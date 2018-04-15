/*
 * Copyright (c) 2017. Jamie Thompson <bishbashboshjt@gmail.com>
 */

package com.github.bishabosha.cuppajoe.higher.functions;

import com.github.bishabosha.cuppajoe.annotation.NonNull;
import com.github.bishabosha.cuppajoe.higher.value.Value1.Value;

import java.util.Objects;

@FunctionalInterface
public interface CheckedConsume0 {

    void apply() throws Exception;

    static CheckedConsume0 of(@NonNull CheckedConsume0 reference) {
        return Objects.requireNonNull(reference, "reference");
    }

    static Func0<Value<Void>> lift(@NonNull CheckedConsume0 func) {
        Objects.requireNonNull(func, "func");
        return () -> LiftOps.liftConsumer(func);
    }
}
