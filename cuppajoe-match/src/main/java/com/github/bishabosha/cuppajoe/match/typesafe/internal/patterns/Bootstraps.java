package com.github.bishabosha.cuppajoe.match.typesafe.internal.patterns;


import com.github.bishabosha.cuppajoe.match.typesafe.patterns.Pattern.Empty;
import com.github.bishabosha.cuppajoe.match.typesafe.patterns.Pattern.Value;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.util.List;

import static com.github.bishabosha.cuppajoe.match.typesafe.patterns.Pattern.empty;
import static com.github.bishabosha.cuppajoe.match.typesafe.patterns.Pattern.value;

public final class Bootstraps {

    private Bootstraps() {}

    private static class PatternConstants {
        private static final Value ID = value(MethodHandleConstants.ALWAYS_TRUE);
        private static final Empty __ = empty(MethodHandleConstants.ALWAYS_TRUE);
    }

    private static class MethodHandleConstants {
        private static final MethodHandle TRUE_CONST;
        private static final MethodHandle ALWAYS_TRUE;

        static {
            TRUE_CONST = MethodHandles.constant(boolean.class, true);
            ALWAYS_TRUE = MethodHandles.dropArgumentsToMatch(TRUE_CONST, 0, List.of(Object.class), 0);
        }
    }

    /**
     * Can expand this to have a cache of Always True predicates for different parameter lists
     */
    public static boolean isAlwaysTrue(MethodHandle predicate) {
        return MethodHandleConstants.ALWAYS_TRUE == predicate;
    }

    public static Value id() {
        return PatternConstants.ID;
    }

    public static Empty __() {
        return PatternConstants.__;
    }

}
