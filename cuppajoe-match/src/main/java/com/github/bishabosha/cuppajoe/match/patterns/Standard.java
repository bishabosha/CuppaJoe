package com.github.bishabosha.cuppajoe.match.patterns;

import java.util.Objects;

import static java.util.regex.Pattern.compile;

public final class Standard {

    public static <O> Pattern<O> __() {
        return x -> Pattern.PASS;
    }

    public static <O> Pattern<O> id() {
        return Pattern::bind;
    }

    public static <O> Pattern<O> eq(O toMatch) {
        return x -> Objects.equals(x, toMatch) ? Pattern.bind(x) : Pattern.FAIL;
    }

    @SafeVarargs
    public static <O> Pattern<O> eq(O first, O... rest) {
        return x -> {
            if (Objects.equals(first, x)) {
                return Pattern.bind(x);
            }
            if (rest == null) {
                return x == null ? Pattern.bind(null) : Pattern.FAIL;
            } else {
                for (var val : rest) {
                    if (Objects.equals(x, val)) {
                        return Pattern.bind(x);
                    }
                }
            }
            return Pattern.FAIL;
        };
    }

    public static <O> Pattern<O> of(Class<? super O> clazz) {
        return x -> clazz.isInstance(x) ? Pattern.bind(x) : Pattern.FAIL;
    }

    public static Pattern<String> regex(String regex) {
        return regex(compile(regex));
    }

    public static Pattern<String> regex(java.util.regex.Pattern pattern) {
        return x -> pattern.matcher(x).matches() ? Pattern.bind(x) : Pattern.FAIL;
    }
}
