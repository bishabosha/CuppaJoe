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

package com.github.bishabosha.cuppajoe.higher.functions.tailcall;

import com.github.bishabosha.cuppajoe.higher.functions.TailCall;
import org.openjdk.jmh.annotations.*;

import java.util.Arrays;
import java.util.Iterator;
import java.util.stream.Collectors;

import static com.github.bishabosha.cuppajoe.higher.functions.TailCall.yield;

/**
 * Results on MacBookPro14,3 i7 3.1Ghz
 * # JMH version: 1.21
 * # VM version: JDK 11-ea, OpenJDK 64-Bit Server VM, 11-ea+25
 * # VM invoker: /Library/Java/JavaVirtualMachines/jdk-11.jdk/Contents/Home/bin/java
 * # VM options: -XX:+UnlockExperimentalVMOptions -XX:+EnableJVMCI -XX:+UseJVMCICompiler
 * # Warmup: 3 iterations, 5 s each
 * # Measurement: 5 iterations, 10 s each
 * # Timeout: 10 min per iteration
 * # Threads: 1 thread, will synchronize iterations
 * # Benchmark mode: Average time, time/op
 *
 * Benchmark                           Mode  Cnt  Score   Error  Units
 * StringConcatBenchmark.testLoop      avgt   15  0.173 ± 0.003   s/op
 * StringConcatBenchmark.testStream    avgt   15  0.222 ± 0.006   s/op
 * StringConcatBenchmark.testTailCall  avgt   15  0.233 ± 0.016   s/op
 */
@Fork(3)
@Warmup(iterations = 3, time = 5)
@Measurement(iterations = 5, time = 10)
@BenchmarkMode(Mode.AverageTime)
@State(Scope.Thread)
public class StringConcatBenchmark {

    @State(Scope.Thread)
    public static class ListState {
        private String[] list;

        @Setup
        public void setup() {
            var size = 10_000_000;
            list = new String[size];
            for (var i = 0; i < size; i++) {
                list[i] = "" + i;
            }
        }
    }

    @Benchmark
    public String testLoop(ListState state) {
        var builder = new StringBuilder();
        for (var string: state.list) {
            builder.append(string);
        }
        return builder.toString();
    }

    @Benchmark
    public String testStream(ListState state) {
        return Arrays.stream(state.list).collect(Collectors.joining());
    }

    @Benchmark
    public String testTailCall(ListState state) {
        return concatList(Arrays.asList(state.list).iterator(), new StringBuilder()).apply();
    }

    private static TailCall<String> concatList(Iterator<String> iterator, StringBuilder builder) {
        return iterator.hasNext()
            ? () -> concatList(iterator, builder.append(iterator.next()))
            : yield(builder.toString());
    }
}
