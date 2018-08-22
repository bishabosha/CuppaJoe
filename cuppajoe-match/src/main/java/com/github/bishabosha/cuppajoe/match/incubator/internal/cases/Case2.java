package com.github.bishabosha.cuppajoe.match.incubator.internal.cases;

import com.github.bishabosha.cuppajoe.control.Option;
import com.github.bishabosha.cuppajoe.higher.functions.Func1;
import com.github.bishabosha.cuppajoe.match.incubator.Case;
import com.github.bishabosha.cuppajoe.match.incubator.internal.extract.Extract1;

import static com.github.bishabosha.cuppajoe.API.None;
import static com.github.bishabosha.cuppajoe.API.Some;

public final class Case1<I, A, O> extends Extract1<I, A> implements Case<I, O> {
    private final Func1<A, O> func1;

    public Case1(Func1<A, O> func1) {
        this.func1 = func1;
    }

    @Override
    public Option<O> match(I input) {
        return matcher.test(input) ? Some(func1.apply(path1.get(input))) : None();
    }
}
