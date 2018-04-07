package com.github.bishabosha.cuppajoe.match;

import com.github.bishabosha.cuppajoe.control.Option;

public final class API {

    public static <I> Matcher<I> Match(I toMatch) {
        return Matcher.create(toMatch);
    }

    public static <I, O> Option<O> Match(Option<I> toMatch, Case<I, O> options) {
        return toMatch.isEmpty() ? com.github.bishabosha.cuppajoe.API.None() : options.match(toMatch.get());
    }
}
