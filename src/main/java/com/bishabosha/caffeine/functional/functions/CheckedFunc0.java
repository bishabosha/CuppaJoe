/*
 * Copyright (c) 2017. Jamie Thompson <bishbashboshjt@gmail.com>
 */

package com.bishabosha.caffeine.functional.functions;

public interface CheckedFunc0<R> {
    R apply() throws Throwable;

    static <R> CheckedFunc0<R> of(CheckedFunc0<R> func) {
        return func;
    }
}
