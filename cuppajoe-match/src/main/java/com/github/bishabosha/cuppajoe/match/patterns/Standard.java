package com.github.bishabosha.cuppajoe.match.patterns;

import com.github.bishabosha.cuppajoe.match.patterns.Pattern.Empty;
import com.github.bishabosha.cuppajoe.match.patterns.Pattern.Value;

import java.util.Objects;
import java.util.function.Predicate;

import static com.github.bishabosha.cuppajoe.match.internal.extract.Extractors.alwaysTrue;
import static com.github.bishabosha.cuppajoe.match.patterns.Pattern.empty;
import static com.github.bishabosha.cuppajoe.match.patterns.Pattern.value;
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

    public static <O> Value<O> of(Class<? super O> clazz) {
        return value(clazz::isInstance);
    }

    public static Value<String> regex(String regex) {
        return regex(compile(regex));
    }

    public static Value<String> regex(java.util.regex.Pattern pattern) {
        return value(x -> pattern.matcher(x).matches());
    }
}
