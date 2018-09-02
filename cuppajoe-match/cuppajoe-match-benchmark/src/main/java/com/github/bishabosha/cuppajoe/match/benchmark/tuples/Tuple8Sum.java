package com.github.bishabosha.cuppajoe.match.benchmark.tuples;

import com.github.bishabosha.cuppajoe.collections.immutable.tuples.Tuple8;
import com.github.bishabosha.cuppajoe.control.Option;
import com.github.bishabosha.cuppajoe.higher.functions.Func1;
import com.github.bishabosha.cuppajoe.higher.functions.Func8;
import com.github.bishabosha.cuppajoe.match.MatchException;
import com.github.bishabosha.cuppajoe.match.cases.Case;
import com.github.bishabosha.cuppajoe.match.cases.Case.CombinatorCase;
import org.openjdk.jmh.annotations.*;

import java.lang.reflect.Array;
import java.util.Objects;
import java.util.function.Predicate;

import static com.github.bishabosha.cuppajoe.collections.immutable.API.Tuple;
import static com.github.bishabosha.cuppajoe.match.API.With;
import static com.github.bishabosha.cuppajoe.match.patterns.Collections.tuple;
import static com.github.bishabosha.cuppajoe.match.patterns.Standard.__;
import static com.github.bishabosha.cuppajoe.match.patterns.Standard.id;

/**
 * match API 3.0
 * Tuple8Sum.sumCase                      avgt   15  0.785 ± 0.233   s/op
 * Tuple8Sum.sumCase_smallExtract_2first  avgt   15  0.742 ± 0.166   s/op
 * Tuple8Sum.sumCase_smallExtract_2last   avgt   15  1.226 ± 0.348   s/op
 * Tuple8Sum.sumCompose                   avgt   15  0.163 ± 0.006   s/op
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

    @Benchmark
    public int sumScalarised(Tuple8State state) {
        int sum = 0;
        for (var tuple: state.arr) {
            sum += (tuple.$1 + tuple.$2 + tuple.$3 + tuple.$4 + tuple.$5 + tuple.$6 + tuple.$7 + tuple.$8);
        }
        return sum;
    }

//    @Benchmark
    public int sumCompose(Tuple8State state) {
        int sum = 0;
        for (var tuple: state.arr) {
            sum += tuple.compose((a, b, c, d, e, f, g, h) -> a + b + c + d + e + f + g + h);
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

//    @Benchmark
    public int sumCaseOptimal(Tuple8State state) throws MatchException {
        int sum = 0;
        var tupleCase = sumComponentsOptimal();
        for (var tuple: state.arr) {
            sum += tupleCase.get(tuple);
        }
        return sum;
    }

//    @Benchmark
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

//    @Benchmark
    public int sum2FirstScalarised(Tuple8State state) {
        int sum = 0;
        for (var tuple: state.arr) {
            sum += (tuple.$1 + tuple.$2);
        }
        return sum;
    }

//    @Benchmark
    public int sum2LastScalarised(Tuple8State state) {
        int sum = 0;
        for (var tuple: state.arr) {
            sum += (tuple.$7 + tuple.$8);
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
