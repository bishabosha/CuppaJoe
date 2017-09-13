/*
 * Copyright (c) 2017. Jamie Thompson <bishbashboshjt@gmail.com>
 */

package com.bishabosha.caffeine.functional.patterns;

import com.bishabosha.caffeine.functional.control.Option;
import com.bishabosha.caffeine.functional.tuples.Tuple2;
import com.bishabosha.caffeine.lists.LinkedList;

import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;

import static com.bishabosha.caffeine.functional.API.Tuple;

public class PatternFactory<I> {
    private Class<I> inputClass;
    private List<Tuple2<Pattern, Function<I, ?>>> tests;
    private List<Predicate<I>> conditions;

    public static <C> PatternFactory<C> patternFor(Class<C> clazz) {
        return new PatternFactory<>(clazz);
    }
    
    public <O> Pattern conditionalAtomic(Predicate<I> condition,
                                         Function<I, O> mapper, Pattern pattern) {
        return x -> Option.of(x)
                          .cast(inputClass)
                          .filter(condition)
                          .map(mapper)
                          .flatMap(pattern::test);
    }
    
    public <O> Pattern atomic(Pattern pattern, Function<I, O> mapper) {
        return x -> Option.of(x)
                          .cast(inputClass)
                          .map(mapper)
                          .flatMap(pattern::test);
    }

    public <A, B> Pattern testTwo(
        Tuple2<Pattern, Function<I, A>> a,
        Tuple2<Pattern, Function<I, B>> b) {
            return testBase(x -> PatternResult.of(
                checkedFail(a.$1().test(a.$2().apply(x))),
                checkedFail(b.$1().test(b.$2().apply(x)))
            ));
        }

    public <A, B, C> Pattern testThree(
        Tuple2<Pattern, Function<I, A>> a,
        Tuple2<Pattern, Function<I, B>> b,
        Tuple2<Pattern, Function<I, C>> c) {
            return testBase(x -> PatternResult.of(
                checkedFail(a.$1().test(a.$2().apply(x))),
                checkedFail(b.$1().test(b.$2().apply(x))),
                checkedFail(c.$1().test(c.$2().apply(x)))
            ));
        }

    public <A, B, C, D> Pattern testFour(
        Tuple2<Pattern, Function<I, A>> a,
        Tuple2<Pattern, Function<I, B>> b,
        Tuple2<Pattern, Function<I, C>> c,
        Tuple2<Pattern, Function<I, D>> d) {
            return testBase(x -> PatternResult.of(
                checkedFail(a.$1().test(a.$2().apply(x))),
                checkedFail(b.$1().test(b.$2().apply(x))),
                checkedFail(c.$1().test(c.$2().apply(x))),
                checkedFail(d.$1().test(d.$2().apply(x)))
            ));
        }

    public <A, B, C, D, E> Pattern testFive(
        Tuple2<Pattern, Function<I, A>> a,
        Tuple2<Pattern, Function<I, B>> b,
        Tuple2<Pattern, Function<I, C>> c,
        Tuple2<Pattern, Function<I, D>> d,
        Tuple2<Pattern, Function<I, E>> e) {
            return testBase(x -> PatternResult.of(
                checkedFail(a.$1().test(a.$2().apply(x))),
                checkedFail(b.$1().test(b.$2().apply(x))),
                checkedFail(c.$1().test(c.$2().apply(x))),
                checkedFail(d.$1().test(d.$2().apply(x))),
                checkedFail(e.$1().test(e.$2().apply(x)))
            ));
        }

    private Pattern testBase(Function<I, PatternResult> supplier) {
        return x -> {
            if (inputClass.isInstance(x)) {
                I toTest = inputClass.cast(x);
                try {
                    return Pattern.compile(supplier.apply(toTest));
                } catch (Exception e) {
                    // fallthrough
                }
            }
            return Pattern.FAIL;
        };
    }

    private PatternFactory(Class<I> inputClass) {
        this.inputClass = inputClass;
    }

    public PatternFactory<I> addPrecondition(Predicate<I> condition) {
        if (conditions == null) {
            conditions = new LinkedList<>();
        }
        conditions.add(condition);
        return this;
    }

    public <O> PatternFactory<I> addTest(Pattern test, Function<I, O> extractor) {
        if (tests == null) {
            tests = new LinkedList<>();
        }
        tests.add(Tuple(test, extractor));
        return this;
    }

    public Pattern build() {
        final boolean hasTests = tests != null && tests.size() > 0;
        final boolean hasConditions = conditions != null && conditions.size() > 0;
        if (hasTests && hasConditions) {
            return buildAll();
        } else if (hasTests) {
            return buildRecursive();
        } else if (hasConditions) {
            return buildConditions();
        } else {
            return buildNothing();
        }
    }

    private Pattern buildNothing() {
        return x -> inputClass.isInstance(x) ?
                Pattern.bind(inputClass.cast(x)) :
                Pattern.FAIL;
    }

    private Pattern buildConditions() {
        return x -> {
            if (inputClass.isInstance(x)){
                I toTest = inputClass.cast(x);
                for (Predicate<I> condition: conditions) {
                    if (!condition.test(toTest)) {
                        return Pattern.FAIL;
                    }
                }
                return Pattern.bind(toTest);
            }
            return Pattern.FAIL;
        };
    }

    private Pattern buildRecursive() {
        return x -> {
            if (inputClass.isInstance(x)){
                return getRecursion(inputClass.cast(x));
            }
            return Pattern.FAIL;
        };
    }

    private Pattern buildAll() {
        return x -> {
            if (inputClass.isInstance(x)){
                I toTest = inputClass.cast(x);
                for (Predicate<I> condition: conditions) {
                    if (!condition.test(toTest)) {
                        return Pattern.FAIL;
                    }
                }
                return getRecursion(toTest);
            }
            return Pattern.FAIL;
        };
    }

    private Option<PatternResult> getRecursion(I toTest) {
        try {
            PatternResult result = PatternResult.create();
            tests.forEach(x -> result.add(
                checkedFail(x.$1().test(x.$2().apply(toTest)))
            ));
            return Pattern.compile(result);
        } catch (Exception e) {
            return Pattern.FAIL;
        }
    }

    private Object checkedFail(Option<?> toTest) throws IllegalArgumentException {
        if (!toTest.isSome()) {
            throw new IllegalArgumentException();
        }
        return toTest.get();
    }
}
