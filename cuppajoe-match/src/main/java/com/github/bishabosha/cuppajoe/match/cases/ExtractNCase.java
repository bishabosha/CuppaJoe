package com.github.bishabosha.cuppajoe.match.cases;

import com.github.bishabosha.cuppajoe.control.Option;
import com.github.bishabosha.cuppajoe.match.MatchException;
import com.github.bishabosha.cuppajoe.match.Path;
import com.github.bishabosha.cuppajoe.match.cases.Case.CombinatorCase;
import com.github.bishabosha.cuppajoe.match.internal.extract.ExtractN;
import com.github.bishabosha.cuppajoe.match.internal.extract.Extractors;
import com.github.bishabosha.cuppajoe.match.patterns.Pattern;

import java.util.function.Predicate;

public abstract class ExtractNCase<I, O> extends CombinatorCase<I, O> {

    private final Predicate<Object> matcher;
    protected final Path[] paths;

    protected ExtractNCase(Pattern<I> pattern, int n) {
        var extractN = Extractors.compile(pattern, new ExtractN(n));
        matcher = extractN.matcher();
        paths = extractN.getPaths();
    }

    @Override
    public O get(I input) {
        if (matcher.test(input)) {
            return extract(input);
        }
        throw new MatchException(input);
    }

    @Override
    public Option<O> match(I input) {
        return matcher.test(input) ? Option.some(extract(input)) : Option.empty();
    }

    @Override
    public boolean matches(I input) {
        return matcher.test(input);
    }
}
