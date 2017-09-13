/*
 * Copyright (c) 2017. Jamie Thompson <bishbashboshjt@gmail.com>
 */

package com.bishabosha.caffeine.functional.functions;

import com.bishabosha.caffeine.functional.Option;

public interface CheckedFunc0<R> {
    R apply() throws Throwable;

    static <R> CheckedFunc0<R> of(CheckedFunc0<R> func) {
        return func;
    }

    default Func0<Option<R>> lifted() {
        return () -> {
            try {
                return Option.ofUnknown(apply());
            } catch (Throwable e) {
                return Option.nothing();
            }
        };
    }
}
