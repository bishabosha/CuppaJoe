/*
 * Copyright (c) 2017. Jamie Thompson <bishbashboshjt@gmail.com>
 */

package com.bishabosha.cuppajoe.patterns;

import com.bishabosha.cuppajoe.API;
import com.bishabosha.cuppajoe.control.Option;
import com.bishabosha.cuppajoe.functions.*;

import java.util.function.*;

import static com.bishabosha.cuppajoe.API.Nothing;
import static com.bishabosha.cuppajoe.API.Option;

/**
 * Represents a Case of a Matcher block,
 * Typically represents a pattern that a variable is matched on, and a mapping function for any variables bound
 * @param <I> the input class being matched
 * @param <O> the output class for if a of is made.
 */
public interface Case<I, O> {

    /**
     * Attempts to of the Object and Map it
     * @param input the Object being matched
     * @return {@link API#Nothing()} if no of is made. Otherwise {@link Option} of the matched variable
     */
    Option<O> match(I input);

    /**
     * Similar to a Case, but only has to optionally supply a value. Guards are different to Cases,
     * as there is no input variable, Guards rely on closures to evaluate application state and supply a result.
     * @param <O> the output Type
     */
    interface Guard<O> {
        /**
         * User defines guards that supply an optional of a value.
         */
        Option<O> match();
    }

    /**
     * Guard that lazily evaluates a condition, and if true, will lazily supply the result given
     * @param test Supply any test to check, for example comparing for equality
     * @param valueSupplier The value to supply if the test is a success.
     * @param <O> The type of the output variable
     * @return A Guard that will supply {@link API#Nothing()} if the test fails. Otherwise {@link Option} of the supplied variable
     */
    static <O> Guard<O> when(BooleanSupplier test, Func0<O> valueSupplier) {
        return () -> Option(test, valueSupplier);
    }

    /**
     * Guard that will lazily wrap the value supplied in an Option.<br>
     * Used as a catch all result in a guard block.
     * @param valueSupplier supplies the value given if this edge guard is evaluated
     * @param <O> The type of the output variable
     * @return a Guard that will supply {@link Option} of the supplied value.
     */
    static <O> Guard<O> edge(Supplier<O> valueSupplier) {
        return () -> Option.of(valueSupplier.get());
    }

    static <I, O> Case<I, O>
    with(Pattern matcher, Supplier<O> binder) {
        return base(matcher, t -> binder.get());
    }

    static <I, O, A> Case<I, O>
    with(Pattern matcher, Func1<A, O> binder) {
        return base(matcher, l -> binder.apply(l.get(0)));
    }

    static <I, O, A, B> Case<I, O>
    with(Pattern matcher, Func2<A, B, O> binder) {
        return base(matcher, l ->
            binder.apply(l.get(0), l.get(1)));
    }

    static <I, O, A, B, C> Case<I, O>
    with(Pattern matcher, Func3<A, B, C, O> binder) {
        return base(matcher, l ->
            binder.apply(l.get(0), l.get(1), l.get(2)));
    }

    static <I, O, A, B, C, D> Case<I, O>
    with(Pattern matcher, Func4<A, B, C, D, O> binder) {
        return base(matcher, l ->
            binder.apply(l.get(0), l.get(1), l.get(2),
                    l.get(3)));
    }

    static <I, O, A, B, C, D, E> Case<I, O>
    with(Pattern matcher, Func5<A, B, C, D, E, O> binder) {
        return base(matcher, l ->
            binder.apply(l.get(0), l.get(1), l.get(2),
                    l.get(3), l.get(4)));
    }

    static <I, O, A, B, C, D, E, F> Case<I, O>
    with(Pattern matcher, Func6<A, B, C, D, E, F, O> binder) {
        return base(matcher, l ->
            binder.apply(l.get(0), l.get(1), l.get(2),
                    l.get(3), l.get(4), l.get(5)));
    }

    static <I, O, A, B, C, D, E, F, G> Case<I, O>
    with(Pattern matcher, Func7<A, B, C, D, E, F, G, O> binder) {
        return base(matcher, l ->
            binder.apply(l.get(0), l.get(1), l.get(2),
                    l.get(3), l.get(4), l.get(5), l.get(6)));
    }

    static <I, O, A, B, C, D, E, F, G, H> Case<I, O>
    with(Pattern matcher, Func8<A, B, C, D, E, F, G, H, O> binder) {
        return base(matcher, l ->
            binder.apply(l.get(0), l.get(1), l.get(2),
                    l.get(3), l.get(4), l.get(5), l.get(6),
                    l.get(7)));
    }

    static <I, O> Case<I, O> base(Pattern matcher, Function<PatternResult, O> mapper) {
        return i -> {
            Option<PatternResult> option = matcher.test(i);
            if (option.isEmpty()) {
                return Nothing();
            }
            PatternResult result = option.get();
            return Option(mapper.apply(result));
        };
    }

    static <O> Guard<O> combine(Guard<O>... guards) {
        return () -> {
            Option<O> result = Nothing();
            for (Guard<O> guard: guards) {
                result = guard.match();
                if (!result.isEmpty()) {
                    break;
                }
            }
            return result;
        };
    }

    static <I, O> Case<I, O> combine(Case<I, O>... cases) {
        return i -> {
            Option<O> result = Nothing();
            for (Case<I, O> c: cases) {
                result = c.match(i);
                if (!result.isEmpty()) {
                    break;
                }
            }
            return result;
        };
    }
}
