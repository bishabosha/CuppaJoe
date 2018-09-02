package com.github.bishabosha.cuppajoe.match.benchmark.typesafe;

import com.github.bishabosha.cuppajoe.collections.immutable.tuples.Tuple8;
import com.github.bishabosha.cuppajoe.match.MatchException;
import com.github.bishabosha.cuppajoe.match.typesafe.cases.Case.CombinatorCase;
import org.openjdk.jmh.annotations.*;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.reflect.Array;

import static com.github.bishabosha.cuppajoe.collections.immutable.API.Tuple;
import static com.github.bishabosha.cuppajoe.match.typesafe.API.With;
import static com.github.bishabosha.cuppajoe.match.typesafe.patterns.Collections.tuple;
import static com.github.bishabosha.cuppajoe.match.typesafe.patterns.Standard.__;
import static com.github.bishabosha.cuppajoe.match.typesafe.patterns.Standard.id;

/**
 * performance is awful on Graal, C2 produces native performance
 * Benchmark                                      Mode  Cnt  Score   Error  Units
 * c.g.b.c.m.b.tuples.Tuple8Sum.sumCase           avgt   15  0.237 ± 0.054   s/op
 * c.g.b.c.m.b.tuples.Tuple8Sum.sumScalarised     avgt   15  0.163 ± 0.003   s/op
 * c.g.b.c.m.b.typesafe.Tuple8Sum.sumCase         avgt   15  0.162 ± 0.014   s/op
 * c.g.b.c.m.b.typesafe.Tuple8Sum.sumCaseExtract  avgt   15  0.161 ± 0.008   s/op
 * c.g.b.c.m.b.typesafe.Tuple8Sum.sumCase_2first  avgt   15  0.133 ± 0.012   s/op
 * c.g.b.c.m.b.typesafe.Tuple8Sum.sumCase_2last   avgt   15  0.120 ± 0.015   s/op
 */
@Fork(3)
@Warmup(iterations = 5, time = 2)
@Measurement(iterations = 5, time = 2)
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

    private static class Handles {
        private static final MethodHandle SUM_8;
        private static final MethodHandle SUM_2;

        static {
            var lookup = MethodHandles.lookup();
            try {
                SUM_8 = lookup.findStatic(Tuple8Sum.class, "sum", MethodType.methodType(int.class, int.class, int.class, int.class, int.class, int.class, int.class, int.class, int.class));
                SUM_2 = lookup.findStatic(Tuple8Sum.class, "sum2", MethodType.methodType(int.class, int.class, int.class));
            } catch (NoSuchMethodException | IllegalAccessException e) {
                throw new Error(e);
            }
        }
    }

    private static class Cases {
        private static final MethodHandle SUM_8_GET = sumComponents().get();
        private static final MethodHandle SUM_8_EXTRACT = sumComponents().extract();
        private static final MethodHandle SUM_2_LEFT_GET = sumTwoFirst().get();
        private static final MethodHandle SUM_2_RIGHT_GET = sumTwoLast().get();

        private static CombinatorCase sumComponents() {
            return With(tuple(id(), id(), id(), id(), id(), id(), id(), id()), Handles.SUM_8);
        }

        private static CombinatorCase sumTwoFirst() {
            return With(tuple(id(), id(), __(), __(), __(), __(), __(), __()), Handles.SUM_2);
        }

        private static CombinatorCase sumTwoLast() {
            return With(tuple(__(), __(), __(), __(), __(), __(), id(), id()), Handles.SUM_2);
        }
    }

    @Benchmark
    public int sumCase(Tuple8State state) throws MatchException {
        int sum = 0;
        for (var tuple: state.arr) {
            try {
                sum += (int)Cases.SUM_8_GET.invokeExact((Object)tuple);
            } catch (Throwable throwable) {
                throw new MatchException(throwable);
            }
        }
        return sum;
    }

    @Benchmark
    public int sumCaseExtract(Tuple8State state) throws MatchException {
        int sum = 0;
        for (var tuple: state.arr) {
            try {
                sum += (int)Cases.SUM_8_EXTRACT.invokeExact(tuple);
            } catch (Throwable throwable) {
                throw new MatchException(throwable);
            }
        }
        return sum;
    }

    @Benchmark
    public int sumCase_2first(Tuple8State state) throws MatchException {
        int sum = 0;
        for (var tuple: state.arr) {
            try {
                sum += (int) Cases.SUM_2_LEFT_GET.invokeExact((Object) tuple);
            } catch (Throwable throwable) {
                throw new MatchException(throwable);
            }
        }
        return sum;
    }

    @Benchmark
    public int sumCase_2last(Tuple8State state) throws MatchException {
        int sum = 0;
        for (var tuple: state.arr) {
            try {
                sum += (int)Cases.SUM_2_RIGHT_GET.invokeExact((Object)tuple);
            } catch (Throwable throwable) {
                throw new MatchException(throwable);
            }
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
