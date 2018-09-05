package com.github.bishabosha.cuppajoe.match.typesafe.internal.extract;

import com.github.bishabosha.cuppajoe.match.ExtractionFailedException;
import com.github.bishabosha.cuppajoe.match.typesafe.internal.patterns.Bootstraps;
import com.github.bishabosha.cuppajoe.match.typesafe.patterns.Pattern;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.util.List;
import java.util.function.Function;

import static java.lang.invoke.MethodHandles.*;

public final class Extractors {

    private static class MethodHandleConstants {
        private static final MethodHandle IDENTITY;
        private static final MethodHandle NEVER_MATCH_CONST;
        private static final MethodHandle NEVER_MATCH_DEFAULT;
        private static final MethodHandle LOGICAL_AND;

        static {
            var lookup = MethodHandles.lookup();
            IDENTITY = MethodHandles.identity(Object.class);
            try {
                NEVER_MATCH_CONST = lookup.findStatic(MethodHandleConstants.class, "neverMatch", MethodType.methodType(boolean.class));
                NEVER_MATCH_DEFAULT = MethodHandles.dropArgumentsToMatch(NEVER_MATCH_CONST, 0, List.of(Object.class), 0);
                LOGICAL_AND = lookup.findStatic(Boolean.class, "logicalAnd", MethodType.methodType(boolean.class, boolean.class, boolean.class));
            } catch (NoSuchMethodException | IllegalAccessException e) {
                throw new Error(e);
            }
        }

        private static boolean neverMatch() {
            throw new IllegalStateException("This predicate will never match");
        }
    }

    public static <E extends Extractor> E compile(Pattern pattern, E extractor) {
        pattern.accept(extractor);
        if (extractor.uninitialized()) {
            throw new ExtractionFailedException();
        }
        return extractor;
    }

    public static MethodHandle identity() {
        return MethodHandleConstants.IDENTITY;
    }

    static MethodHandle neverMatch() {
        return MethodHandleConstants.NEVER_MATCH_DEFAULT;
    }

    private static MethodHandle logicalAnd() {
        return MethodHandleConstants.LOGICAL_AND;
    }

    private Extractors() {
    }

    static MethodHandle composePredicates(MethodHandle before, MethodHandle after) {
        return neverMatches(before) || Bootstraps.isAlwaysTrue(before)
            ? after
            : Bootstraps.isAlwaysTrue(after)
                ? before
                : composeWithAnd(before, after);
    }

    private static MethodHandle composeWithAnd(MethodHandle before, MethodHandle after) {
        var eval = filterArguments(logicalAnd(), 0, before, after.asType(before.type()));
        return permuteArguments(eval, before.type(), 0, 0);
    }

    static MethodHandle composeWithPath(MethodHandle path, MethodHandle predicate) {
        return isIdentity(path)
            ? predicate
            : Bootstraps.isAlwaysTrue(predicate)
                ? predicate
                : filterReturnValue(path, predicate.asType(predicate.type().changeParameterType(0, path.type().returnType())));
    }

    static Function<MethodHandle, MethodHandle> composePaths(MethodHandle path) {
        return next -> composePaths(path, next);
    }

    private static MethodHandle composePaths(MethodHandle before, MethodHandle after) {
        return isIdentity(before) ? after : filterReturnValue(before.asType(before.type().changeReturnType(after.type().parameterType(0))), after);
    }

    static boolean neverMatches(MethodHandle predicate) {
        return neverMatch() == predicate;
    }

    private static boolean isIdentity(MethodHandle path) {
        return identity() == path;
    }
}

