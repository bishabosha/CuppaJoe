/*
 * Copyright (c) 2017. Jamie Thompson <bishbashboshjt@gmail.com>
 */

package com.bishabosha.caffeine.sequences;

import com.bishabosha.caffeine.functional.Option;
import org.hamcrest.CoreMatchers;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;
import java.util.function.Function;

public class SequenceTest {

    @Test
    public void testTerms() {
        Sequence<Integer> naturals = new NonRecursiveSequence<>(Function.identity());
        Assert.assertThat(
            naturals.terms(5),
            CoreMatchers.hasItems(1, 2, 3, 4, 5)
        );
        Assert.assertThat(
            naturals.terms(6, 10),
            CoreMatchers.hasItems(6, 7, 8, 9, 10)
        );
        Assert.assertThat(
            new RecursiveSequence<>(List::size)
                .terms(5),
            CoreMatchers.hasItems(0, 1, 2, 3, 4)
        );
        Assert.assertThat(
            new RecursiveSequence<>(List::size)
                .terms(6, 10),
            CoreMatchers.hasItems(5, 6, 7, 8, 9)
        );
    }

    @Test
    public void testFilterTerms() {
        Sequence<Integer> naturals = new NonRecursiveSequence<>(Function.identity());
        Assert.assertThat(
            naturals.filter(x -> x > 3).terms(5),
            CoreMatchers.hasItems(4, 5)
        );
        Assert.assertThat(
            naturals.removeRearNode(),
            CoreMatchers.hasItems(1, 2, 3, 4, 5)
        );
        Assert.assertThat(
            naturals.filter(x -> x > 5).takeWhile(x -> x <= 5).terms(10),
            CoreMatchers.hasItems()
        );
        Assert.assertThat(
            naturals.removeFrontNode(),
            CoreMatchers.hasItems(1, 2, 3, 4, 5)
        );
    }

    @Test
    public void testFibonnacci() {
        Assert.assertThat(
            SequenceOf.fibonacci().terms(5),
            CoreMatchers.hasItems(1L, 1L, 2L, 3L, 5L)
        );
    }

    @Test
    public void testCubes() {
        Assert.assertThat(
            SequenceOf.cubes().terms(5),
            CoreMatchers.hasItems(1L, 8L, 27L, 64L, 125L)
        );
    }

    @Test
    public void testExponentials() {
        Assert.assertThat(
            SequenceOf.exponentials(2).terms(5),
            CoreMatchers.hasItems(2L, 4L, 8L, 16L, 32L)
        );
    }

    @Test
    public void testPrimes() {
        Assert.assertThat(
            SequenceOf.primes().terms(5),
            CoreMatchers.hasItems(2L, 3L, 5L, 7L, 11L)
        );
    }

    @Test
    public void testRange() {
        Assert.assertThat(
            SequenceOf.range(10L, 5L),
            CoreMatchers.hasItems(10L, 9L, 8L, 7L, 6L, 5L)
        );
        Assert.assertThat(
            SequenceOf.range(1000L, -500L, -1000L),
            CoreMatchers.hasItems(1000L, 500L, 0L, -500L, -1000L)
        );
        Assert.assertThat(
            SequenceOf.range(6L, 10L),
            CoreMatchers.hasItems(6L, 7L, 8L, 9L, 10L)
        );
        Assert.assertThat(
            SequenceOf.range(0L, 2L, 10L),
            CoreMatchers.hasItems(0L, 2L, 4L, 6L, 8L, 10L)
        );
    }

    @Test
    public void testEquality() {
        Sequence<Long> a = SequenceOf.powers(2).terms(5);
        Sequence<Long> b = SequenceOf.squares().terms(5);
        Assert.assertEquals(a, b);
        Assert.assertNotEquals(a, b.terms(5, 10));
    }

    @Test
    public void testTerm() {
        Assert.assertEquals(
            Option.of(25L),
            SequenceOf.squares().term(5) // non-recursive
        );
        Assert.assertEquals(
            Option.of(1000L),
            SequenceOf.cubes().terms(8, 11).term(10) // non-recursive
        );
        Assert.assertEquals(
            Option.of(25L),
            SequenceOf.integers().term(26) // recursive
        );
        Assert.assertEquals(
            Option.of(30L),
            SequenceOf.integers().terms(26, 35).term(31) // recursive
        );
        Assert.assertEquals(
            Option.of(30L),
            SequenceOf.integers().term(31) // recursive
        );
        try {
            SequenceOf.integers().terms(15, 20).term(21); // out with range
            Assert.fail("Expected Exception.");
        } catch (Exception e) {
            Assert.assertThat(e, CoreMatchers.instanceOf(IllegalArgumentException.class));
        }
        try {
            SequenceOf.integers().terms(15, 20).term(14); // out with range
            Assert.fail("Expected Exception.");
        } catch (Exception e) {
            Assert.assertThat(e, CoreMatchers.instanceOf(IllegalArgumentException.class));
        }
        try {
            SequenceOf.integers().terms(10).term(11); // out with range
            Assert.fail("Expected Exception.");
        } catch (Exception e) {
            Assert.assertThat(e, CoreMatchers.instanceOf(IllegalArgumentException.class));
        }
        try {
            SequenceOf.integers().terms(10).term(-1); // out with range
            Assert.fail("Expected Exception.");
        } catch (Exception e) {
            Assert.assertThat(e, CoreMatchers.instanceOf(IllegalArgumentException.class));
        }
    }
}
