package com.github.bishabosha.cuppajoe.match.incubator;

import com.github.bishabosha.cuppajoe.collections.immutable.tuples.Tuple2;
import com.github.bishabosha.cuppajoe.control.Option;

public class Patterns {

    public static <O> Pattern<O> $() {
        return Pattern::bind;
    }

    public static <O> Pattern<O> __() {
        return x -> Pattern.PASS;
    }

    public static <O> Pattern<Option<O>> Some$(Pattern<O> value) {
        return PatternFactory.unapply1(Option.Some.class, value);
    }

    public static <O> Pattern<Option<O>> None$() {
        return PatternFactory.unapply0(Option.None.INSTANCE);
    }

    public static <A, B> Pattern<Tuple2<A, B>> Tuple2$(Pattern<A> $1, Pattern<B> $2) {
        return PatternFactory.unapply2(Tuple2.class, $1, $2);
    }
}
