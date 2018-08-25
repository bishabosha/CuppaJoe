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

import com.github.bishabosha.cuppajoe.match.cases.Case;
import org.openjdk.jmh.annotations.*;

import static com.github.bishabosha.cuppajoe.match.API.Match;
import static com.github.bishabosha.cuppajoe.match.API.With;
import static com.github.bishabosha.cuppajoe.match.patterns.Standard.id;

/**
 * SIZE = 100_000_000
 * Benchmark           Mode  Cnt  Score    Error  Units
 * Basic.sumCase       avgt   15  0.054 ±  0.001   s/op
 * Basic.sumMatcher    avgt   15  3.823 ±  1.117   s/op
 * Basic.sumPrimitive  avgt   15  0.058 ±  0.004   s/op
 */
@Fork(3)
@Warmup(iterations = 5, time = 1)
@Measurement(iterations = 5, time = 1)
@BenchmarkMode(Mode.AverageTime)
@State(Scope.Thread)
public class Basic {

    @State(Scope.Thread)
    public static class IntArrayState {
        static final int SIZE = 100_000_000;
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

    @Benchmark
    public int sumPrimitive(IntArrayState state) {
        int sum = 0;
        for (var num: state.arr) {
            sum += num;
        }
        return sum;
    }

    private static Case<Integer, Integer> intCase() {
        return With(id(), Basic::identity);
    }

    @Benchmark
    public int sumCase(IntArrayState state) {
        int sum = 0;
        final var intCase = intCase();
        for (var tuple: state.arr) {
            sum += intCase.get(tuple);
        }
        return sum;
    }

    /**
     * @apiNote Do not use matcher in tight loops
     */
    @Benchmark
    public int sumMatcher(IntArrayState state) {
        int sum = 0;
        for (var tuple: state.arr) {
            sum += Match(tuple).get(
                With(id(), (Integer x) -> x)
            );
        }
        return sum;
    }

    private static int identity(int i) {
        return i;
    }
}
