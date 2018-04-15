/*
 * Copyright (c) 2017. Jamie Thompson <bishbashboshjt@gmail.com>
 */

package com.github.bishabosha.cuppajoe.examples.sequences;

import com.github.bishabosha.cuppajoe.collections.mutable.sequences.NonRecursiveSequence;
import com.github.bishabosha.cuppajoe.collections.mutable.sequences.RecursiveSequence;
import com.github.bishabosha.cuppajoe.collections.mutable.sequences.Sequence;

import java.util.function.Supplier;

public class SequenceOf {
    public static Sequence<Integer> randomPalindromes(int minValue) {
        return new NonRecursiveSequence<>(x -> (int) (minValue / Math.random()))
                .filter(PredicateFor.isPalindromeInteger());
    }

    public static Sequence<Long> fibonacci() {
        return new RecursiveSequence<>(xs -> {
            if (xs.isEmpty()) {
                return (long) 1;
            }
            if (xs.size() == 1) {
                return (long) 1;
            }
            return xs.get(xs.size() - 1) + xs.get(xs.size() - 2);
        });
    }

    public static Sequence<Long> primes() {
        Supplier<Long> counter = new Supplier<>() {
            long count = 1;

            @Override
            public Long get() {
                return count += 2;
            }
        };
        return new RecursiveSequence<>(xs -> {
            if (0 == xs.size()) {
                return (long) 2;
            }
            while (true) {
                var value = counter.get();
                var sqrt = Math.sqrt(value);
                if (!PredicateFor.isMultipleOf(xs, x -> x > sqrt).test(value)) {
                    return value;
                }
            }
        });
    }

    public static Sequence<Long> range(long start, long end) {
        return range(start, start > end ? -1 : 1, end);
    }

    public static Sequence<Long> range(long start, long interval, long end) {
        return new NonRecursiveSequence<>(x -> start + (x - 1) * interval)
                .takeWhile(end <= start ? x -> x >= end : x -> x <= end);
    }

    public static Sequence<Long> naturals() {
        return new NonRecursiveSequence<>(Long::valueOf);
    }

    public static Sequence<Long> integers() {
        return new RecursiveSequence<>(x -> (long) x.size());
    }

    public static Sequence<Long> exponentials(int baseTerm) {
        return new NonRecursiveSequence<>(x -> (long) Math.pow(baseTerm, x));
    }

    public static Sequence<Long> powers(int power) {
        return new NonRecursiveSequence<>(x -> (long) Math.pow(x, power));
    }

    public static Sequence<Long> squares() {
        return powers(2);
    }

    public static Sequence<Long> cubes() {
        return powers(3);
    }
}
