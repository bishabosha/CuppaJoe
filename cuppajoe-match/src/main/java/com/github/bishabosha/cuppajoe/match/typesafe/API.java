package com.github.bishabosha.cuppajoe.match.typesafe;

import com.github.bishabosha.cuppajoe.match.typesafe.cases.Case.CombinatorCase;
import com.github.bishabosha.cuppajoe.match.typesafe.cases.ExtractNCase;
import com.github.bishabosha.cuppajoe.match.typesafe.patterns.Pattern;

import java.lang.invoke.MethodHandle;

public final class API {

    private API() {
    }

    public static CombinatorCase With(Pattern pattern, MethodHandle func) {
        return new ExtractNCase(pattern, func);
    }
}
