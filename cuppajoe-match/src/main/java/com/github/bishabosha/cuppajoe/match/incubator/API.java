package com.github.bishabosha.cuppajoe.match.incubator;

import com.github.bishabosha.cuppajoe.higher.functions.*;
import com.github.bishabosha.cuppajoe.match.incubator.cases.Case.CombinatorCase;
import com.github.bishabosha.cuppajoe.match.incubator.cases.ExtractNCase;
import com.github.bishabosha.cuppajoe.match.incubator.internal.extract.Extractors;
import com.github.bishabosha.cuppajoe.match.incubator.patterns.Pattern;

public class API {

    private API() {
    }

    public static <I, O> CombinatorCase<I, O> With(Pattern<I> pattern, O value) {
        return Extractors.compile(pattern, new ExtractNCase<>(0) {
            @Override
            public final O extract(I input) {
                return value;
            }
        });
    }

    public static <I, O> CombinatorCase<I, O> With(Pattern<I> pattern, Func0<O> func) {
        return Extractors.compile(pattern, new ExtractNCase<>(0) {
            @Override
            public final O extract(I input) {
                return func.apply();
            }
        });
    }

    public static <I, A, O> CombinatorCase<I, O> With(Pattern<I> pattern, Func1<A, O> func) {
        return Extractors.compile(pattern, new ExtractNCase<>(1) {
            @SuppressWarnings("unchecked")
            @Override
            public final O extract(I input) {
                return func.apply(
                    ((Path<I, A>)paths[0]).get(input));
            }
        });
    }

    public static <I, A, B, O> CombinatorCase<I, O> With(Pattern<I> pattern, Func2<A, B, O> func) {
        return Extractors.compile(pattern, new ExtractNCase<>(2) {
            @SuppressWarnings("unchecked")
            @Override
            public final O extract(I input) {
                return func.apply(
                    ((Path<I, A>)paths[0]).get(input),
                    ((Path<I, B>)paths[1]).get(input));
            }
        });
    }

    public static <I, A, B, C, O> CombinatorCase<I, O> With(Pattern<I> pattern, Func3<A, B, C, O> func) {
        return Extractors.compile(pattern, new ExtractNCase<>(3) {
            @SuppressWarnings("unchecked")
            @Override
            public final O extract(I input) {
                return func.apply(
                    ((Path<I, A>)paths[0]).get(input),
                    ((Path<I, B>)paths[1]).get(input),
                    ((Path<I, C>)paths[2]).get(input));
            }
        });
    }

    public static <I, A, B, C, D, O> CombinatorCase<I, O> With(Pattern<I> pattern, Func4<A, B, C, D, O> func) {
        return Extractors.compile(pattern, new ExtractNCase<>(4) {
            @SuppressWarnings("unchecked")
            @Override
            public final O extract(I input) {
                return func.apply(
                    ((Path<I, A>)paths[0]).get(input),
                    ((Path<I, B>)paths[1]).get(input),
                    ((Path<I, C>)paths[2]).get(input),
                    ((Path<I, D>)paths[3]).get(input));
            }
        });
    }

    public static <I, A, B, C, D, E, O> CombinatorCase<I, O> With(Pattern<I> pattern, Func5<A, B, C, D, E, O> func) {
        return Extractors.compile(pattern, new ExtractNCase<>(5) {
            @SuppressWarnings("unchecked")
            @Override
            public final O extract(I input) {
                return func.apply(
                    ((Path<I, A>)paths[0]).get(input),
                    ((Path<I, B>)paths[1]).get(input),
                    ((Path<I, C>)paths[2]).get(input),
                    ((Path<I, D>)paths[3]).get(input),
                    ((Path<I, E>)paths[4]).get(input));
            }
        });
    }

    public static <I, A, B, C, D, E, F, O> CombinatorCase<I, O> With(Pattern<I> pattern, Func6<A, B, C, D, E, F, O> func) {
        return Extractors.compile(pattern, new ExtractNCase<>(6) {
            @SuppressWarnings("unchecked")
            @Override
            public final O extract(I input) {
                return func.apply(
                    ((Path<I, A>)paths[0]).get(input),
                    ((Path<I, B>)paths[1]).get(input),
                    ((Path<I, C>)paths[2]).get(input),
                    ((Path<I, D>)paths[3]).get(input),
                    ((Path<I, E>)paths[4]).get(input),
                    ((Path<I, F>)paths[5]).get(input));
            }
        });
    }

    public static <I, A, B, C, D, E, F, G, O> CombinatorCase<I, O> With(Pattern<I> pattern, Func7<A, B, C, D, E, F, G, O> func) {
        return Extractors.compile(pattern, new ExtractNCase<>(7) {
            @SuppressWarnings("unchecked")
            @Override
            public final O extract(I input) {
                return func.apply(
                    ((Path<I, A>)paths[0]).get(input),
                    ((Path<I, B>)paths[1]).get(input),
                    ((Path<I, C>)paths[2]).get(input),
                    ((Path<I, D>)paths[3]).get(input),
                    ((Path<I, E>)paths[4]).get(input),
                    ((Path<I, F>)paths[5]).get(input),
                    ((Path<I, G>)paths[6]).get(input));
            }
        });
    }

    public static <I, A, B, C, D, E, F, G, H, O> CombinatorCase<I, O> With(Pattern<I> pattern, Func8<A, B, C, D, E, F, G, H, O> func) {
        return Extractors.compile(pattern, new ExtractNCase<>(8) {
            @SuppressWarnings("unchecked")
            @Override
            public final O extract(I input) {
                return func.apply(
                    ((Path<I, A>)paths[0]).get(input),
                    ((Path<I, B>)paths[1]).get(input),
                    ((Path<I, C>)paths[2]).get(input),
                    ((Path<I, D>)paths[3]).get(input),
                    ((Path<I, E>)paths[4]).get(input),
                    ((Path<I, F>)paths[5]).get(input),
                    ((Path<I, G>)paths[6]).get(input),
                    ((Path<I, H>)paths[7]).get(input));
            }
        });
    }
}
