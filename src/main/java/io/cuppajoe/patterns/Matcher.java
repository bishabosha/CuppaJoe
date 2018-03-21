/*
 * Copyright (c) 2017. Jamie Thompson <bishbashboshjt@gmail.com>
 */

package io.cuppajoe.patterns;

import io.cuppajoe.control.Option;
import io.cuppajoe.patterns.Case.Guard;

import static io.cuppajoe.API.Nothing;

public class Matcher<I> {

    private I toMatch;

    public static <I> Matcher<I> create(I toMatch) {
        return new Matcher<>(toMatch);
    }

    public static <I, O> Option<O> create(Option<I> toMatch, Case<I, O> options) {
        return toMatch.isEmpty() ? Nothing() : options.match(toMatch.get());
    }

    @SafeVarargs
    public static <O> Option<O> guard(Guard<O>... guards) {
        return Case.combine(guards).match();
    }

    @SafeVarargs
    public static <O> O guardUnsafe(Guard<O>... guards) {
        var result = guard(guards);
        if (result.isEmpty()) {
            throw new RuntimeException("No Match Made");
        }
        return result.get();
    }

    private Matcher(I toMatch) {
        this.toMatch = toMatch;
    }

    @SafeVarargs
    public final <O> Option<O> option(Case<I, O>... cases) {
        return Case.combine(cases).match(toMatch);
    }

    @SafeVarargs
    public final <O> Option<Option<O>> wrap(Case<I, Option<O>>... cases) {
        Option<Option<O>> result;
        if ((result = option(cases)).isEmpty()) {
            return Nothing();
        }
        var temp = result.get();
        return temp.isEmpty() ? Nothing() : result;
    }

    @SafeVarargs
    public final <O> O of(Case<I, O>... cases) {
        Option<O> result;
        if ((result = option(cases)).isEmpty()) {
            throw new RuntimeException("No Match Made");
        }
        return result.get();
    }
}


