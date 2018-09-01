/*
 * Copyright (c) 2017. Jamie Thompson <bishbashboshjt@gmail.com>
 */

package com.github.bishabosha.cuppajoe.match.typesafe.cases;

import java.lang.invoke.MethodHandle;

public abstract class Case  {

//    private static final class CaseHolder {
//        private static final Case EMPTY = new Case() {
//            @Override
//            public MethodHandle get() {
//                throw new MatchException("Never matches!");
//            }
//
//            @Override
//            public MethodHandle match() {
//                return Option.empty();
//            }
//        };
//    }

//    public static  Case combine(CombinatorCase single) {
//        return single;
//    }
//
//    public static  Case combine(CombinatorCase case1, CombinatorCase case2) {
//        return new CombinedCase2(case1, case2);
//    }
//
//    public static  Case combine(CombinatorCase case1, CombinatorCase case2, CombinatorCase case3) {
//        return new CombinedCase3(case1, case2, case3);
//    }
//
//    public static  Case combine(CombinatorCase case1, CombinatorCase case2, CombinatorCase case3, CombinatorCase case4) {
//        return new CombinedCase4(case1, case2, case3, case4);
//    }
//
//    @SafeVarargs
//    public static  Case combine(CombinatorCase... cases) {
//        switch (cases.length) {
//            case 0:
//                return empty();
//            case 1:
//                return combine(cases[0]);
//            case 2:
//                return combine(cases[0], cases[1]);
//            case 3:
//                return combine(cases[0], cases[1], cases[2]);
//            case 4:
//                return combine(cases[0], cases[1], cases[2], cases[3]);
//            default:
//                return new CombinedCaseN(cases);
//        }
//    }

//    public static  Case combine(List<CombinatorCase> cases) {
//        return Case.combine(cases.reverse().toArray(CombinatorCase[]::new));
//    }

//    public static Case empty() {
//        return CaseHolder.EMPTY;
//    }

    public abstract MethodHandle get();
    public abstract MethodHandle match();

    public static abstract class CombinatorCase extends Case {
        public abstract MethodHandle matches();
        public abstract MethodHandle extract();
    }

//    public final static class CombinedCase2 extends Case {
//        private final CombinatorCase case1;
//        private final CombinatorCase case2;
//
//        private CombinedCase2(CombinatorCase case1, CombinatorCase case2) {
//            this.case1 = case1;
//            this.case2 = case2;
//        }
//
//        @Override
//        public O get(I input) {
//            if (case1.matches(input)) {
//                return case1.extract(input);
//            }
//            if (case2.matches(input)) {
//                return case2.extract(input);
//            }
//            throw new MatchException(input);
//        }
//
//        @Override
//        public Option<O> match(I input) {
//            if (case1.matches(input)) {
//                return Option.some(case1.extract(input));
//            }
//            if (case2.matches(input)) {
//                return Option.some(case2.extract(input));
//            }
//            return Option.empty();
//        }
//    }
//
//    public final static class CombinedCase3 extends Case {
//        private final CombinatorCase case1;
//        private final CombinatorCase case2;
//        private final CombinatorCase case3;
//
//        private CombinedCase3(CombinatorCase case1, CombinatorCase case2, CombinatorCase case3) {
//            this.case1 = case1;
//            this.case2 = case2;
//            this.case3 = case3;
//        }
//
//        @Override
//        public O get(I input) {
//            if (case1.matches(input)) {
//                return case1.extract(input);
//            }
//            if (case2.matches(input)) {
//                return case2.extract(input);
//            }
//            if (case3.matches(input)) {
//                return case3.extract(input);
//            }
//            throw new MatchException(input);
//        }
//
//        @Override
//        public Option<O> match(I input) {
//            if (case1.matches(input)) {
//                return Option.some(case1.extract(input));
//            }
//            if (case2.matches(input)) {
//                return Option.some(case2.extract(input));
//            }
//            if (case3.matches(input)) {
//                return Option.some(case3.extract(input));
//            }
//            return Option.empty();
//        }
//    }
//
//    public final static class CombinedCase4 extends Case {
//        private final CombinatorCase case1;
//        private final CombinatorCase case2;
//        private final CombinatorCase case3;
//        private final CombinatorCase case4;
//
//        private CombinedCase4(CombinatorCase case1, CombinatorCase case2, CombinatorCase case3, CombinatorCase case4) {
//            this.case1 = case1;
//            this.case2 = case2;
//            this.case3 = case3;
//            this.case4 = case4;
//        }
//
//        @Override
//        public O get(I input) {
//            if (case1.matches(input)) {
//                return case1.extract(input);
//            }
//            if (case2.matches(input)) {
//                return case2.extract(input);
//            }
//            if (case3.matches(input)) {
//                return case3.extract(input);
//            }
//            if (case4.matches(input)) {
//                return case4.extract(input);
//            }
//            throw new MatchException(input);
//        }
//
//        @Override
//        public Option<O> match(I input) {
//            if (case1.matches(input)) {
//                return Option.some(case1.extract(input));
//            }
//            if (case2.matches(input)) {
//                return Option.some(case2.extract(input));
//            }
//            if (case3.matches(input)) {
//                return Option.some(case3.extract(input));
//            }
//            if (case4.matches(input)) {
//                return Option.some(case4.extract(input));
//            }
//            return Option.empty();
//        }
//    }
//
//    public final static class CombinedCaseN extends Case {
//        private final CombinatorCase[] cases;
//
//        @SuppressWarnings("unchecked")
//        private CombinedCaseN(CombinatorCase[] cases) {
//            this.cases = Arrays.copyOf(cases, cases.length);
//        }
//
//        @Override
//        public O get(I input) {
//            for (var caseOption: cases) {
//                if (caseOption.matches(input)) {
//                    return caseOption.extract(input);
//                }
//            }
//            throw new MatchException(input);
//        }
//
//        @Override
//        public Option<O> match(I input) {
//            for (var caseOption: cases) {
//                if (caseOption.matches(input)) {
//                    return Option.some(caseOption.extract(input));
//                }
//            }
//            return Option.empty();
//        }
//    }
}

