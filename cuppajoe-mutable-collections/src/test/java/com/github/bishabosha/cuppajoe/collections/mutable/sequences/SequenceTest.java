/*
 * Copyright (c) 2017. Jamie Thompson <bishbashboshjt@gmail.com>
 */

package com.github.bishabosha.cuppajoe.collections.mutable.sequences;

import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.function.Function;

import static org.junit.jupiter.api.Assertions.assertIterableEquals;

public class SequenceTest {

    @Test
    public void test_Terms() {
        Sequence<Integer> naturals = new NonRecursiveSequence<>(Function.identity());
        assertIterableEquals(
            List.of(1, 2, 3, 4, 5),
            naturals.terms(5)
        );
        assertIterableEquals(
            List.of(6, 7, 8, 9, 10),
            naturals.terms(6, 10)
        );
        assertIterableEquals(
            List.of(0, 1, 2, 3, 4),
            new RecursiveSequence<>(List::size)
                    .terms(5)
        );
        assertIterableEquals(
            List.of(5, 6, 7, 8, 9),
            new RecursiveSequence<>(List::size)
                    .terms(6, 10)
        );
    }

    @Test
    public void test_FilterTerms() {
        Sequence<Integer> naturals = new NonRecursiveSequence<>(Function.identity());
        assertIterableEquals(
            List.of(4, 5),
            naturals.filter(x -> x > 3).terms(5)
        );
        assertIterableEquals(
            List.of(1, 2, 3, 4, 5),
            naturals.removeRearNode()
        );
        assertIterableEquals(
            List.of(),
            naturals.filter(x -> x > 5).takeWhile(x -> x <= 5).terms(10)
        );
        assertIterableEquals(
            List.of(1, 2, 3, 4, 5),
            naturals.removeFrontNode()
        );
    }
}
