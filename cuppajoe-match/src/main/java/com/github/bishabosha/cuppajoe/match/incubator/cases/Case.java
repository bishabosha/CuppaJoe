/*
 * Copyright (c) 2017. Jamie Thompson <bishbashboshjt@gmail.com>
 */

package com.github.bishabosha.cuppajoe.match.incubator.cases;

import com.github.bishabosha.cuppajoe.control.Option;
import com.github.bishabosha.cuppajoe.match.incubator.MatchException;

@FunctionalInterface
public interface Case<I, O>  {

    static <I, O> Case<I, O> combine(CombinatorCase<I, O> single) {
        return single;
    }

    static <I, O> Case<I, O> combine(CombinatorCase<I, O>... cases) {
        return cases.length <= 1
                ? cases.length == 0
                    ? empty()
                    : combine(cases[0])
                : new CombinedCase<>(cases);
    }

    static <I, O> Case<I, O> empty() {
        return input -> Option.empty();
    }

    default O get(I input) throws MatchException {
        return match(input).orElseThrow(() -> new MatchException(input));
    }

    Option<O> match(I input);

    interface CombinatorCase<I, O> extends Case<I, O> {
        boolean matches(I input);
        O extract(I input);
    }

    final class CombinedCase<I, O> implements Case<I, O> {
        private final CombinatorCase<I, O>[] cases;

        private CombinedCase(CombinatorCase<I, O>[] cases) {
            this.cases = cases.clone();
        }

        @Override
        public O get(I input) throws MatchException {
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

