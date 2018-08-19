/*
 * Copyright (c) 2017. Jamie Thompson <bishbashboshjt@gmail.com>
 */

package com.github.bishabosha.cuppajoe.match.incubator;

import com.github.bishabosha.cuppajoe.control.Option;

import static com.github.bishabosha.cuppajoe.API.None;
import static com.github.bishabosha.cuppajoe.API.Some;

public interface Pattern<A> {
    Option<MatchResult> test(A obj);

    Option<MatchResult> PASS = Some(MatchResult.empty());

    Option<MatchResult> FAIL = None();

    static <O> Option<MatchResult> bind(O $x) {
        return Some(MatchResult.of($x));
    }
}

