/*
 * Copyright (c) 2017. Jamie Thompson <bishbashboshjt@gmail.com>
 */

package com.bishabosha.caffeine.functional;

import com.bishabosha.caffeine.functional.functions.*;

import java.util.function.*;

/**
 * Represents a Case of a Matcher block,
 * Typically represents a pattern that a variable is matched on, and a mapping function for any variables bound
 * @param <I> the input class being matched
 * @param <O> the output class for if a match is made.
 */
public interface Case<I, O> {

    /**
     * Attempts to match the Object and Map it
     * @param input the Object being matched
     * @return {@link Option#nothing()} if no match is made. Otherwise {@link Option} of the matched variable
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
     * @return A Guard that will supply {@link Option#nothing()} if the test fails. Otherwise {@link Option} of the supplied variable
     */
    static <O> Guard<O> when(BooleanSupplier test, Func0<O> valueSupplier) {
        return () -> test.getAsBoolean() ? Option.of(valueSupplier.apply()) : Option.nothing();
    }

    /**
     * Guard that will lazily wrap the value supplied in an Option.<br>
     * Used as a catch all result in a guard block.
     * @param valueSupplier supplies the value given if this edge guard is evaluated
     * @param <O> The type of the output variable
     * @return a Guard that will supply {@link Option} of the supplied value.
     */
    static <O> Guard<O> edge(Func0<O> valueSupplier) {
        return () -> Option.of(valueSupplier.apply());
    }

    /**
     * Guard that will throw a {@link RuntimeException} of the supplied throwable if the test is positive.
     * @param test Supply any test to check, for example an index out of bounds.
     * @param toThrow A Throwable error that will be thrown if the test passes
     * @param <O> The expected type for the guard block
     * @return A Guard that will throw the throwable if the test passes,
     * otherwise will supply {@link Option#nothing()}
     * @throws RuntimeException if the test passes.
     */
    static <O> Guard<O> thro(BooleanSupplier test, Supplier<Throwable> toThrow) {
        return () -> {
            if (test.getAsBoolean()) {
                throw new RuntimeException(toThrow.get());
            }
            return Option.nothing();
        };
    }

    /**
     * Case that will throw a {@link RuntimeException} of the supplied throwable if the pattern matches.
     * @param matcher A Pattern to match on the input variable
     * @param toThrow A Throwable error that will be thrown if the pattern matches
     * @param <I> The input type of the Case
     * @param <O> The expected output type of the case block
     * @return {@link Option#nothing()} if the pattern does not match
     * @throws RuntimeException if the pattern matches
     */
    static <I, O> Case<I, O> thro(Pattern matcher, Supplier<Throwable> toThrow) {
        return x -> {
            if (matcher.test(x).isSome()) {
                throw new RuntimeException(toThrow.get());
            }
            return Option.nothing();
        };
    }

    static <I, O> Case<I, O>
    with(Pattern matcher, Func0<O> binder) {
        return base(matcher, t -> binder.apply());
    }

    static <I, O, A> Case<I, O>
    with(Pattern matcher, Func1<A, O> binder) {
        return base(matcher, l -> binder.apply((A) l.get(0)));
    }

    static <I, O, A, B> Case<I, O>
    with(Pattern matcher, Func2<A, B, O> binder) {
        return base(matcher, l ->
            binder.apply((A) l.get(0), (B) l.get(1)));
    }

    static <I, O, A, B, C> Case<I, O>
    with(Pattern matcher, Func3<A, B, C, O> binder) {
        return base(matcher, l ->
            binder.apply((A) l.get(0), (B) l.get(1), (C) l.get(2)));
    }

    static <I, O, A, B, C, D> Case<I, O>
    with(Pattern matcher, Func4<A, B, C, D, O> binder) {
        return base(matcher, l ->
            binder.apply((A) l.get(0), (B) l.get(1), (C) l.get(2),
                (D) l.get(3)));
    }

    static <I, O, A, B, C, D, E> Case<I, O>
    with(Pattern matcher, Func5<A, B, C, D, E, O> binder) {
        return base(matcher, l ->
            binder.apply((A) l.get(0), (B) l.get(1), (C) l.get(2),
                (D) l.get(3), (E) l.get(4)));
    }

    static <I, O, A, B, C, D, E, F> Case<I, O>
    with(Pattern matcher, Func6<A, B, C, D, E, F, O> binder) {
        return base(matcher, l ->
            binder.apply((A) l.get(0), (B) l.get(1), (C) l.get(2),
                (D) l.get(3), (E) l.get(4), (F) l.get(5)));
    }

    static <I, O, A, B, C, D, E, F, G> Case<I, O>
    with(Pattern matcher, Func7<A, B, C, D, E, F, G, O> binder) {
        return base(matcher, l ->
            binder.apply((A) l.get(0), (B) l.get(1), (C) l.get(2),
                (D) l.get(3), (E) l.get(4), (F) l.get(5), (G) l.get(6)));
    }

    static <I, O, A, B, C, D, E, F, G, H> Case<I, O>
    with(Pattern matcher, Func8<A, B, C, D, E, F, G, H, O> binder) {
        return base(matcher, l ->
            binder.apply((A) l.get(0), (B) l.get(1), (C) l.get(2),
                (D) l.get(3), (E) l.get(4), (F) l.get(5), (G) l.get(6),
                (H) l.get(7)));
    }

    static <I, O> Case<I, O> base(Pattern matcher, Function<PatternResult, O> mapper) {
        return i -> {
            Option<PatternResult> option = matcher.test(i);
            if (option.isSome()) {
                PatternResult result = option.get();
                try {
                    return Option.ofUnknown(mapper.apply(result));
                } catch (IndexOutOfBoundsException e) {
                    e.printStackTrace();
                    throw new IllegalArgumentException("Not enough variables yielded to bind the given function.");
                }
            }
            return Option.nothing();
        };
    }

    static <O> Guard<O> combine(Guard<O>... guards) {
        return () -> {
            Option<O> result = Option.nothing();
            for (Guard<O> guard: guards) {
                result = guard.match();
                if (result.isSome()) {
                    break;
                }
            }
            return result;
        };
    }

    static <I, O> Case<I, O> combine(Case<I, O>... cases) {
        return i -> {
            Option<O> result = Option.nothing();
            for (Case<I, O> c: cases) {
                result = c.match(i);
                if (result.isSome()) {
                    break;
                }
            }
            return result;
        };
    }
}
