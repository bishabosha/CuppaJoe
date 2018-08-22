package com.github.bishabosha.cuppajoe.match.incubator;

import com.github.bishabosha.cuppajoe.higher.functions.Func0;
import com.github.bishabosha.cuppajoe.higher.functions.Func1;
import com.github.bishabosha.cuppajoe.match.incubator.internal.cases.Case0Lazy;
import com.github.bishabosha.cuppajoe.match.incubator.internal.cases.Case0Strict;
import com.github.bishabosha.cuppajoe.match.incubator.internal.cases.Case1;
import com.github.bishabosha.cuppajoe.match.incubator.patterns.Pattern;

import static com.github.bishabosha.cuppajoe.match.incubator.internal.extract.Extractors.compile;

public class API {

    private API() {
    }

    public static <I, O> Case<I, O> With(Pattern<I> pattern, O value) {
        return compile(pattern, new Case0Strict<>(value));
    }

    public static <I, O> Case<I, O> With(Pattern<I> pattern, Func0<O> func0) {
        return compile(pattern, new Case0Lazy<>(func0));
    }

    public static <I, A, O> Case<I, O> With(Pattern<I> pattern, Func1<A, O> func1) {
        return compile(pattern, new Case1<>(func1));
    }
}
