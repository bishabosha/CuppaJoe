package com.github.bishabosha.cuppajoe.match.incubator.internal.extract;

import com.github.bishabosha.cuppajoe.match.incubator.ExtractionFailedException;
import com.github.bishabosha.cuppajoe.match.incubator.Path;
import com.github.bishabosha.cuppajoe.match.incubator.patterns.Pattern;

import java.util.function.Function;
import java.util.function.Predicate;

public final class Extractors {

    public static <T, E extends Extractor> E compile(Pattern<T> pattern, E extractor) {
        pattern.accept(extractor);
        if (extractor.notInstantiated()) {
            throw new ExtractionFailedException();
        }
        return extractor;
    }

    public static <T> Predicate<T> alwaysTrue() {
        return x -> true;
    }

    static <T> Predicate<T> neverMatch() {
        return x -> { throw new IllegalStateException("This predicate will never match"); };
    }

    private Extractors() {
    }

    @SuppressWarnings("unchecked")
    static <I, T> Predicate<I> composePredicates(Predicate<I> before, Predicate<T> after) {
        return neverMatches(before) || isIdempotent(before)
            ? (Predicate<I>) after
            : isIdempotent(after)
                ? before
                : before.and((Predicate<I>) after);
    }

    @SuppressWarnings("unchecked")
    static <I, T> Predicate<I> composeWithPath(Path path, Predicate<T> predicate) {
        return isIdempotent(path)
            ? (Predicate<I>) predicate
            : isIdempotent(predicate)
                ? (Predicate<I>) predicate
                : x -> predicate.test((T)path.get(x));
    }

    static Function<Path, Path> composePaths(Path path) {
        return next -> composePaths(path, next);
    }

    @SuppressWarnings("unchecked")
    private static Path composePaths(Path before, Path after) {
        return isIdempotent(before) ? after : before.then(after);
    }

    static boolean neverMatches(Predicate predicate) {
        return neverMatch() == predicate;
    }

    private static boolean isIdempotent(Predicate predicate) {
        return alwaysTrue() == predicate;
    }

    private static boolean isIdempotent(Path func1) {
        return Path.identity() == func1;
    }
}

