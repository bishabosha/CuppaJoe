/*
 * Copyright (c) 2017. Jamie Thompson <bishbashboshjt@gmail.com>
 */

package com.github.bishabosha.cuppajoe.match.patterns;

import com.github.bishabosha.cuppajoe.API;
import com.github.bishabosha.cuppajoe.control.Option;

public interface Pattern<A> {
    Option<Result> test(A obj);

    Option<Result> PASS = API.Some(Result.empty());

    Option<Result> FAIL = API.None();

    static <O> Option<Result> bind(O $x) {
        return API.Some(Result.of($x));
    }
}

