/*
 * Copyright (c) 2017. Jamie Thompson <bishbashboshjt@gmail.com>
 */

package com.github.bishabosha.cuppajoe.functions;

import com.github.bishabosha.cuppajoe.annotation.NonNull;
import com.github.bishabosha.cuppajoe.control.Try;
import com.github.bishabosha.cuppajoe.tuples.Tuple1;
import com.github.bishabosha.cuppajoe.tuples.Unit;

import java.util.Objects;

@FunctionalInterface
public interface CheckedConsume1<R> {

    void apply(R r) throws Exception;

    static <R> CheckedConsume1<R> of(@NonNull CheckedConsume1<R> reference) {
        return Objects.requireNonNull(reference, "reference");
    }

    static <R> Func1<R, Try<Unit>> lift(@NonNull CheckedConsume1<? super R> func) {
        Objects.requireNonNull(func, "func");
        return x -> Try.of(() -> {
            func.apply(x);
            return Unit.INSTANCE;
        });
    }

    default CheckedConsume1<Tuple1<R>> tupled() {
        return t -> apply(t.$1);
    }
}
