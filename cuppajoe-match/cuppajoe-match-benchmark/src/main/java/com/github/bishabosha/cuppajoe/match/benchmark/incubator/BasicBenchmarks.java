package com.github.bishabosha.cuppajoe.match.benchmark.incubator;

import com.github.bishabosha.cuppajoe.collections.immutable.tuples.Tuple2;
import com.github.bishabosha.cuppajoe.control.Option;
import com.github.bishabosha.cuppajoe.match.Case;
import com.github.bishabosha.cuppajoe.match.incubator.Patterns;
import org.openjdk.jmh.annotations.*;

import java.lang.reflect.Array;

import static com.github.bishabosha.cuppajoe.API.None;
import static com.github.bishabosha.cuppajoe.API.Some;
import static com.github.bishabosha.cuppajoe.collections.immutable.API.Tuple;
import static com.github.bishabosha.cuppajoe.match.incubator.CaseFactory.with;
import static com.github.bishabosha.cuppajoe.match.incubator.Patterns.*;

@Fork(1)
@Warmup(iterations = 3, time = 5)
@Measurement(iterations = 3, time = 10)
@BenchmarkMode(Mode.AverageTime)
@State(Scope.Thread)
public class BasicBenchmarks {

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

    @State(Scope.Thread)
    public static class OptionState {

        static final int SIZE = 10_000_000;
        Option<Integer>[] arr;

        @SuppressWarnings("unchecked")
        @Setup
        public void setup() {
            arr = (Option<Integer>[]) Array.newInstance(Option.class, SIZE);
            for (var i = 0; i < SIZE; i += 1) {
                arr[i] = i % 2 == 0 ? Some(i) : None();
            }
        }
    }

//    @Benchmark
    public int sumCase(Tuple2State state) {
        int sum = 0;
        for (var tuple: state.arr) {
            sum += with(Patterns.<Integer, Integer>Tuple2$($(), $()), BasicBenchmarks::sum).get(tuple);
        }
        return sum;
    }

//    @Benchmark
    public int sumLeft(Tuple2State state) {
        int sum = 0;
        for (var tuple: state.arr) {
            sum += with(Patterns.<Integer, Integer>Tuple2$($(), __()), BasicBenchmarks::identity).get(tuple);
        }
        return sum;
    }

//    @Benchmark
    public int sumRight(Tuple2State state) {
        int sum = 0;
        for (var tuple: state.arr) {
            sum += with(Patterns.<Integer, Integer>Tuple2$(__(), $()), BasicBenchmarks::identity).get(tuple);
        }
        return sum;
    }

    @Benchmark
    public int sumCaseNested(Tuple2NestedState state) {
        int sum = 0;
        Case<Tuple2<Tuple2<Integer, Integer>, Integer>, Integer> tupleCase = with(Tuple2$(Tuple2$($(), $()), __()), BasicBenchmarks::sum);
        for (var tuple: state.arr) {
            sum += tupleCase.get(tuple);
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
