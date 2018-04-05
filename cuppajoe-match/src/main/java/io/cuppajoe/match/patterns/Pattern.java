/*
 * Copyright (c) 2017. Jamie Thompson <bishbashboshjt@gmail.com>
 */

package io.cuppajoe.match.patterns;

import io.cuppajoe.control.Option;

import static io.cuppajoe.API.None;
import static io.cuppajoe.API.Some;

public interface Pattern<A> {
    Option<Result> test(A obj);

    Option<Result> PASS = Some(Result.empty());

    Option<Result> FAIL = None();

    static <O> Option<Result> bind(O $x) {
        return Some(Result.of($x));
    }
}

