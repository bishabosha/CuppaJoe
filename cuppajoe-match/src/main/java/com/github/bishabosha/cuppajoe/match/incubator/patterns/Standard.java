package com.github.bishabosha.cuppajoe.match.incubator.patterns;

import java.util.Objects;

import static com.github.bishabosha.cuppajoe.match.incubator.internal.extract.Extractors.alwaysTrue;
import static com.github.bishabosha.cuppajoe.match.incubator.patterns.Pattern.empty;
import static com.github.bishabosha.cuppajoe.match.incubator.patterns.Pattern.value;
import static java.util.regex.Pattern.compile;

public final class Standard {

    private Standard() {}

    public static <T> Pattern<T> id() {
        return value(alwaysTrue());
    }

    public static <T> Pattern<T> __() {
        return empty(alwaysTrue());
    }

    public static <O> Pattern<O> nil() {
        return empty(Objects::isNull);
    }

    public static <O> Pattern<O> is(O toMatch) {
        return toMatch == null ? nil() : empty(x -> x == toMatch);
    }

    public static <O> Pattern<O> eq(O toMatch) {
        return toMatch == null ? value(Objects::isNull) : value(toMatch::equals);
    }

    @SafeVarargs
    public static <O> Pattern<O> eq(O first, O... rest) {
        return value(x -> {
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
        });
    }

    public static <O> Pattern<O> of(Class<? super O> clazz) {
        return value(clazz::isInstance);
    }

    public static Pattern<String> regex(String regex) {
        return regex(compile(regex));
    }

    public static Pattern<String> regex(java.util.regex.Pattern pattern) {
        return value(x -> pattern.matcher(x).matches());
    }
}
