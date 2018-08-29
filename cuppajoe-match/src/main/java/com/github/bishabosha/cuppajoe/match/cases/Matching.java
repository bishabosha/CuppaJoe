package com.github.bishabosha.cuppajoe.match.cases;

import com.github.bishabosha.cuppajoe.collections.immutable.List;
import com.github.bishabosha.cuppajoe.higher.functions.*;
import com.github.bishabosha.cuppajoe.match.cases.Case.CombinatorCase;
import com.github.bishabosha.cuppajoe.match.patterns.Pattern;

import static com.github.bishabosha.cuppajoe.match.API.Default;
import static com.github.bishabosha.cuppajoe.match.API.With;

public final class Matching<I, O> {
    private final List<CombinatorCase<I, O>> cases;

    public static <I, O> Matching<I, O> toCase() {
        return Matching.of(List.empty());
    }

    public static <I, O> Case<I, O> eval(Matching<I, O> matching) {
        return Case.combine(matching.cases);
    }

    public static <I, O> Matching<I, O> of(List<CombinatorCase<I, O>> cases) {
        return new Matching<>(cases);
    }

    private Matching(List<CombinatorCase<I, O>> cases) {
        this.cases = cases;
    }

    public final OngoingMatching<I, O> with(Pattern<I> pattern) {
        return new OngoingMatching<>(pattern, cases);
    }

    public final Case<I, O> def(O value) {
        return Case.combine(cases.push(Default(value)));
    }

    public final Case<I, O> def(Func0<O> func) {
        return Case.combine(cases.push(Default(func)));
    }

    public final Case<I, O> def(Func1<I, O> func) {
        return Case.combine(cases.push(Default(func)));
    }

    public static final class OngoingMatching<I, O> {
        private final Pattern<I> pattern;
        private final List<CombinatorCase<I, O>> cases;

        private OngoingMatching(Pattern<I> pattern, List<CombinatorCase<I, O>> cases) {
            this.pattern = pattern;
            this.cases = cases;
        }

        public final Matching<I, O> then(O value) {
            return new Matching<>(cases.push(With(pattern, value)));
        }

        public final Matching<I, O> then(Func0<O> func) {
            return new Matching<>(cases.push(With(pattern, func)));
        }

        public final <A> Matching<I, O> then(Func1<A, O> func) {
            return new Matching<>(cases.push(With(pattern, func)));
        }

        public final <A, B> Matching<I, O> then(Func2<A, B, O> func) {
            return new Matching<>(cases.push(With(pattern, func)));
        }

        public final <A, B, C> Matching<I, O> then(Func3<A, B, C, O> func) {
            return new Matching<>(cases.push(With(pattern, func)));
        }

        public final <A, B, C, D> Matching<I, O> then(Func4<A, B, C, D, O> func) {
            return new Matching<>(cases.push(With(pattern, func)));
        }

        public final <A, B, C, D, E> Matching<I, O> then(Func5<A, B, C, D, E, O> func) {
            return new Matching<>(cases.push(With(pattern, func)));
        }

        public final <A, B, C, D, E, F> Matching<I, O> then(Func6<A, B, C, D, E, F, O> func) {
            return new Matching<>(cases.push(With(pattern, func)));
        }

        public final <A, B, C, D, E, F, G> Matching<I, O> then(Func7<A, B, C, D, E, F, G, O> func) {
            return new Matching<>(cases.push(With(pattern, func)));
        }

        public final <A, B, C, D, E, F, G, H> Matching<I, O> then(Func8<A, B, C, D, E, F, G, H, O> func) {
            return new Matching<>(cases.push(With(pattern, func)));
        }
    }
}
