/*
 * Copyright (c) 2017. Jamie Thompson <bishbashboshjt@gmail.com>
 */

package com.github.bishabosha.cuppajoe.functions;

import com.github.bishabosha.cuppajoe.annotation.NonNull;
import com.github.bishabosha.cuppajoe.higher.value.Value1.Value;

import java.util.Objects;

@FunctionalInterface
public interface CheckedConsume1<R> {

    void apply(R r) throws Exception;

    static <R> CheckedConsume1<R> of(@NonNull CheckedConsume1<R> reference) {
        return Objects.requireNonNull(reference, "reference");
    }

    static <R> Func1<R, Value<Void>> lift(@NonNull CheckedConsume1<? super R> func) {
        Objects.requireNonNull(func, "func");
        return x -> LiftOps.liftConsumer(() -> func.apply(x));
    }
}
