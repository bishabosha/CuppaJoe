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

package com.github.bishabosha.cuppajoe.match.benchmark.tuples;

import com.github.bishabosha.cuppajoe.collections.immutable.tuples.Tuple2;
import com.github.bishabosha.cuppajoe.match.Case;
import org.openjdk.jmh.annotations.*;

import java.lang.reflect.Array;

import static com.github.bishabosha.cuppajoe.collections.immutable.API.Tuple;
import static com.github.bishabosha.cuppajoe.match.API.With;
import static com.github.bishabosha.cuppajoe.match.patterns.Collections.Tuple2$;
import static com.github.bishabosha.cuppajoe.match.patterns.Standard.$;
import static com.github.bishabosha.cuppajoe.match.patterns.Standard.__;

@Fork(1)
@Warmup(iterations = 3, time = 5)
@Measurement(iterations = 3, time = 10)
@BenchmarkMode(Mode.AverageTime)
@State(Scope.Thread)
public class Tuple2NestedSum {

    @State(Scope.Thread)
    public static class Tuple2NestedState {

        static final int SIZE = 10_000_000;
        Tuple2<Tuple2<Integer, Integer>, Integer>[] arr;

        @SuppressWarnings("unchecked")
        @Setup
        public void setup() {
            arr = (Tuple2<Tuple2<Integer, Integer>, Integer>[]) Array.newInstance(Tuple2.class, SIZE);
            for (var i = 0; i < SIZE; i += 1) {
                arr[i] = Tuple(Tuple(i, i+1), i+2);
            }
        }
    }

//    @Benchmark
    public int sumScalarisedNested(Tuple2NestedState state) {
        int sum = 0;
        for (var tuple: state.arr) {
            var tupleNested = tuple.$1;
            sum += (tupleNested.$1 + tupleNested.$2);
        }
        return sum;
    }

//    @Benchmark
    public int sumConsumeNested(Tuple2NestedState state) {
        int sum = 0;
        for (var tuple: state.arr) {
            sum += tuple.compose((xy, _ignored) ->
                xy.compose((x, y) ->
                    x + y
                )
            );
        }
        return sum;
    }

    //    @Benchmark
    public int sumCaseNested(Tuple2NestedState state) {
        int sum = 0;
        Case<Tuple2<Tuple2<Integer, Integer>, Integer>, Integer> tupleCase = With(Tuple2$(Tuple2$($(), $()), __()), Tuple2NestedSum::sum);
        for (var tuple: state.arr) {
            sum += tupleCase.get(tuple);
        }
        return sum;
    }

    private static int sum(int x, int y) {
        return x + y;
    }
}
