package io.cuppajoe.match;

import io.cuppajoe.control.Option;
import org.jetbrains.annotations.NotNull;

import static io.cuppajoe.API.None;

public final class API {

    @NotNull
    public static <I> Matcher<I> Match(I toMatch) {
        return Matcher.create(toMatch);
    }

    public static <I, O> Option<O> Match(Option<I> toMatch, Case<I, O> options) {
        return toMatch.isEmpty() ? None() : options.match(toMatch.get());
    }
}
