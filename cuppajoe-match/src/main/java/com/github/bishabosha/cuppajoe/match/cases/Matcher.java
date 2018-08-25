package com.github.bishabosha.cuppajoe.match.cases;

import com.github.bishabosha.cuppajoe.control.Option;
import com.github.bishabosha.cuppajoe.match.cases.Case.CombinatorCase;

public final class Matcher<I> {
    private final I input;

    public static <I> Matcher<I> of(I input) {
        return new Matcher<>(input);
    }

    private Matcher(I input) {
        this.input = input;
    }

    public final <O> O get(Case<I, O> single) {
        return single.get(input);
    }

    public final <O> Option<O> match(Case<I, O> single) {
        return single.match(input);
    }

    @SafeVarargs
    public final <O> O get(CombinatorCase<I, O>... many) {
        return Case.combine(many).get(input);
    }

    @SafeVarargs
    public final <O> Option<O> match(CombinatorCase<I, O>... many) {
        return Case.combine(many).match(input);
    }
}
