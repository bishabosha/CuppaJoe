package com.github.bishabosha.cuppajoe.match.benchmark.tuples;

import com.github.bishabosha.cuppajoe.collections.immutable.tuples.Tuple8;
import com.github.bishabosha.cuppajoe.match.Case;
import org.openjdk.jmh.annotations.*;

import java.lang.reflect.Array;

import static com.github.bishabosha.cuppajoe.collections.immutable.API.Tuple;
import static com.github.bishabosha.cuppajoe.match.API.With;
import static com.github.bishabosha.cuppajoe.match.patterns.Standard.$;
import static com.github.bishabosha.cuppajoe.match.patterns.Collections.Tuple8$;
import static com.github.bishabosha.cuppajoe.match.patterns.Standard.__;

/**
 * match API 3.0
 * Tuple8Sum.sumCase                      avgt   15  0.900 ± 0.417   s/op
 * Tuple8Sum.sumCase_smallExtract_2first  avgt   15  0.940 ± 0.105   s/op
 * Tuple8Sum.sumCase_smallExtract_2last   avgt   15  1.346 ± 0.361   s/op
 * Tuple8Sum.sumCompose                   avgt   15  0.164 ± 0.002   s/op
 */
@Fork(3)
@Warmup(iterations = 5, time = 5)
@Measurement(iterations = 5, time = 10)
@BenchmarkMode(Mode.AverageTime)
@State(Scope.Thread)
public class Tuple8Sum {

    @State(Scope.Thread)
    public static class Tuple8State {

        static final int SIZE = 10_000_000;
        Tuple8<Integer, Integer, Integer, Integer, Integer, Integer, Integer, Integer>[] arr;

        @SuppressWarnings("unchecked")
        @Setup
        public void setup() {
            arr = (Tuple8<Integer, Integer, Integer, Integer, Integer, Integer, Integer, Integer>[]) Array.newInstance(Tuple8.class, SIZE);
            for (var i = 0; i < SIZE; i += 1) {
                arr[i] = Tuple(i, i+1, i+2, i+3, i+4, i+5, i+6, i+7);
            }
        }
    }

    @Benchmark
    public int sumCompose(Tuple8State state) {
        int sum = 0;
        for (var tuple: state.arr) {
            sum += tuple.compose((a, b, c, d, e, f, g, h) -> a + b + c + d + e + f + g+ h);
        }
        return sum;
    }

    @Benchmark
    public int sumCase(Tuple8State state) {
        int sum = 0;
        Case<Tuple8<Integer, Integer, Integer, Integer, Integer, Integer, Integer, Integer>, Integer> tupleCase;
        tupleCase = With(Tuple8$($(), $(), $(), $(), $(), $(), $(), $()), Tuple8Sum::sum);
        for (var tuple: state.arr) {
            sum += tupleCase.get(tuple);
        }
        return sum;
    }

    /**
     * Currently this is faster than the sumCase_smallExtract_2last, i.e. ideally they should be the same speed.
     * This is because on each eval -> a nested Result structure is created and walked; biased to the first branches
     * This is inefficient as both the number of params to extract and the method of extraction is constant at the time of case creation.
     * When creating a case, the patterns should be walked and flattened before use.
     * Flattened structure should have a heuristic where if the test passes then invoke the function directly, calling each typed param extractor
     * Extractors to variables within a nested pattern should share a root extractor for their parent object that is invoked exactly once
     */
    @Benchmark
    public int sumCase_smallExtract_2first(Tuple8State state) {
        int sum = 0;
        Case<Tuple8<Integer, Integer, Integer, Integer, Integer, Integer, Integer, Integer>, Integer> tupleCase;
        tupleCase = With(Tuple8$($(), $(), __(), __(), __(), __(), __(), __()), Tuple8Sum::sum2);
        for (var tuple: state.arr) {
            sum += tupleCase.get(tuple);
        }
        return sum;
    }

    @Benchmark
    public int sumCase_smallExtract_2last(Tuple8State state) {
        int sum = 0;
        Case<Tuple8<Integer, Integer, Integer, Integer, Integer, Integer, Integer, Integer>, Integer> tupleCase;
        tupleCase = With(Tuple8$(__(), __(), __(), __(), __(), __(), $(), $()), Tuple8Sum::sum2);
        for (var tuple: state.arr) {
            sum += tupleCase.get(tuple);
        }
        return sum;
    }

    private static int sum(int a, int b, int c, int d, int e, int f, int g, int h) {
        return a + b + c + d + e + f + g + h;
    }

    private static int sum2(int a, int b) {
        return a + b;
    }
}
