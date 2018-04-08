package com.github.bishabosha.cuppajoe.match.patterns;

import com.github.bishabosha.cuppajoe.control.Either;
import com.github.bishabosha.cuppajoe.control.Either.Left;
import com.github.bishabosha.cuppajoe.control.Either.Right;
import com.github.bishabosha.cuppajoe.control.Lazy;
import com.github.bishabosha.cuppajoe.control.Option;
import com.github.bishabosha.cuppajoe.control.Try;
import com.github.bishabosha.cuppajoe.tuples.*;

import java.util.Objects;

import static com.github.bishabosha.cuppajoe.match.API.PatternFor;

public final class Standard {

    public static <O> Pattern<O> __() {
        return x -> Pattern.PASS;
    }

    public static <O> Pattern<O> $() {
        return Pattern::bind;
    }

    public static <O> Pattern<O> $(O toMatch) {
        return x -> Objects.equals(x, toMatch) ? Pattern.bind(x) : Pattern.FAIL;
    }

    @SafeVarargs
    public static <R> Pattern<R> $any(R... values) {
        return x -> {
            for (var val : values) {
                if (Objects.equals(x, val)) {
                    return Pattern.bind(x);
                }
            }
            return Pattern.FAIL;
        };
    }

    public static <O> Pattern<O> $instance(Class<? super O> clazz) {
        return x -> clazz.isInstance(x) ? Pattern.bind(x) : Pattern.FAIL;
    }

    public static Pattern<String> $RegEx(String regex) {
        return $RegEx(java.util.regex.Pattern.compile(regex));
    }

    public static Pattern<String> $RegEx(java.util.regex.Pattern pattern) {
        return x -> {
            var matcher = pattern.matcher(x);
            return matcher.matches() ? Pattern.bind(x) : Pattern.FAIL;
        };
    }

    public static final Pattern<Integer> LT = x -> x < 0 ? Pattern.PASS : Pattern.FAIL;
    public static final Pattern<Integer> GT = x -> x > 0 ? Pattern.PASS : Pattern.FAIL;
    public static final Pattern<Integer> EQ = x -> x == 0 ? Pattern.PASS : Pattern.FAIL;

    public static <O> Pattern<Try<O>> Success_(Pattern<O> value) {
        return PatternFor(Try.Success.class, value);
    }

    public static <O> Pattern<Try<O>> Failure_(Pattern<Exception> error) {
        return PatternFor(Try.Failure.class, error);
    }

    public static <L, R> Pattern<Either<L, R>> Left_(Pattern<L> value) {
        return PatternFor(Left.class, value);
    }

    public static <L, R> Pattern<Either<L, R>> Right_(Pattern<R> value) {
        return PatternFor(Right.class, value);
    }

    public static <O> Pattern<Lazy<O>> Lazy_(Pattern<O> value) {
        return PatternFor(Lazy.class, value);
    }

    public static <O> Pattern<Option<O>> Some_(Pattern<O> value) {
        return PatternFor(Option.Some.class, value);
    }

    public static <O> Pattern<Option<O>> None_() {
        return PatternFor(Option.None.INSTANCE);
    }

    public static final Pattern<Unit> Unit_ = PatternFor(Unit.INSTANCE);

    public static <A> Pattern<Tuple1<A>> Tuple1_(Pattern<A> $1) {
        return PatternFor(Tuple1.class, $1);
    }

    public static <A, B> Pattern<Tuple2<A, B>> Tuple2_(Pattern<A> $1, Pattern<B> $2) {
        return PatternFor(Tuple2.class, $1, $2);
    }

    public static <A, B, C> Pattern<Tuple3<A, B, C>> Tuple3_(Pattern<A> $1, Pattern<B> $2, Pattern<C> $3) {
        return PatternFor(Tuple3.class, $1, $2, $3);
    }

    public static <A, B, C, D> Pattern<Tuple4<A, B, C, D>> Tuple4_(Pattern<A> $1, Pattern<B> $2, Pattern<C> $3, Pattern<D> $4) {
        return PatternFor(Tuple4.class, $1, $2, $3, $4);
    }

    public static <A, B, C, D, E> Pattern<Tuple5<A, B, C, D, E>> Tuple5_(Pattern<A> $1, Pattern<B> $2, Pattern<C> $3, Pattern<D> $4, Pattern<E> $5) {
        return PatternFor(Tuple5.class, $1, $2, $3, $4, $5);
    }

    public static <A, B, C, D, E, F> Pattern<Tuple6<A, B, C, D, E, F>> Tuple6_(Pattern<A> $1, Pattern<B> $2, Pattern<C> $3, Pattern<D> $4, Pattern<E> $5, Pattern<F> $6) {
        return PatternFor(Tuple6.class, $1, $2, $3, $4, $5, $6);
    }

    public static <A, B, C, D, E, F, G> Pattern<Tuple7<A, B, C, D, E, F, G>> Tuple7_(Pattern<A> $1, Pattern<B> $2, Pattern<C> $3, Pattern<D> $4, Pattern<E> $5, Pattern<F> $6, Pattern<G> $7) {
        return PatternFor(Tuple7.class, $1, $2, $3, $4, $5, $6, $7);
    }

    public static <A, B, C, D, E, F, G, H> Pattern<Tuple8<A, B, C, D, E, F, G, H>> Tuple8_(Pattern<A> $1, Pattern<B> $2, Pattern<C> $3, Pattern<D> $4, Pattern<E> $5, Pattern<F> $6, Pattern<G> $7, Pattern<H> $8) {
        return PatternFor(Tuple8.class, $1, $2, $3, $4, $5, $6, $7, $8);
    }
}
