package com.github.bishabosha.cuppajoe.match.incubator.internal.cases;

import com.github.bishabosha.cuppajoe.control.Option;
import com.github.bishabosha.cuppajoe.match.incubator.Case;
import com.github.bishabosha.cuppajoe.match.incubator.internal.extract.Extract0;
import com.github.bishabosha.cuppajoe.match.incubator.patterns.Pattern;

import static com.github.bishabosha.cuppajoe.API.None;
import static com.github.bishabosha.cuppajoe.API.Some;

public final class Case0Strict<I, O> extends Extract0<I> implements Case<I, O> {
    private final O val;

    public Case0Strict(O val) {
        this.val = val;
    }

    @Override
    public Option<O> match(I input) {
        return matcher.test(input) ? Some(val) : None();
    }
}
