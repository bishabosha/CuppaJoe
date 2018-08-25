/*
 * Copyright (c) 2017. Jamie Thompson <bishbashboshjt@gmail.com>
 */

package com.github.bishabosha.cuppajoe.match.cases;

import com.github.bishabosha.cuppajoe.control.Option;
import com.github.bishabosha.cuppajoe.match.MatchException;

import java.util.Arrays;

public abstract class Case<I, O>  {

    private static final class CaseHolder {
        private static final Case<?, ?> EMPTY = new Case<>() {
            @Override
            public Object get(Object input) {
                throw new MatchException("Never matches!");
            }

            @Override
            public Option<Object> match(Object input) {
                return Option.empty();
            }
        };
    }

    public static <I, O> Case<I, O> combine(CombinatorCase<I, O> single) {
        return single;
    }

    @SafeVarargs
    public static <I, O> Case<I, O> combine(CombinatorCase<I, O>... cases) {
        return cases.length <= 1
                ? cases.length == 0
                    ? empty()
                    : combine(cases[0])
                : new CombinedCase<>(cases);
    }

    @SuppressWarnings("unchecked")
    public static <I, O> Case<I, O> empty() {
        return (Case<I, O>) CaseHolder.EMPTY;
    }

    public abstract O get(I input);
    public abstract Option<O> match(I input);

    public static abstract class CombinatorCase<I, O> extends Case<I, O> {
        public abstract boolean matches(I input);
        public abstract O extract(I input);
    }

    public final static class CombinedCase<I, O> extends Case<I, O> {
        private final CombinatorCase<I, O>[] cases;

        @SuppressWarnings("unchecked")
        private CombinedCase(CombinatorCase<I, O>[] cases) {
            this.cases = Arrays.copyOf(cases, cases.length);
        }

        @Override
        public O get(I input) {
            for (var caseOption: cases) {
                if (caseOption.matches(input)) {
                    return caseOption.extract(input);
                }
            }
            throw new MatchException(input);
        }

        @Override
        public Option<O> match(I input) {
            for (var caseOption: cases) {
                if (caseOption.matches(input)) {
                    return Option.some(caseOption.extract(input));
                }
            }
            return Option.empty();
        }
    }
}

