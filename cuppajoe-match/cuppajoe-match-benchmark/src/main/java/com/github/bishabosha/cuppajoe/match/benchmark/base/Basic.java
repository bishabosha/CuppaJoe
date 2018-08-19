/*
 * Copyright (c) 2014, Oracle America, Inc.
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 *  * Redistributions of source code must retain the above copyright notice,
 *    this list of conditions and the following disclaimer.
 *
 *  * Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in the
 *    documentation and/or other materials provided with the distribution.
 *
 *  * Neither the name of Oracle nor the names of its contributors may be used
 *    to endorse or promote products derived from this software without
 *    specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF
 * THE POSSIBILITY OF SUCH DAMAGE.
 */

package com.github.bishabosha.cuppajoe.match.benchmark.base;

import com.github.bishabosha.cuppajoe.match.patterns.Pattern;
import com.github.bishabosha.cuppajoe.match.patterns.Result;
import com.github.bishabosha.cuppajoe.match.patterns.ResultVisitor;
import org.openjdk.jmh.annotations.*;

import static com.github.bishabosha.cuppajoe.match.API.Match;
import static com.github.bishabosha.cuppajoe.match.API.With;
import static com.github.bishabosha.cuppajoe.match.patterns.Standard.id;

@Fork(1)
@Warmup(iterations = 3, time = 5)
@Measurement(iterations = 3, time = 5)
@BenchmarkMode(Mode.AverageTime)
@State(Scope.Thread)
public class Basic {

    @State(Scope.Thread)
    public static class IntArrayState {
        static final int SIZE = 1_000_000_000;
        int[] arr;

        @SuppressWarnings("unchecked")
        @Setup
        public void setup() {
            arr = new int[SIZE];
            for (var i = 0; i < SIZE; i += 1) {
                arr[i] = i;
            }
        }
    }

    /**
     * Benchmark           Mode  Cnt  Score   Error  Units
     * Basic.sumPrimitive  avgt    3  0.578 ± 0.324   s/op
     */
//    @Benchmark
    public int sumPrimitive(IntArrayState state) {
        int sum = 0;
        for (var num: state.arr) {
            sum += num;
        }
        return sum;
    }

    /**
     * Benchmark         Mode  Cnt  Score   Error  Units
     * Basic.sumPattern  avgt    3  0.569 ± 0.015   s/op
     */
//    @Benchmark
    public int sumPattern(IntArrayState state) {
       int sum = 0;
       Pattern<Integer> pattern = id();
       for (var num: state.arr) {
           sum += SingleNumExtractor.extract(pattern.test(num).get());
       }
       return sum;
    }

    /**
     * Benchmark      Mode  Cnt  Score   Error  Units
     * Basic.sumCase  avgt    3  0.546 ± 0.259   s/op
     */
//    @Benchmark
    public int sumCase(IntArrayState state) {
        int sum = 0;
        for (var tuple: state.arr) {
            sum += With(id(), (Integer x) -> x).get(tuple);
        }
        return sum;
    }

    /**
     * Benchmark         Mode  Cnt  Score   Error  Units
     * Basic.sumMatcher  avgt    3  0.565 ± 0.226   s/op
     */
//    @Benchmark
    public int sumMatcher(IntArrayState state) {
        int sum = 0;
        for (var tuple: state.arr) {
            sum += Match(tuple).of(
                With(id(), (Integer x) -> x)
            );
        }
        return sum;
    }

    private static class SingleNumExtractor extends ResultVisitor {

        private static int extract(Result result) {
            var extractor = new SingleNumExtractor();
            result.accept(extractor);
            return extractor.getInt();
        }

        boolean uninitialised = true;
        int val;

        @Override
        public void onValue(Object o) {
            val = (int) o;
            uninitialised = false;
        }

        @Override
        public boolean uninitialised() {
            return uninitialised;
        }

        private int getInt() {
            if (uninitialised) {
                throw new IllegalStateException();
            }
            return val;
        }
    }
}
