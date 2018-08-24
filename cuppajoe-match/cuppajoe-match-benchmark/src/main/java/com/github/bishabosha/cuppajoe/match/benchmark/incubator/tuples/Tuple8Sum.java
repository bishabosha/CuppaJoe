package com.github.bishabosha.cuppajoe.match.benchmark.incubator.tuples;

import com.github.bishabosha.cuppajoe.collections.immutable.tuples.Tuple8;
import com.github.bishabosha.cuppajoe.control.Option;
import com.github.bishabosha.cuppajoe.higher.functions.Func1;
import com.github.bishabosha.cuppajoe.higher.functions.Func8;
import com.github.bishabosha.cuppajoe.match.incubator.Case;
import com.github.bishabosha.cuppajoe.match.incubator.Case.CombinatorCase;
import com.github.bishabosha.cuppajoe.match.incubator.MatchException;
import org.openjdk.jmh.annotations.*;

import java.lang.reflect.Array;
import java.util.Objects;
import java.util.function.Predicate;

import static com.github.bishabosha.cuppajoe.collections.immutable.API.Tuple;
import static com.github.bishabosha.cuppajoe.match.incubator.API.With;
import static com.github.bishabosha.cuppajoe.match.incubator.patterns.Standard.*;

/**
 * match API 4.0
 * c.g.b.c.m.b.incubator.tuples.Tuple8Sum.sumCase           avgt    5  0.273 ± 0.018   s/op
 * c.g.b.c.m.b.incubator.tuples.Tuple8Sum.sumCaseUnsafe     avgt    5  0.252 ± 0.030   s/op
 * c.g.b.c.m.b.incubator.tuples.Tuple8Sum.sumFuncs          avgt    5  0.169 ± 0.016   s/op
 * c.g.b.c.m.b.incubator.tuples.Tuple8Sum.sumFuncsIndirect  avgt    5  0.480 ± 0.022   s/op
 * c.g.b.c.m.b.tuples.Tuple8Sum.sum2FirstScalarised         avgt    5  0.136 ± 0.029   s/op
 * c.g.b.c.m.b.tuples.Tuple8Sum.sum2LastScalarised          avgt    5  0.128 ± 0.040   s/op
 * c.g.b.c.m.b.tuples.Tuple8Sum.sumCase                     avgt    5  2.057 ± 0.160   s/op
 * c.g.b.c.m.b.tuples.Tuple8Sum.sumCase_2first              avgt    5  1.353 ± 0.094   s/op
 * c.g.b.c.m.b.tuples.Tuple8Sum.sumCase_2last               avgt    5  0.744 ± 0.058   s/op
 * c.g.b.c.m.b.tuples.Tuple8Sum.sumCompose                  avgt    5  0.158 ± 0.032   s/op
 * c.g.b.c.m.b.tuples.Tuple8Sum.sumScalarised               avgt    5  0.169 ± 0.019   s/op
 */
@Fork(3)
@Warmup(iterations = 5, time = 1)
@Measurement(iterations = 5, time = 1)
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

    private static CombinatorCase<Tuple8<Integer, Integer, Integer, Integer, Integer, Integer, Integer, Integer>, Integer> sumComponents() {
        return With(tuple(id(), id(), id(), id(), id(), id(), id(), id()), Tuple8Sum::sum);
    }

    private static Case<Tuple8<Integer, Integer, Integer, Integer, Integer, Integer, Integer, Integer>, Integer> sumComponentsOptimal() {
        return new Case<>() {

            Predicate<Tuple8<Integer, Integer, Integer, Integer, Integer, Integer, Integer, Integer>> matcher = Objects::nonNull;
            Func8<Integer, Integer, Integer, Integer, Integer, Integer, Integer, Integer, Integer> sumFunc = Tuple8Sum::sum;
            Func1<Tuple8<Integer, Integer, Integer, Integer, Integer, Integer, Integer, Integer>, Integer> _1 = t -> t.$1;
            Func1<Tuple8<Integer, Integer, Integer, Integer, Integer, Integer, Integer, Integer>, Integer> _2 = t -> t.$2;
            Func1<Tuple8<Integer, Integer, Integer, Integer, Integer, Integer, Integer, Integer>, Integer> _3 = t -> t.$3;
            Func1<Tuple8<Integer, Integer, Integer, Integer, Integer, Integer, Integer, Integer>, Integer> _4 = t -> t.$4;
            Func1<Tuple8<Integer, Integer, Integer, Integer, Integer, Integer, Integer, Integer>, Integer> _5 = t -> t.$5;
            Func1<Tuple8<Integer, Integer, Integer, Integer, Integer, Integer, Integer, Integer>, Integer> _6 = t -> t.$6;
            Func1<Tuple8<Integer, Integer, Integer, Integer, Integer, Integer, Integer, Integer>, Integer> _7 = t -> t.$7;
            Func1<Tuple8<Integer, Integer, Integer, Integer, Integer, Integer, Integer, Integer>, Integer> _8 = t -> t.$8;

            @Override
            public Integer get(Tuple8<Integer, Integer, Integer, Integer, Integer, Integer, Integer, Integer> tuple) throws MatchException {
                if (matcher.test(tuple)) {
                    return sumFunc.apply(_1.apply(tuple), _2.apply(tuple), _3.apply(tuple), _4.apply(tuple), _5.apply(tuple), _6.apply(tuple), _7.apply(tuple), _8.apply(tuple));
                }
                throw new MatchException(tuple);
            }

            @Override
            public Option<Integer> match(Tuple8<Integer, Integer, Integer, Integer, Integer, Integer, Integer, Integer> tuple) {
                if (matcher.test(tuple)) {
                    return Option.some(sumFunc.apply(_1.apply(tuple), _2.apply(tuple), _3.apply(tuple), _4.apply(tuple), _5.apply(tuple), _6.apply(tuple), _7.apply(tuple), _8.apply(tuple)));
                }
                return Option.empty();
            }
        };
    }

    private static Case<Tuple8<Integer, Integer, Integer, Integer, Integer, Integer, Integer, Integer>, Integer> sumComponentsHandrolled(Func8<Integer, Integer, Integer, Integer, Integer, Integer, Integer, Integer, Integer> func) {
        return new Case<>() {

            @Override
            public Integer get(Tuple8<Integer, Integer, Integer, Integer, Integer, Integer, Integer, Integer> tuple) throws MatchException {
                if (tuple != null) {
                    return func.apply(tuple.$1, tuple.$2, tuple.$3, tuple.$4, tuple.$5, tuple.$6, tuple.$7, tuple.$8);
                }
                throw new MatchException("null");
            }

            @Override
            public Option<Integer> match(Tuple8<Integer, Integer, Integer, Integer, Integer, Integer, Integer, Integer> tuple) {
                if (tuple != null) {
                    return Option.some(func.apply(tuple.$1, tuple.$2, tuple.$3, tuple.$4, tuple.$5, tuple.$6, tuple.$7, tuple.$8));
                }
                return Option.empty();
            }
        };
    }

    private static CombinatorCase<Tuple8<Integer, Integer, Integer, Integer, Integer, Integer, Integer, Integer>, Integer> sumTwoFirst() {
        return With(tuple(id(), id(), __(), __(), __(), __(), __(), __()), Tuple8Sum::sum2);
    }

    private static CombinatorCase<Tuple8<Integer, Integer, Integer, Integer, Integer, Integer, Integer, Integer>, Integer> sumTwoLast() {
        return With(tuple(__(), __(), __(), __(), __(), __(), id(), id()), Tuple8Sum::sum2);
    }

//    @Benchmark
    public int sumCompose(Tuple8State state) {
        int sum = 0;
        for (var tuple: state.arr) {
            sum += tuple.compose((a, b, c, d, e, f, g, h) -> a + b + c + d + e + f + g+ h);
        }
        return sum;
    }

    @Benchmark
    public int sumCase(Tuple8State state) throws MatchException {
        int sum = 0;
        var tupleCase = sumComponents();
        for (var tuple: state.arr) {
            sum += tupleCase.get(tuple);
        }
        return sum;
    }

    private static final Case<Tuple8<Integer, Integer, Integer, Integer, Integer, Integer, Integer, Integer>, Integer> SUM_CASE = sumComponents();

    @Benchmark
    public int sumCaseStatic(Tuple8State state) throws MatchException {
        int sum = 0;
        for (var tuple: state.arr) {
            sum += SUM_CASE.get(tuple);
        }
        return sum;
    }

    @Benchmark
    public int sumCaseStaticMatch(Tuple8State state) {
        int sum = 0;
        for (var tuple: state.arr) {
            sum += SUM_CASE.match(tuple).get();
        }
        return sum;
    }

    @Benchmark
    public int sumCaseOptimal(Tuple8State state) throws MatchException {
        int sum = 0;
        var tupleCase = sumComponentsOptimal();
        for (var tuple: state.arr) {
            sum += tupleCase.get(tuple);
        }
        return sum;
    }

    @Benchmark
    public int sumCaseHandrolled(Tuple8State state) throws MatchException {
        int sum = 0;
        var tupleCase = sumComponentsHandrolled(Tuple8Sum::sum);
        for (var tuple: state.arr) {
            sum += tupleCase.get(tuple);
        }
        return sum;
    }

//    @Benchmark
    public int sumCase_2first(Tuple8State state) throws MatchException {
        int sum = 0;
        var tupleCase = sumTwoFirst();
        for (var tuple: state.arr) {
            sum += tupleCase.get(tuple);
        }
        return sum;
    }

//    @Benchmark
    public int sumCase_2last(Tuple8State state) throws MatchException {
        int sum = 0;
        var tupleCase = sumTwoLast();
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
