package com.github.bishabosha.cuppajoe.match.typesafe.patterns;

import com.github.bishabosha.cuppajoe.collections.immutable.API;
import com.github.bishabosha.cuppajoe.collections.immutable.tuples.Tuple;
import com.github.bishabosha.cuppajoe.collections.immutable.tuples.Tuple2;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.reflect.Modifier;
import java.util.*;
import java.util.function.IntFunction;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public abstract class Pattern {

    private final MethodHandle matches;

    public interface PatternVisitor {
        void onEmpty(Empty empty);
        void onValue(Value value);
        void onBranch(Branch branch);
    }

    public static Empty empty(MethodHandle matches) {
        return new Empty(matches);
    }

    public static Value value(MethodHandle matches) {
        return new Value(matches);
    }

    public static <T> Branch branch1(Class<T> canBranch, Pattern branch, MethodHandle path) {
        return branch1(classEq(canBranch), branch, path);
    }

    public static Branch branch1(MethodHandle canBranch, Pattern branch, MethodHandle path) {
        return new Branch(canBranch, Tuple.of(branch, path));
    }

    @SafeVarargs
    public static <T> Branch branchN(Class<T> canBranch, Tuple2<Pattern, MethodHandle>... paths) {
        return branchN(classEq(canBranch), paths);
    }

    @SafeVarargs
    public static Branch branchN(MethodHandle canBranch, Tuple2<Pattern, MethodHandle>... paths) {
        return new Branch(canBranch, paths);
    }

    @SuppressWarnings("unchecked")
    public static <T> Branch branchGenerator(Class<T> canBranch, Stream<Tuple2<Pattern, MethodHandle>> paths) {
        return new Branch(classEq(canBranch), paths.toArray(Tuple2[]::new));
    }

    @SuppressWarnings("unchecked")
    public static Branch branchGenerator(MethodHandle canBranch, Stream<Tuple2<Pattern, MethodHandle>> paths) {
        return new Branch(canBranch, paths.toArray(Tuple2[]::new));
    }

    @SuppressWarnings("unchecked")
    public static <T> Branch branchGenerator(Class<T> canBranch, IntFunction<MethodHandle> generator, Pattern... patterns) {
        return branchGenerator(classEq(canBranch), generator, patterns);
    }

    @SuppressWarnings("unchecked")
    public static Branch branchGenerator(MethodHandle canBranch, IntFunction<MethodHandle> generator, Pattern... patterns) {
        return branchGenerator(
            canBranch,
            IntStream.range(0, patterns.length)
                     .mapToObj(i ->
                         API.Tuple(patterns[i], generator.apply(i))
                     )
        );
    }

    private static final MethodHandle GET_CLASS;
    private static final MethodHandle CLASS_EQ;
    private static final MethodHandle CLASS_ASSIGNABLE;

    static {
        var lookup = MethodHandles.lookup();
        try {
            GET_CLASS = lookup.findVirtual(Object.class, "getClass", MethodType.methodType(Class.class));
            CLASS_EQ = lookup.findStatic(Pattern.class, "classEq", MethodType.methodType(boolean.class, Class.class, Class.class));
            CLASS_ASSIGNABLE = lookup.findVirtual(Class.class, "isAssignableFrom", MethodType.methodType(boolean.class, Class.class));
        } catch (NoSuchMethodException | IllegalAccessException e) {
            throw new Error(e);
        }
    }

    private static boolean classEq(Class a, Class b) {
        return a == b;
    }

    private static final Map<Class<?>, MethodHandle> CLASS_EQ_CACHE = new WeakHashMap<>();

    public static <T> MethodHandle classEq(Class<T> testClass) {
        return CLASS_EQ_CACHE.computeIfAbsent(testClass, type -> {
            if (!Modifier.isFinal(testClass.getModifiers())) {
                throw new IllegalArgumentException(testClass + " is not final");
            }
            var classEqBound = CLASS_EQ.bindTo(testClass);
            return MethodHandles.filterReturnValue(GET_CLASS, classEqBound);
        });
    }


    private static final Map<Class<?>, MethodHandle> CLASS_ASSIGNABLE_CACHE = new WeakHashMap<>();

    public static <T> MethodHandle classAssignable(Class<T> testClass) {
        return CLASS_ASSIGNABLE_CACHE.computeIfAbsent(testClass, type -> {
            var classEqBound = CLASS_ASSIGNABLE.bindTo(testClass);
            return MethodHandles.filterReturnValue(GET_CLASS, classEqBound);
        });
    }

    private Pattern(MethodHandle matches) {
        this.matches = matches;
    }

    public MethodHandle matches() {
        return matches;
    }

    public abstract void accept(PatternVisitor visitor);

    public static final class Empty extends Pattern {
        private Empty(MethodHandle matches) {
            super(matches);
        }

        @Override
        public void accept(PatternVisitor visitor) {
            visitor.onEmpty(this);
        }
    }

    public static final class Value extends Pattern {
        private Value(MethodHandle matches) {
            super(matches);
        }

        @Override
        public void accept(PatternVisitor visitor) {
            visitor.onValue(this);
        }
    }

    public static class Branch extends Pattern {
        private final List<Pattern> branches;
        private final Deque<MethodHandle> paths;

        @SafeVarargs
        private Branch(MethodHandle canBranch, Tuple2<Pattern, MethodHandle>... paths) {
            super(canBranch);
            this.branches = Arrays.stream(paths).map(x -> x.$1).collect(Collectors.toList());
            this.paths = new ArrayDeque<>();
            Arrays.stream(paths).map(x -> x.$2).forEach(this.paths::addFirst);
        }

        public final Stream<MethodHandle> pathsAscending() {
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
