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
import com.github.bishabosha.cuppajoe.match.patterns.Collections;
import org.openjdk.jmh.annotations.*;

import java.lang.reflect.Array;

import static com.github.bishabosha.cuppajoe.collections.immutable.API.Tuple;
import static com.github.bishabosha.cuppajoe.match.API.Match;
import static com.github.bishabosha.cuppajoe.match.API.With;
import static com.github.bishabosha.cuppajoe.match.patterns.Collections.tuple;
import static com.github.bishabosha.cuppajoe.match.patterns.Standard.__;
import static com.github.bishabosha.cuppajoe.match.patterns.Standard.id;

@Fork(1)
@Warmup(iterations = 3, time = 5)
@Measurement(iterations = 3, time = 10)
@BenchmarkMode(Mode.AverageTime)
@State(Scope.Thread)
public class Tuple2Sum {

    @State(Scope.Thread)
    public static class Tuple2State {

        static final int SIZE = 10_000_000;
        Tuple2<Integer, Integer>[] arr;

        @SuppressWarnings("unchecked")
        @Setup
        public void setup() {
            arr = (Tuple2<Integer, Integer>[]) Array.newInstance(Tuple2.class, SIZE);
            for (var i = 0; i < SIZE; i += 1) {
                arr[i] = Tuple(i, i+1);
            }
        }
    }

//    @Benchmark
    public int sumScalarised(Tuple2State state) {
        int sum = 0;
        for (var tuple: state.arr) {
            sum += (tuple.$1 + tuple.$2);
        }
        return sum;
    }

//    @Benchmark
    public int sumCompose(Tuple2State state) {
        int sum = 0;
        for (var tuple: state.arr) {
            sum += tuple.compose((x, y) -> x + y);
        }
        return sum;
    }

//    @Benchmark
    public int sumComposeLeft(Tuple2State state) {
        int sum = 0;
        for (var tuple: state.arr) {
            sum += tuple.compose((x, _ignored) -> x);
        }
        return sum;
    }

    //    @Benchmark
    public int sumCase(Tuple2State state) {
        int sum = 0;
        for (var tuple: state.arr) {
            sum += With(Collections.<Integer, Integer>tuple(id(), id()), Tuple2Sum::sum).get(tuple);
        }
        return sum;
    }

    //    @Benchmark
    public int sumLeft(Tuple2State state) {
        int sum = 0;
        for (var tuple: state.arr) {
            sum += With(Collections.<Integer, Integer>tuple(id(), __()), Tuple2Sum::identity).get(tuple);
        }
        return sum;
    }

    //    @Benchmark
    public int sumRight(Tuple2State state) {
        int sum = 0;
        for (var tuple: state.arr) {
            sum += With(Collections.<Integer, Integer>tuple(__(), id()), Tuple2Sum::identity).get(tuple);
        }
        return sum;
    }

    //    @Benchmark
    public int sumMatcher(Tuple2State state) {
        int sum = 0;
        for (var tuple: state.arr) {
            sum += Match(tuple).of(
                With(tuple(id(), id()), Tuple2Sum::sum)
            );
        }
        return sum;
    }

    private static int sum(int x, int y) {
        return x + y;
    }

    private static int identity(int x) {
        return x;
    }
}