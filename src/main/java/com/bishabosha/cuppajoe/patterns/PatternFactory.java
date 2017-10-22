/*
 * Copyright (c) 2017. Jamie Thompson <bishbashboshjt@gmail.com>
 */

package com.bishabosha.cuppajoe.patterns;

import com.bishabosha.cuppajoe.control.Option;
import com.bishabosha.cuppajoe.functions.Func1;
import com.bishabosha.cuppajoe.tuples.Tuple2;
import com.bishabosha.cuppajoe.collections.mutable.lists.LinkedList;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.function.Function;
import java.util.function.Predicate;

import static com.bishabosha.cuppajoe.API.Tuple;

public class PatternFactory<I> {
    private Class<I> inputClass;

    public static <C> PatternFactory<C> patternFor(Class<C> clazz) {
        return new PatternFactory<>(clazz);
    }
    
    public <O> Pattern atomic(Pattern pattern, Func1<I, O> mapper) {
        return x -> Option.of(x)
                          .cast(inputClass)
                          .map(mapper)
                          .flatMap(pattern::test);
    }

    public <A, B> Pattern test2(
        Pattern p1, Function<I, A> g1,
        Pattern p2, Function<I, B> g2) {
            return testBase(x -> PatternResult.of(
                p1.test(g1.apply(x)).get(),
                p2.test(g2.apply(x)).get()
            ));
        }

    public <A, B, C> Pattern test3(
        Pattern p1, Function<I, A> g1,
        Pattern p2, Function<I, B> g2,
        Pattern p3, Function<I, C> g3) {
            return testBase(x -> PatternResult.of(
                p1.test(g1.apply(x)).get(),
                p2.test(g2.apply(x)).get(),
                p3.test(g3.apply(x)).get()
            ));
        }

    public <A, B, C, D> Pattern test4(
        Pattern p1, Function<I, A> g1,
        Pattern p2, Function<I, B> g2,
        Pattern p3, Function<I, C> g3,
        Pattern p4, Function<I, D> g4) {
            return testBase(x -> PatternResult.of(
                p1.test(g1.apply(x)).get(),
                p2.test(g2.apply(x)).get(),
                p3.test(g3.apply(x)).get(),
                p4.test(g4.apply(x)).get()
            ));
        }

    public <A, B, C, D, E> Pattern test5(
        Pattern p1, Function<I, A> g1,
        Pattern p2, Function<I, B> g2,
        Pattern p3, Function<I, C> g3,
        Pattern p4, Function<I, D> g4,
        Pattern p5, Function<I, E> g5) {
            return testBase(x -> PatternResult.of(
                p1.test(g1.apply(x)).get(),
                p2.test(g2.apply(x)).get(),
                p3.test(g3.apply(x)).get(),
                p4.test(g4.apply(x)).get(),
                p5.test(g5.apply(x)).get()
            ));
        }

    public <A, B, C, D, E, F> Pattern test6(
        Pattern p1, Function<I, A> g1,
        Pattern p2, Function<I, B> g2,
        Pattern p3, Function<I, C> g3,
        Pattern p4, Function<I, D> g4,
        Pattern p5, Function<I, E> g5,
        Pattern p6, Function<I, F> g6) {
            return testBase(x -> PatternResult.of(
                p1.test(g1.apply(x)).get(),
                p2.test(g2.apply(x)).get(),
                p3.test(g3.apply(x)).get(),
                p4.test(g4.apply(x)).get(),
                p5.test(g5.apply(x)).get(),
                p6.test(g6.apply(x)).get()
            ));
        }

    public <A, B, C, D, E, F, G> Pattern test7(
        Pattern p1, Function<I, A> g1,
        Pattern p2, Function<I, B> g2,
        Pattern p3, Function<I, C> g3,
        Pattern p4, Function<I, D> g4,
        Pattern p5, Function<I, E> g5,
        Pattern p6, Function<I, F> g6,
        Pattern p7, Function<I, G> g7) {
            return testBase(x -> PatternResult.of(
                p1.test(g1.apply(x)).get(),
                p2.test(g2.apply(x)).get(),
                p3.test(g3.apply(x)).get(),
                p4.test(g4.apply(x)).get(),
                p5.test(g5.apply(x)).get(),
                p6.test(g6.apply(x)).get(),
                p7.test(g7.apply(x)).get()
            ));
        }

    public <A, B, C, D, E, F, G, H> Pattern test8(
        Pattern p1, Function<I, A> g1,
        Pattern p2, Function<I, B> g2,
        Pattern p3, Function<I, C> g3,
        Pattern p4, Function<I, D> g4,
        Pattern p5, Function<I, E> g5,
        Pattern p6, Function<I, F> g6,
        Pattern p7, Function<I, G> g7,
        Pattern p8, Function<I, H> g8) {
            return testBase(x -> PatternResult.of(
                p1.test(g1.apply(x)).get(),
                p2.test(g2.apply(x)).get(),
                p3.test(g3.apply(x)).get(),
                p4.test(g4.apply(x)).get(),
                p5.test(g5.apply(x)).get(),
                p6.test(g6.apply(x)).get(),
                p7.test(g7.apply(x)).get(),
                p8.test(g8.apply(x)).get()
            ));
        }

    private Pattern testBase(Function<I, PatternResult> supplier) {
        return x -> {
            if (inputClass.isInstance(x)) {
                I toTest = inputClass.cast(x);
                try {
                    return Pattern.compile(supplier.apply(toTest));
                } catch (NoSuchElementException e) {
                    // fallthrough
                }
            }
            return Pattern.FAIL;
        };
    }

    private PatternFactory(Class<I> inputClass) {
        this.inputClass = inputClass;
    }
}
