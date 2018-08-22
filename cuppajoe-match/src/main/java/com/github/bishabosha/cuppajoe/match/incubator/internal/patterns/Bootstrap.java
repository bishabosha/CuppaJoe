package com.github.bishabosha.cuppajoe.match.incubator.internal.patterns;

import com.github.bishabosha.cuppajoe.match.incubator.patterns.Pattern;
import com.github.bishabosha.cuppajoe.match.incubator.patterns.Pattern.Empty;
import com.github.bishabosha.cuppajoe.match.incubator.patterns.Pattern.Value;

/**
 * These pattern factory methods are permitted to be used for specialised treatment in the implementation to optimise
 */
public class Bootstrap {

    private static final Id<?> ID = new Id<>();
    private static final __<?> __ = new __<>();

    @SuppressWarnings("unchecked")
    public static <T> Pattern<T> id() {
        return (Id<T>) ID;
    }

    @SuppressWarnings("unchecked")
    public static <T> Pattern<T> __() {
        return (__<T>) __;
    }

    /**
     * Value Pattern that always matches
     */
    private static final class Id<T> extends Value<T> {
        private Id() {}

        @Override
        public boolean matches(T value) {
            return true;
        }
    }

    /**
     * Empty Pattern that always matches
     */
    private static final class __<T> extends Empty<T> {
        private __() {}

        @Override
        public boolean matches(T value) {
            return true;
        }
    }
}
