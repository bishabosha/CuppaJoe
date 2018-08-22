package com.github.bishabosha.cuppajoe.match.incubator.internal.cases;

import com.github.bishabosha.cuppajoe.control.Option;
import com.github.bishabosha.cuppajoe.higher.functions.Func0;
import com.github.bishabosha.cuppajoe.match.incubator.Case;
import com.github.bishabosha.cuppajoe.match.incubator.internal.extract.Extract0;

import static com.github.bishabosha.cuppajoe.API.None;
import static com.github.bishabosha.cuppajoe.API.Some;

public final class Case0Lazy<I, O> extends Extract0<I> implements Case<I, O> {
    private final Func0<O> func0;

    public Case0Lazy(Func0<O> func0) {
        this.func0 = func0;
    }

    @Override
    public Option<O> match(I input) {
        return matcher.test(input) ? Some(func0.get()) : None();
    }
}
