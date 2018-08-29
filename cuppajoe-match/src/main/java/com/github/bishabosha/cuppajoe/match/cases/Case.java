/*
 * Copyright (c) 2017. Jamie Thompson <bishbashboshjt@gmail.com>
 */

package com.github.bishabosha.cuppajoe.match.cases;

import com.github.bishabosha.cuppajoe.collections.immutable.List;
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

    public static <I, O> Case<I, O> combine(CombinatorCase<I, O> case1, CombinatorCase<I, O> case2) {
        return new CombinedCase2<>(case1, case2);
    }

    public static <I, O> Case<I, O> combine(CombinatorCase<I, O> case1, CombinatorCase<I, O> case2, CombinatorCase<I, O> case3) {
        return new CombinedCase3<>(case1, case2, case3);
    }

    public static <I, O> Case<I, O> combine(CombinatorCase<I, O> case1, CombinatorCase<I, O> case2, CombinatorCase<I, O> case3, CombinatorCase<I, O> case4) {
        return new CombinedCase4<>(case1, case2, case3, case4);
    }

    @SafeVarargs
    public static <I, O> Case<I, O> combine(CombinatorCase<I, O>... cases) {
        switch (cases.length) {
            case 0:
                return empty();
            case 1:
                return combine(cases[0]);
            case 2:
                return combine(cases[0], cases[1]);
            case 3:
                return combine(cases[0], cases[1], cases[2]);
            case 4:
                return combine(cases[0], cases[1], cases[2], cases[3]);
            default:
                return new CombinedCaseN<>(cases);
        }
    }

    public static <I, O> Case<I, O> combine(List<CombinatorCase<I, O>> cases) {
        return Case.combine(cases.reverse().toArray(CombinatorCase[]::new));
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

    public final static class CombinedCase2<I, O> extends Case<I, O> {
        private final CombinatorCase<I, O> case1;
        private final CombinatorCase<I, O> case2;

        private CombinedCase2(CombinatorCase<I, O> case1, CombinatorCase<I, O> case2) {
            this.case1 = case1;
            this.case2 = case2;
        }

        @Override
        public O get(I input) {
            if (case1.matches(input)) {
                return case1.extract(input);
            }
            if (case2.matches(input)) {
                return case2.extract(input);
            }
            throw new MatchException(input);
        }

        @Override
        public Option<O> match(I input) {
            if (case1.matches(input)) {
                return Option.some(case1.extract(input));
            }
            if (case2.matches(input)) {
                return Option.some(case2.extract(input));
            }
            return Option.empty();
        }
    }

    public final static class CombinedCase3<I, O> extends Case<I, O> {
        private final CombinatorCase<I, O> case1;
        private final CombinatorCase<I, O> case2;
        private final CombinatorCase<I, O> case3;

        private CombinedCase3(CombinatorCase<I, O> case1, CombinatorCase<I, O> case2, CombinatorCase<I, O> case3) {
            this.case1 = case1;
            this.case2 = case2;
            this.case3 = case3;
        }

        @Override
        public O get(I input) {
            if (case1.matches(input)) {
                return case1.extract(input);
            }
            if (case2.matches(input)) {
                return case2.extract(input);
            }
            if (case3.matches(input)) {
                return case3.extract(input);
            }
            throw new MatchException(input);
        }

        @Override
        public Option<O> match(I input) {
            if (case1.matches(input)) {
                return Option.some(case1.extract(input));
            }
            if (case2.matches(input)) {
                return Option.some(case2.extract(input));
            }
            if (case3.matches(input)) {
                return Option.some(case3.extract(input));
            }
            return Option.empty();
        }
    }

    public final static class CombinedCase4<I, O> extends Case<I, O> {
        private final CombinatorCase<I, O> case1;
        private final CombinatorCase<I, O> case2;
        private final CombinatorCase<I, O> case3;
        private final CombinatorCase<I, O> case4;

        private CombinedCase4(CombinatorCase<I, O> case1, CombinatorCase<I, O> case2, CombinatorCase<I, O> case3, CombinatorCase<I, O> case4) {
            this.case1 = case1;
            this.case2 = case2;
            this.case3 = case3;
            this.case4 = case4;
        }

        @Override
        public O get(I input) {
            if (case1.matches(input)) {
                return case1.extract(input);
            }
            if (case2.matches(input)) {
                return case2.extract(input);
            }
            if (case3.matches(input)) {
                return case3.extract(input);
            }
            if (case4.matches(input)) {
                return case4.extract(input);
            }
            throw new MatchException(input);
        }

        @Override
        public Option<O> match(I input) {
            if (case1.matches(input)) {
                return Option.some(case1.extract(input));
            }
            if (case2.matches(input)) {
                return Option.some(case2.extract(input));
            }
            if (case3.matches(input)) {
                return Option.some(case3.extract(input));
            }
            if (case4.matches(input)) {
                return Option.some(case4.extract(input));
            }
            return Option.empty();
        }
    }

    public final static class CombinedCaseN<I, O> extends Case<I, O> {
        private final CombinatorCase<I, O>[] cases;

        @SuppressWarnings("unchecked")
        private CombinedCaseN(CombinatorCase<I, O>[] cases) {
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

