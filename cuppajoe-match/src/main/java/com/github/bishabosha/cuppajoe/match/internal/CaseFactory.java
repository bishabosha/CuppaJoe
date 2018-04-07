package com.github.bishabosha.cuppajoe.match.internal;

import com.github.bishabosha.cuppajoe.control.Option;
import com.github.bishabosha.cuppajoe.functions.*;
import com.github.bishabosha.cuppajoe.match.Case;
import com.github.bishabosha.cuppajoe.match.patterns.Pattern;
import com.github.bishabosha.cuppajoe.match.patterns.Result;

import java.util.function.Function;
import java.util.function.Supplier;

import static com.github.bishabosha.cuppajoe.API.None;

public final class CaseFactory {

    private CaseFactory() {
    }

    public static <I, O> Case<I, O>
    with(Pattern<I> matcher, Supplier<O> binder) {
        return base(matcher, xs ->
            binder.get());
    }

    public static <I, O, A> Case<I, O>
    with(Pattern<I> matcher, Func1<A, O> binder) {
        return base(matcher, xs ->
            binder.apply(xs.nextVal()));
    }

    public static <I, O, A, B> Case<I, O>
    with(Pattern<I> matcher, Func2<A, B, O> binder) {
        return base(matcher, xs ->
            binder.apply(xs.nextVal(), xs.nextVal()));
    }

    public static <I, O, A, B, C> Case<I, O>
    with(Pattern<I> matcher, Func3<A, B, C, O> binder) {
        return base(matcher, xs ->
            binder.apply(xs.nextVal(), xs.nextVal(), xs.nextVal()));
    }

    public static <I, O, A, B, C, D> Case<I, O>
    with(Pattern<I> matcher, Func4<A, B, C, D, O> binder) {
        return base(matcher, xs ->
            binder.apply(xs.nextVal(), xs.nextVal(), xs.nextVal(), xs.nextVal()));
    }

    public static <I, O, A, B, C, D, E> Case<I, O>
    with(Pattern<I> matcher, Func5<A, B, C, D, E, O> binder) {
        return base(matcher, xs ->
            binder.apply(xs.nextVal(), xs.nextVal(), xs.nextVal(), xs.nextVal(), xs.nextVal()));
    }

    public static <I, O, A, B, C, D, E, F> Case<I, O>
    with(Pattern<I> matcher, Func6<A, B, C, D, E, F, O> binder) {
        return base(matcher, xs ->
            binder.apply(xs.nextVal(), xs.nextVal(), xs.nextVal(), xs.nextVal(), xs.nextVal(), xs.nextVal()));
    }

    public static <I, O, A, B, C, D, E, F, G> Case<I, O>
    with(Pattern<I> matcher, Func7<A, B, C, D, E, F, G, O> binder) {
        return base(matcher, xs ->
            binder.apply(xs.nextVal(), xs.nextVal(), xs.nextVal(), xs.nextVal(), xs.nextVal(), xs.nextVal(), xs.nextVal()));
    }

    public static <I, O, A, B, C, D, E, F, G, H> Case<I, O>
    with(Pattern<I> matcher, Func8<A, B, C, D, E, F, G, H, O> binder) {
        return base(matcher, xs ->
            binder.apply(xs.nextVal(), xs.nextVal(), xs.nextVal(), xs.nextVal(), xs.nextVal(), xs.nextVal(), xs.nextVal(), xs.nextVal()));
    }

    private static <I, O> Case<I, O> base(Pattern<I> matcher, Function<Result.Values, O> mapper) {
        return i ->
            matcher.test(i)
                   .map(result ->
                       mapper.apply(result.values()));
    }

    @SafeVarargs
    public static <I, O> Case<I, O> combine(Case<I, O>... cases) {
        return i -> {
            Option<O> result = None();
            for (var c : cases) {
                result = c.match(i);
                if (!result.isEmpty()) {
                    break;
                }
            }
            return result;
        };
    }
}
