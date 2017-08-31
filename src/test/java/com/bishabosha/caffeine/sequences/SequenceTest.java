/*
 * Copyright (c) 2017. Jamie Thompson <bishbashboshjt@gmail.com>
 */

package com.bishabosha.caffeine.sequences;

import com.bishabosha.caffeine.base.Iterables;
import com.bishabosha.caffeine.functional.Option;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.function.Function;

public class SequenceTest {

    @Test
    void testTerms() {
        Sequence<Integer> naturals = new NonRecursiveSequence<>(Function.identity());
        Assertions.assertIterableEquals(
            Iterables.of(1, 2, 3, 4, 5),
            naturals.terms(5)
        );
        Assertions.assertIterableEquals(
            Iterables.of(6, 7, 8, 9, 10),
            naturals.terms(6, 10)
        );
        Assertions.assertIterableEquals(
            Iterables.of(0, 1, 2, 3, 4),
            new RecursiveSequence<>(xs -> xs.size())
                .terms(5)
        );
        Assertions.assertIterableEquals(
            Iterables.of(5, 6, 7, 8, 9),
            new RecursiveSequence<>(xs -> xs.size())
                .terms(6, 10)
        );
    }

    @Test
    void testFilterTerms() {
        Sequence<Integer> naturals = new NonRecursiveSequence<>(Function.identity());
        Assertions.assertIterableEquals(
            Iterables.of(4, 5),
            naturals.filter(x -> x > 3).terms(5)
        );
        Assertions.assertIterableEquals(
            Iterables.of(1, 2, 3, 4, 5),
            naturals.removeRearNode()
        );
        Assertions.assertIterableEquals(
            Iterables.of(),
            naturals.filter(x -> x > 5).takeWhile(x -> x <= 5).terms(10)
        );
        Assertions.assertIterableEquals(
            Iterables.of(1, 2, 3, 4, 5),
            naturals.removeFrontNode()
        );
    }

    @Test
    void testFibonnacci() {
        Assertions.assertIterableEquals(
            Iterables.of(1L, 1L, 2L, 3L, 5L),
            SequenceOf.fibonacci().terms(5)
        );
    }

    @Test
    void testCubes() {
        Assertions.assertIterableEquals(
            Iterables.of(1L, 8L, 27L, 64L, 125L),
            SequenceOf.cubes().terms(5)
        );
    }

    @Test
    void testExponentials() {
        Assertions.assertIterableEquals(
            Iterables.of(2L, 4L, 8L, 16L, 32L),
            SequenceOf.exponentials(2).terms(5)
        );
    }

    @Test
    void testPrimes() {
        Assertions.assertIterableEquals(
            Iterables.of(2L, 3L, 5L, 7L, 11L),
            SequenceOf.primes().terms(5)
        );
    }

    @Test
    void testRange() {
        Assertions.assertIterableEquals(
            Iterables.of(10L, 9L, 8L, 7L, 6L, 5L),
            SequenceOf.range(10L, 5L)
        );
        Assertions.assertIterableEquals(
            Iterables.of(1000L, 500L, 0L, -500L, -1000L),
            SequenceOf.range(1000L, -500L, -1000L)
        );
        Assertions.assertIterableEquals(
            Iterables.of(6L, 7L, 8L, 9L, 10L),
            SequenceOf.range(6L, 10L)
        );
        Assertions.assertIterableEquals(
            Iterables.of(0L, 2L, 4L, 6L, 8L, 10L),
            SequenceOf.range(0L, 2L, 10L)
        );
    }

    @Test
    void testEquality() {
        Sequence<Long> a = SequenceOf.powers(2).terms(5);
        Sequence<Long> b = SequenceOf.squares().terms(5);
        Assertions.assertEquals(a, b);
        Assertions.assertNotEquals(a, b.terms(5, 10));
    }

    @Test
    void testTerm() {
        Assertions.assertEquals(
            Option.of(25L),
            SequenceOf.squares().term(5) // non-recursive
        );
        Assertions.assertEquals(
            Option.of(1000L),
            SequenceOf.cubes().terms(8, 11).term(10) // non-recursive
        );
        Assertions.assertEquals(
            Option.of(25L),
            SequenceOf.integers().term(26) // recursive
        );
        Assertions.assertEquals(
            Option.of(30L),
            SequenceOf.integers().terms(26, 35).term(31) // recursive
        );
        Assertions.assertEquals(
            Option.of(30L),
            SequenceOf.integers().term(31) // recursive
        );
        Assertions.assertThrows(
            IllegalArgumentException.class,
            () -> SequenceOf.integers().terms(15, 20).term(21) // out with range
        );
        Assertions.assertThrows(
            IllegalArgumentException.class,
            () -> SequenceOf.integers().terms(15, 20).term(14) // out with range
        );
        Assertions.assertThrows(
            IllegalArgumentException.class,
            () -> SequenceOf.integers().terms(10).term(11) // out with range
        );
        Assertions.assertThrows(
            IllegalArgumentException.class,
            () -> SequenceOf.integers().terms(10).term(-1) // out with range
        );
    }
}
