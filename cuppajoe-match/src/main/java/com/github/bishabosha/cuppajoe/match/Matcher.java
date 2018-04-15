/*
 * Copyright (c) 2017. Jamie Thompson <bishbashboshjt@gmail.com>
 */

package com.github.bishabosha.cuppajoe.match;

import com.github.bishabosha.cuppajoe.annotation.NonNull;
import com.github.bishabosha.cuppajoe.control.Option;

import static com.github.bishabosha.cuppajoe.match.API.Cases;

public class Matcher<I> {

    private I toMatch;

    static <I> Matcher<I> create(I toMatch) {
        return new Matcher<>(toMatch);
    }

    private Matcher(I toMatch) {
        this.toMatch = toMatch;
    }

    @SafeVarargs
    public final <O> Option<O> option(@NonNull Case<I, O>... cases) {
        return Cases(cases).match(toMatch);
    }

    @SafeVarargs
    public final <O> O of(@NonNull Case<I, O>... cases) {
        return Cases(cases).get(toMatch);
    }
}


