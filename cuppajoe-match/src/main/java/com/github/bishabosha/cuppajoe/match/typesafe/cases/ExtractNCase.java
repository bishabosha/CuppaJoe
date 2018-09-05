package com.github.bishabosha.cuppajoe.match.typesafe.cases;

import com.github.bishabosha.cuppajoe.control.Option;
import com.github.bishabosha.cuppajoe.control.Option.None;
import com.github.bishabosha.cuppajoe.control.Option.Some;
import com.github.bishabosha.cuppajoe.match.MatchException;
import com.github.bishabosha.cuppajoe.match.typesafe.cases.Case.CombinatorCase;
import com.github.bishabosha.cuppajoe.match.typesafe.internal.extract.ExtractN;
import com.github.bishabosha.cuppajoe.match.typesafe.internal.extract.Extractors;
import com.github.bishabosha.cuppajoe.match.typesafe.patterns.Pattern;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;

public final class ExtractNCase extends CombinatorCase {

    private static class Handles {
        private static final MethodHandle SOME;
        private static final MethodHandle NONE;
        private static final MethodHandle NEW_MATCH_EXCEPTION;
        private static final MethodHandle NEW_MATCH_EXCEPTION_EMPTY;

        static {
            var lookup = MethodHandles.lookup();
            try {
                SOME = lookup.findStatic(Option.class, "some", MethodType.methodType(Some.class, Object.class)).asType(MethodType.methodType(Option.class, Object.class));
                NONE = lookup.findStatic(Option.class, "none", MethodType.methodType(None.class)).asType(MethodType.methodType(Option.class));
                NEW_MATCH_EXCEPTION = lookup.findConstructor(MatchException.class, MethodType.methodType(void.class, Object.class));
                NEW_MATCH_EXCEPTION_EMPTY = lookup.findConstructor(MatchException.class, MethodType.methodType(void.class));
            } catch (NoSuchMethodException | IllegalAccessException e) {
                throw new Error(e);
            }
        }
    }

    private final MethodHandle func;
    private final MethodHandle matcher;
    private final MethodHandle[] paths;

    public ExtractNCase(Pattern pattern, MethodHandle func) {
        var extractN = Extractors.compile(pattern, new ExtractN(func.type().parameterList()));
        matcher = extractN.matcher();
        paths = extractN.getPaths();
        this.func = func;
    }

    @Override
    public MethodHandle get() {
        var extract = extract();
        var matchExceptionThrower = MethodHandles.throwException(func.type().returnType(), MatchException.class);
        if (paths.length == 0) {
            var newMatchExceptionThrower = MethodHandles.filterReturnValue(Handles.NEW_MATCH_EXCEPTION_EMPTY, matchExceptionThrower);
            var paddedThrower = MethodHandles.dropArgumentsToMatch(newMatchExceptionThrower, 0, matcher.type().parameterList(), 0);
            var paddedExtract = MethodHandles.dropArgumentsToMatch(extract, 0, matcher.type().parameterList(), 0);
            return MethodHandles.guardWithTest(matcher, paddedExtract, paddedThrower);
        }
        var exceptionMaker = Handles.NEW_MATCH_EXCEPTION.asType(Handles.NEW_MATCH_EXCEPTION.type().changeParameterType(0, extract.type().parameterType(0)));
        var genericType = extract.type().changeParameterType(0, matcher.type().parameterType(0));
        var newMatchExceptionThrower = MethodHandles.filterReturnValue(exceptionMaker, matchExceptionThrower);
        return MethodHandles.guardWithTest(matcher, extract.asType(genericType), newMatchExceptionThrower.asType(genericType));
    }

    @Override
    public MethodHandle match() {
        var extract = extract();
        var wrapExtract = MethodHandles.filterReturnValue(extract, Handles.SOME.asType(Handles.SOME.type().changeParameterType(0, extract.type().returnType())));
        var nonePadded = MethodHandles.dropArgumentsToMatch(Handles.NONE, 0, matcher.type().parameterList(), 0);
        if (paths.length == 0) {
            var wrapExtractPad = MethodHandles.dropArgumentsToMatch(wrapExtract, 0, matcher.type().parameterList(), 0);
            return MethodHandles.guardWithTest(matcher, wrapExtractPad, nonePadded);
        }
        var genericType = wrapExtract.type().changeParameterType(0, matcher.type().parameterType(0));
        return MethodHandles.guardWithTest(matcher, wrapExtract.asType(genericType), nonePadded);
    }

    @Override
    public MethodHandle matches() {
        return matcher;
    }

    @Override
    public MethodHandle extract() {
        if (paths.length == 0) {
            return func;
        }
        if (paths.length == 1) {
            return MethodHandles.filterReturnValue(paths[0], func);
        }
        var pathsFolded = MethodHandles.filterArguments(func, 0, paths);
        var newType = paths[0].type().changeReturnType(func.type().returnType());
        var zeros = new int[func.type().parameterCount()];
        return MethodHandles.permuteArguments(pathsFolded, newType, zeros);
    }
}
