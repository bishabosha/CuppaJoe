package com.github.bishabosha.cuppajoe.match.patterns;

import com.github.bishabosha.cuppajoe.collections.immutable.API;
import com.github.bishabosha.cuppajoe.collections.immutable.tuples.Tuple;
import com.github.bishabosha.cuppajoe.collections.immutable.tuples.Tuple2;
import com.github.bishabosha.cuppajoe.match.Path;

import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Deque;
import java.util.List;
import java.util.function.IntFunction;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public abstract class Pattern<T> {

    private final Predicate<T> matches;

    public interface PatternVisitor {
        <O> void onEmpty(Empty<O> empty);
        <O> void onValue(Value<O> value);
        <O> void onBranch(Branch<O> branch);
    }

    public static <T> Empty<T> empty(Predicate<T> matches) {
        return new Empty<>(matches);
    }

    public static <T> Value<T> value(Predicate<T> matches) {
        return new Value<>(matches);
    }

    public static <R, T extends R> Branch<R> branch1(Class<T> canBranch, Pattern<?> branch, Path<T, ?> path) {
        return branch1(classEq(canBranch), branch, path);
    }

    public static <T> Branch<T> branch1(Predicate<T> canBranch, Pattern<?> branch, Path<? extends T, ?> path) {
        return new Branch<>(canBranch, Tuple.of(branch, path));
    }

    @SafeVarargs
    public static <R, T extends R> Branch<R> branchN(Class<T> canBranch, Tuple2<Pattern<?>, Path<T, ?>>... paths) {
        return branchN(classEq(canBranch), paths);
    }

    @SafeVarargs
    public static <T> Branch<T> branchN(Predicate<T> canBranch, Tuple2<Pattern<?>, ? extends Path<? extends T, ?>>... paths) {
        return new Branch<>(canBranch, paths);
    }

    @SuppressWarnings("unchecked")
    public static <R, T extends R> Branch<R> branchGenerator(Class<T> canBranch, Stream<Tuple2<Pattern<?>, ? extends Path<T, ?>>> paths) {
        return new Branch<>(classEq(canBranch), paths.toArray(Tuple2[]::new));
    }

    @SuppressWarnings("unchecked")
    public static <T> Branch<T> branchGenerator(Predicate<T> canBranch, Stream<Tuple2<Pattern<?>, ? extends Path<? extends T, ?>>> paths) {
        return new Branch<>(canBranch, paths.toArray(Tuple2[]::new));
    }

    @SuppressWarnings("unchecked")
    public static <R, T extends R> Branch<R> branchGenerator(Class<T> canBranch, IntFunction<? extends Path<? extends T, ?>> generator, Pattern<?>... patterns) {
        return branchGenerator(classEq(canBranch), generator, patterns);
    }

    @SuppressWarnings("unchecked")
    public static <T> Branch<T> branchGenerator(Predicate<T> canBranch, IntFunction<? extends Path<? extends T, ?>> generator, Pattern<?>... patterns) {
        return branchGenerator(
            canBranch,
            IntStream.range(0, patterns.length)
                     .mapToObj(i ->
                         API.Tuple(patterns[i], generator.apply(i))
                     )
        );
    }

    public static <R, T extends R> Predicate<R> classEq(Class<? extends T> tClass) {
        return x -> x != null && tClass == x.getClass();
    }

    private Pattern(Predicate<T> matches) {
        this.matches = matches;
    }

    public Predicate<T> matches() {
        return matches;
    }

    public abstract void accept(PatternVisitor visitor);

    public static final class Empty<T> extends Pattern<T> {
        private Empty(Predicate<T> matches) {
            super(matches);
        }

        @Override
        public void accept(PatternVisitor visitor) {
            visitor.onEmpty(this);
        }
    }

    public static final class Value<T> extends Pattern<T> {
        private Value(Predicate<T> matches) {
            super(matches);
        }

        @Override
        public void accept(PatternVisitor visitor) {
            visitor.onValue(this);
        }
    }

    public static class Branch<T> extends Pattern<T> {
        private final List<Pattern<?>> branches;
        private final Deque<Path<? extends T, ?>> paths;

        @SafeVarargs
        private Branch(Predicate<T> canBranch, Tuple2<Pattern<?>, ? extends Path<? extends T, ?>>... paths) {
            super(canBranch);
            this.branches = Arrays.stream(paths).map(x -> x.$1).collect(Collectors.toList());
            this.paths = new ArrayDeque<>();
            Arrays.stream(paths).map(x -> x.$2).forEach(this.paths::addFirst);
        }

        public final Stream<Path<? extends T, ?>> pathsAscending() {
            return paths.stream();
        }

        public final void visitEachBranch(PatternVisitor visitor) {
            branches.forEach(branch -> branch.accept(visitor));
        }

        @Override
        public final void accept(PatternVisitor visitor) {
            visitor.onBranch(this);
        }
    }
}
