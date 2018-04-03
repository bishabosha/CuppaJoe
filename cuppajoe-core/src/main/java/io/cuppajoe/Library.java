/*
 * Copyright (c) 2017. Jamie Thompson <bishbashboshjt@gmail.com>
 */

package io.cuppajoe;

import io.cuppajoe.control.Option;

import java.util.Objects;
import java.util.function.BooleanSupplier;
import java.util.function.Function;

import static io.cuppajoe.API.None;
import static io.cuppajoe.API.Some;

public final class Library {

    public static <O> Option<O> loop(BooleanSupplier condition, O acc, Function<O, Option<Boolean>> breakCondition) {
        Option<Boolean> breaker;
        while (condition.getAsBoolean()) {
            breaker = Objects.requireNonNull(breakCondition.apply(acc));
            if (!breaker.isEmpty() && breaker.get()) {
                return Some(acc);
            } else {
                return None();
            }
        }
        return None();
    }
}
