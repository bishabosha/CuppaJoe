package com.github.bishabosha.cuppajoe.match.patterns;

import com.github.bishabosha.cuppajoe.match.patterns.Pattern.*;

import java.util.Objects;
import java.util.function.IntFunction;
import java.util.function.Predicate;
import java.util.stream.Stream;

import static com.github.bishabosha.cuppajoe.match.internal.extract.Extractors.alwaysTrue;
import static com.github.bishabosha.cuppajoe.match.patterns.Pattern.*;
import static java.util.regex.Pattern.compile;

public final class Standard {

    private Standard() {}

    public static <T> Value<T> id() {
        return value(alwaysTrue());
    }

    public static <T> Empty<T> __() {
        return empty(alwaysTrue());
    }

    public static <O> Empty<O> nil() {
        return empty(Objects::isNull);
    }

    public static <O> Empty<O> is(O toMatch) {
        return toMatch == null ? nil() : empty(x -> x == toMatch);
    }

    public static <O> Value<O> eq(O value) {
        return value == null ? value(Objects::isNull) : value(value::equals);
    }

    @SafeVarargs
    public static <O> Value<O> eq(O first, O... rest) {
        return value(range(first, rest));
    }

    public static <O> Empty<O> in(O value) {
        return value == null ? empty(Objects::isNull) : empty(value::equals);
    }

    @SafeVarargs
    public static <O> Empty<O> in(O first, O... rest) {
        return empty(range(first, rest));
    }

    private static <O> Predicate<O> range(O first, O... rest) {
        return x -> {
            if (Objects.equals(x, first)) {
                return true;
            } else {
                for (var val : rest) {
                    if (Objects.equals(x, val)) {
                        return true;
                    }
                }
            }
            return false;
        };
    }

    public static <R, O extends R> Value<R> of(Class<O> clazz) {
        return value(classEq(clazz));
    }

    public static <R, O extends R> Empty<R> as(Class<O> clazz) {
        return empty(classEq(clazz));
    }

    @SafeVarargs
    public static <O> Value<O> any(Value<O>... values) {
        return value(any(Stream.of(values).map(Pattern::matches)));
    }

    @SafeVarargs
    public static <O> Empty<O> any(Empty<O>... values) {
        return empty(any(Stream.of(values).map(Pattern::matches)));
    }

    private static <O> Predicate<O> any(Stream<Predicate<O>> predicates) {
        return predicates.reduce(alwaysFalse(), (result, predicate) -> {
            if (alwaysTrue() == predicate) {
                return predicate;
            } else if (alwaysFalse() != predicate) {
                return alwaysFalse() == result
                        ? predicate
                        : result.or(predicate);
            } else {
                return result;
            }
        });
    }

    public static <O> Value<O> not(Value<O> value) {
        return value(value.matches().negate());
    }

    private static <O> Predicate<O> alwaysFalse() {
        return x -> false;
    }

    public static Value<String> regex(String regex) {
        return regex(compile(regex));
    }

    public static Value<String> regex(java.util.regex.Pattern pattern) {
        return value(x -> pattern.matcher(x).matches());
    }
}
