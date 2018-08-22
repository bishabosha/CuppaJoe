package com.github.bishabosha.cuppajoe.match.incubator.internal.extract;

import com.github.bishabosha.cuppajoe.match.incubator.ExtractionFailedException;
import com.github.bishabosha.cuppajoe.match.incubator.patterns.Pattern;
import com.github.bishabosha.cuppajoe.match.incubator.patterns.Pattern.Leaf;

import java.util.function.Predicate;

import static com.github.bishabosha.cuppajoe.match.incubator.internal.patterns.Bootstrap.__;
import static com.github.bishabosha.cuppajoe.match.incubator.internal.patterns.Bootstrap.id;

public abstract class Extractors {

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

    static <I, O> Path<I, O> toNowhere() {
        return x -> { throw new IllegalStateException("This Path goes to nowhere"); };
    }

    private Extractors() {
    }

    static <I, T> Predicate<I> extractMatches(Path<I, T> path, Leaf<T> leaf) {
        return alwaysMatches(leaf) ? alwaysTrue() : composeWithPath(path, leaf::matches);
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
    static <I, T> Predicate<I> composeWithPath(Path<I, T> path, Predicate<T> predicate) {
        return isIdempotent(path) ? (Predicate<I>) predicate : x -> predicate.test(path.get(x));
    }

    @SuppressWarnings("unchecked")
    static <U, P, T, S> Path<T, S> composePaths(Path<T, S> before, Path<U, P> after) {
        return goesNowhere(before) || isIdempotent(before) ? (Path<T, S>) after : before.then((Path<S, S>) after);
    }

    @SuppressWarnings("unchecked")
    static <T, S> Path<T, S> completePath(Path<T, S> path) {
        return goesNowhere(path) ? Path.identityCompletedPath() : Path.complete(path);
    }

    private static <O> boolean alwaysMatches(Leaf<O> leaf) {
        return leaf == id() || leaf == __();
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

    private static boolean goesNowhere(Path path) {
        return toNowhere() == path;
    }
}

