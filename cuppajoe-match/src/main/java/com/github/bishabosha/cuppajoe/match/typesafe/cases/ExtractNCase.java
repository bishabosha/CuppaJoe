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
import java.util.stream.Stream;

public final class ExtractNCase extends CombinatorCase {

    private static class Handles {
        private static final MethodHandle SOME;
        private static final MethodHandle NONE;
        private static final MethodHandle NEW_MATCH_EXCEPTION;

        static {
            var lookup = MethodHandles.lookup();
            try {
                SOME = lookup.findStatic(Option.class, "some", MethodType.methodType(Some.class, Object.class)).asType(MethodType.methodType(Option.class, Object.class));
                NONE = lookup.findStatic(Option.class, "none", MethodType.methodType(None.class)).asType(MethodType.methodType(Option.class));
                NEW_MATCH_EXCEPTION = lookup.findConstructor(MatchException.class, MethodType.methodType(void.class, Object.class));
            } catch (NoSuchMethodException | IllegalAccessException e) {
                throw new Error(e);
            }
        }
    }

    private final MethodHandle func;
    private final MethodHandle matcher;
    private final MethodHandle[] paths;

    public ExtractNCase(Pattern pattern, MethodHandle func) {
        var extractN = Extractors.compile(pattern, new ExtractN(func.type().parameterCount()));
        matcher = extractN.matcher();
        paths = extractN.getPaths();
        this.func = func;
    }

    @Override
    public MethodHandle get() {
        var extract = extract();
        var matchExceptionThrower = MethodHandles.throwException(func.type().returnType(), MatchException.class);
        var exceptionMaker = Handles.NEW_MATCH_EXCEPTION.asType(Handles.NEW_MATCH_EXCEPTION.type().changeParameterType(0, extract.type().parameterType(0)));
        var newMatchExceptionThrower = MethodHandles.filterReturnValue(exceptionMaker, matchExceptionThrower).asType(extract.type());
        return MethodHandles.guardWithTest(matcher, extract, newMatchExceptionThrower);
    }

    @Override
    public MethodHandle match() {
        var extract = extract();
        var wrapExtract = MethodHandles.filterReturnValue(extract(), Handles.SOME.asType(Handles.SOME.type().changeParameterType(0, extract.type().returnType())));
        var nonePadded = MethodHandles.dropArgumentsToMatch(Handles.NONE, 0, extract.type().parameterList(), 0);
        return MethodHandles.guardWithTest(matcher, wrapExtract, nonePadded);
    }

    @Override
    public MethodHandle matches() {
        return matcher;
    }

    @Override
    public MethodHandle extract() {
        var handlesAsTypes = Stream.of(paths).map(mh -> mh.asType(mh.type().changeReturnType(func.type().parameterType(0)))).toArray(MethodHandle[]::new);
        var pathsFolded = MethodHandles.filterArguments(func, 0, handlesAsTypes);
        var newType = MethodType.methodType(func.type().returnType(), matcher.type().parameterType(0));
        var zeros = new int[func.type().parameterCount()];
        return MethodHandles.permuteArguments(pathsFolded, newType, zeros);
    }
}
