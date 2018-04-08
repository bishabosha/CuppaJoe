package com.github.bishabosha.cuppajoe.functions;

import com.github.bishabosha.cuppajoe.higher.value.Value1;
import com.github.bishabosha.cuppajoe.higher.value.Value1.Value;

import java.util.concurrent.Callable;

final class LiftOps {
    private LiftOps() {
    }

    static <R> Value<R> liftFunction(Callable<R> function) {
        try {
            return Value1.value(function.call());
        } catch (Exception e) {
            return Value1.empty();
        }
    }

    static Value<Void> liftConsumer(CheckedConsume0 runnable) {
        try {
            runnable.apply();
            return Value1.value(null);
        } catch (Exception e) {
            return Value1.empty();
        }
    }
}
