/*
 * Copyright (c) 2017. Jamie Thompson <bishbashboshjt@gmail.com>
 */

package com.bishabosha.caffeine.sequences;

import com.bishabosha.caffeine.functional.API;
import com.bishabosha.caffeine.functional.control.Option;
import com.bishabosha.caffeine.pipelines.Pipeline;

import java.util.function.Function;

import static com.bishabosha.caffeine.functional.API.Option;

public class NonRecursiveSequence<T> extends AbstractSequence<T> {

    public NonRecursiveSequence(Function<Integer, T> algorithm) {
        builder = new NonRecursiveBuilder(algorithm);
    }

    private class NonRecursiveBuilder extends AbstractBuilder<T> {
        int offset = 0;
        private Function<Integer, T> algorithm;

        NonRecursiveBuilder(Function<Integer, T> algorithm) {
            this.algorithm = algorithm;
        }

        @Override
        protected T generateTerm() {
            return calculate(n);
        }

        public void setTerms(int from) {
            int old = initialTerm;
            if (from != old) {
                n = offset = from - 1;
                cache = getEmptyList();
            }
        }

        public T calculate(int n) {
            return algorithm.apply(n);
        }

        @Override
        protected void setUpIterator() {
            n = offset;
        }

        @Override
        protected int incN() {
            return n++ - offset;
        }

        @Override
        protected int getN() {
            return n - offset - 1;
        }

        @Override
        public T buildAndReturn(int term) {
            while (n < term) {
                getNextValue();
            }
            return cache.get(term-1-offset);
        }
    }

    @Override
    public Option<T> term(int n) {
        removeOffset(n);
        return getPipeline().isEmpty() ?
            Option(((NonRecursiveBuilder)builder).calculate(n)) :
            super.term(n);
    }

    @Override
    public Sequence<T> terms(int from, int to) {
        ((NonRecursiveBuilder)builder).setTerms(from);
        return super.terms(from, to);
    }

    @Override
    public Pipeline<T> pipeline() {
        Pipeline<T> stream = copyPipeline();
        if (finalTerm > initialTerm) {
            stream.pushLimit(finalTerm - initialTerm);
        }
        return stream;
    }
}
