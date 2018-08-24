package com.github.bishabosha.cuppajoe.match.incubator.cases;

import com.github.bishabosha.cuppajoe.control.Option;
import com.github.bishabosha.cuppajoe.match.incubator.cases.Case.CombinatorCase;
import com.github.bishabosha.cuppajoe.match.incubator.MatchException;
import com.github.bishabosha.cuppajoe.match.incubator.internal.extract.ExtractN;

public abstract class ExtractNCase<I, O> extends ExtractN implements CombinatorCase<I, O> {

    protected ExtractNCase(int size) {
        super(size);
    }

    @Override
    public O get(I input) throws MatchException {
        if (matcher().test(input)) {
            return extract(input);
        }
        throw new MatchException(input);
    }

    @Override
    public Option<O> match(I input) {
        return matcher().test(input) ? Option.some(extract(input)) : Option.empty();
    }

    @Override
    public boolean matches(I input) {
        return matcher().test(input);
    }
}
