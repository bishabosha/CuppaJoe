package com.github.bishabosha.cuppajoe.match.patterns;

import com.github.bishabosha.cuppajoe.control.Either;
import com.github.bishabosha.cuppajoe.control.Either.Left;
import com.github.bishabosha.cuppajoe.control.Either.Right;
import com.github.bishabosha.cuppajoe.control.Lazy;
import com.github.bishabosha.cuppajoe.control.Option;
import com.github.bishabosha.cuppajoe.control.Try;

import java.util.Objects;

import static com.github.bishabosha.cuppajoe.match.API.PatternFor;
import static java.util.regex.Pattern.compile;

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
        return $RegEx(compile(regex));
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
}
