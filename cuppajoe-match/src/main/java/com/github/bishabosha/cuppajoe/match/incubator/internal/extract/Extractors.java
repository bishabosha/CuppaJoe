package com.github.bishabosha.cuppajoe.match.incubator.internal.extract;

import com.github.bishabosha.cuppajoe.higher.functions.Func1;
import com.github.bishabosha.cuppajoe.match.incubator.ExtractionFailedException;
import com.github.bishabosha.cuppajoe.match.incubator.internal.extract.Path.CompletedPath;
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

    @SuppressWarnings("unchecked")
    public static <I> Func1<I, I> identity() {
        return x -> x;
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

    @SuppressWarnings("unchecked")
    private static <I, O> CompletedPath<I, O> identityCompletedPath() {
        return x -> (O) x;
    }

    private Extractors() {
    }

    static <I> Predicate<I> extractMatches(Leaf<I> leaf) {
        return alwaysMatches(leaf) ? alwaysTrue() : leaf::matches;
    }

    @SuppressWarnings("unchecked")
    static <I, T> Predicate<I> composePredicates(Predicate<I> before, Predicate<T> after, Func1<I, T> mapper) {
        return neverMatches(before) || isIdempotent(before)
            ? (Predicate<I>) after
            : isIdempotent(after)
                ? before
                : isIdempotent(mapper)
                    ? before.and((Predicate<I>) after)
                    : before.and(x -> after.test(mapper.apply(x)));
    }

    @SuppressWarnings("unchecked")
    static <U, P, T, S> Path<T, S> composePaths(Path<T, S> before, Path<U, P> after) {
        return goesNowhere(before) ? (Path<T, S>) after : before.then((Path<S, S>) after);
    }

    @SuppressWarnings("unchecked")
    static <T, S> Path<T, S> completePath(Path<T, S> path) {
        return goesNowhere(path) ? identityCompletedPath() : Path.complete(path);
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

    private static boolean isIdempotent(Func1 func1) {
        return identity() == func1;
    }

//    private static boolean isIdempotent(Path path) {
//        return identityCompletedPath() == path;
//    }

    private static boolean goesNowhere(Path path) {
        return toNowhere() == path;
    }
}

