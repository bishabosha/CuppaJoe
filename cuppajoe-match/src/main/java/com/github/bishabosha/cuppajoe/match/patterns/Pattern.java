/*
 * Copyright (c) 2017. Jamie Thompson <bishbashboshjt@gmail.com>
 */

package com.github.bishabosha.cuppajoe.match.patterns;

import com.github.bishabosha.cuppajoe.control.Option;

import static com.github.bishabosha.cuppajoe.API.None;
import static com.github.bishabosha.cuppajoe.API.Some;

public interface Pattern<A> {
    Option<Result> test(A obj);

    Option<Result> PASS = Some(Result.empty());

    Option<Result> FAIL = None();

    static <O> Option<Result> bind(O $x) {
        return Some(Result.of($x));
    }
}

