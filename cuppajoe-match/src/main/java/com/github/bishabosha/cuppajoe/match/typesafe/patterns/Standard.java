package com.github.bishabosha.cuppajoe.match.typesafe.patterns;


import com.github.bishabosha.cuppajoe.match.typesafe.internal.patterns.Bootstraps;
import com.github.bishabosha.cuppajoe.match.typesafe.patterns.Pattern.Empty;
import com.github.bishabosha.cuppajoe.match.typesafe.patterns.Pattern.Value;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;

import static com.github.bishabosha.cuppajoe.match.typesafe.patterns.Pattern.empty;

public final class Standard {

    private Standard() {}

    private static class Handles {
        private static final MethodHandle REF_EQ;
        private static final MethodHandle EQ_NULL;

        static {
            var lookup = MethodHandles.lookup();
            try {
                REF_EQ = lookup.findStatic(Handles.class, "refEq", MethodType.methodType(boolean.class, Object.class, Object.class));
                EQ_NULL = REF_EQ.bindTo(null);
            } catch (NoSuchMethodException | IllegalAccessException e) {
               throw new Error(e);
            }
        }

        private static boolean refEq(Object o, Object ref) {
            return o == ref;
        }
    }

    public static Value id() {
        return Bootstraps.id();
    }

    public static Empty __() {
        return Bootstraps.__();
    }

    public static <O> Empty is(O toMatch) {
        return toMatch == null ? nil() : empty(Handles.REF_EQ.bindTo(toMatch));
    }

    public static Empty nil() {
        return empty(Handles.EQ_NULL);
    }

}
