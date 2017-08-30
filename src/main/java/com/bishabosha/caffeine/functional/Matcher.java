/*
 * Copyright (c) 2017. Jamie Thompson <bishbashboshjt@gmail.com>
 */

package com.bishabosha.caffeine.functional;

import com.bishabosha.caffeine.functional.Case.Guard;

public class Matcher<I> {

    private I toMatch;

    public static <I> Matcher<I> match(I toMatch) {
        return new Matcher<>(toMatch);
    }

    public static <I, O> Option<O> match(Option<I> toMatch, Case<I, O> options) {
        return toMatch.isSome() ? options.match(toMatch.get()) : Option.nothing();
    }

    public static <O> Option<O> guard(Guard<O>... guards) {
        return Case.combine(guards).match();
    }

    public static <O> O guardUnsafe(Guard<O>... guards) {
        Option<O> result = guard(guards);
        if (result.isSome()) {
            return result.get();
        }
        throw new RuntimeException("No Match Made");
    }

    private Matcher(I toMatch) {
        this.toMatch = toMatch;
    }

    public <O> Option<O> option(Case<I, O>... cases) {
        return Case.combine(cases).match(toMatch);
    }

    public <O> Option<Option<O>> wrap(Case<I, Option<O>>... cases) {
        Option<Option<O>> result;
        if ((result = option(cases)).isSome()) {
            Option<O> temp = result.get();
            return temp.isSome() ? result : Option.nothing();
        }
        return Option.nothing();
    }

    public <O> O of(Case<I, O>... cases) {
        Option<O> result;
        if ((result = option(cases)).isSome()) {
            return result.get();
        }
        throw new RuntimeException("No Match Made");
    }
}


