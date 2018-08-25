package com.github.bishabosha.cuppajoe.match;

import com.github.bishabosha.cuppajoe.annotation.NonNull;
import com.github.bishabosha.cuppajoe.control.Option;
import com.github.bishabosha.cuppajoe.higher.functions.*;
import com.github.bishabosha.cuppajoe.match.cases.Case;
import com.github.bishabosha.cuppajoe.match.cases.Case.CombinatorCase;
import com.github.bishabosha.cuppajoe.match.cases.ExtractNCase;
import com.github.bishabosha.cuppajoe.match.cases.Guard;
import com.github.bishabosha.cuppajoe.match.cases.Matcher;
import com.github.bishabosha.cuppajoe.match.internal.guards.GuardFactory;
import com.github.bishabosha.cuppajoe.match.patterns.Pattern;

import java.util.function.BooleanSupplier;
import java.util.function.Supplier;

public final class API {

    private API () {
    }

    public static <I> Matcher<I> Match(I input) {
        return Matcher.of(input);
    }

    @SafeVarargs
    public static <O> O IfUnsafe(@NonNull Guard<O>... guards) {
        return Guards(guards).get();
    }

    @SafeVarargs
    public static <O> Option<O> If(@NonNull Guard<O>... guards) {
        return Guards(guards).match();
    }

    @SafeVarargs
    public static <O> Guard<O> Guards(@NonNull Guard<O>... guards) {
        return GuardFactory.combine(guards);
    }

    public static <O> Guard<O> When(@NonNull BooleanSupplier test, @NonNull Func0<O> valueSupplier) {
        return GuardFactory.when(test, valueSupplier);
    }

    public static <O> Guard<O> When(@NonNull BooleanSupplier test, @NonNull O value) {
        return GuardFactory.when(test, value);
    }

    public static <O> Guard<O> Edge(@NonNull Supplier<O> valueSupplier) {
        return GuardFactory.edge(valueSupplier);
    }

    public static <O> Guard<O> Edge(@NonNull O value) {
        return GuardFactory.edge(value);
    }

    @SafeVarargs
    public static <I, O> Case<I, O> Cases(@NonNull CombinatorCase<I, O>... cases) {
        return Case.combine(cases);
    }

    public static <I, O> CombinatorCase<I, O> With(Pattern<I> pattern, O value) {
        return new ExtractNCase<>(pattern, 0) {
            @Override
            public O extract(I input) {
                return value;
            }
        };
    }

    public static <I, O> CombinatorCase<I, O> With(Pattern<I> pattern, Func0<O> func) {
        return new ExtractNCase<>(pattern, 0) {
            @Override
            public final O extract(I input) {
                return func.apply();
            }
        };
    }

    public static <I, A, O> CombinatorCase<I, O> With(Pattern<I> pattern, Func1<A, O> func) {
        return new ExtractNCase<>(pattern, 1) {
            @SuppressWarnings("unchecked")
            @Override
            public final O extract(I input) {
                return func.apply(
                    ((Path<I, A>)paths[0]).get(input)
                );
            }
        };
    }

    public static <I, A, B, O> CombinatorCase<I, O> With(Pattern<I> pattern, Func2<A, B, O> func) {
        return new ExtractNCase<>(pattern, 2) {
            @SuppressWarnings("unchecked")
            @Override
            public final O extract(I input) {
                return func.apply(
                    ((Path<I, A>)paths[0]).get(input),
                    ((Path<I, B>)paths[1]).get(input)
                );
            }
        };
    }

    public static <I, A, B, C, O> CombinatorCase<I, O> With(Pattern<I> pattern, Func3<A, B, C, O> func) {
        return new ExtractNCase<>(pattern, 3) {
            @SuppressWarnings("unchecked")
            @Override
            public final O extract(I input) {
                return func.apply(
                    ((Path<I, A>)paths[0]).get(input),
                    ((Path<I, B>)paths[1]).get(input),
                    ((Path<I, C>)paths[2]).get(input)
                );
            }
        };
    }

    public static <I, A, B, C, D, O> CombinatorCase<I, O> With(Pattern<I> pattern, Func4<A, B, C, D, O> func) {
        return new ExtractNCase<>(pattern, 4) {
            @SuppressWarnings("unchecked")
            @Override
            public final O extract(I input) {
                return func.apply(
                    ((Path<I, A>)paths[0]).get(input),
                    ((Path<I, B>)paths[1]).get(input),
                    ((Path<I, C>)paths[2]).get(input),
                    ((Path<I, D>)paths[3]).get(input)
                );
            }
        };
    }

    public static <I, A, B, C, D, E, O> CombinatorCase<I, O> With(Pattern<I> pattern, Func5<A, B, C, D, E, O> func) {
        return new ExtractNCase<>(pattern, 5) {
            @SuppressWarnings("unchecked")
            @Override
            public final O extract(I input) {
                return func.apply(
                    ((Path<I, A>)paths[0]).get(input),
                    ((Path<I, B>)paths[1]).get(input),
                    ((Path<I, C>)paths[2]).get(input),
                    ((Path<I, D>)paths[3]).get(input),
                    ((Path<I, E>)paths[4]).get(input)
                );
            }
        };
    }

    public static <I, A, B, C, D, E, F, O> CombinatorCase<I, O> With(Pattern<I> pattern, Func6<A, B, C, D, E, F, O> func) {
        return new ExtractNCase<>(pattern, 6) {
            @SuppressWarnings("unchecked")
            @Override
            public final O extract(I input) {
                return func.apply(
                    ((Path<I, A>)paths[0]).get(input),
                    ((Path<I, B>)paths[1]).get(input),
                    ((Path<I, C>)paths[2]).get(input),
                    ((Path<I, D>)paths[3]).get(input),
                    ((Path<I, E>)paths[4]).get(input),
                    ((Path<I, F>)paths[5]).get(input)
                );
            }
        };
    }

    public static <I, A, B, C, D, E, F, G, O> CombinatorCase<I, O> With(Pattern<I> pattern, Func7<A, B, C, D, E, F, G, O> func) {
        return new ExtractNCase<>(pattern, 7) {
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
                    ((Path<I, G>)paths[6]).get(input)
                );
            }
        };
    }

    public static <I, A, B, C, D, E, F, G, H, O> CombinatorCase<I, O> With(Pattern<I> pattern, Func8<A, B, C, D, E, F, G, H, O> func) {
        return new ExtractNCase<>(pattern, 8) {
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
                    ((Path<I, H>)paths[7]).get(input)
                );
            }
        };
    }
}
