package io.cuppajoe.collections.mutable.sequences;

import io.cuppajoe.control.Option;
import org.hamcrest.CoreMatchers;
import org.junit.Assert;
import org.junit.Test;

import static org.hamcrest.Matchers.contains;

public class SequenceOfTest {

    @Test
    public void testFibonnacci() {
        Assert.assertThat(
                SequenceOf.fibonacci().terms(5),
                contains(1L, 1L, 2L, 3L, 5L)
        );
    }

    @Test
    public void testCubes() {
        Assert.assertThat(
                SequenceOf.cubes().terms(5),
                contains(1L, 8L, 27L, 64L, 125L)
        );
    }

    @Test
    public void testExponentials() {
        Assert.assertThat(
                SequenceOf.exponentials(2).terms(5),
                contains(2L, 4L, 8L, 16L, 32L)
        );
    }

    @Test
    public void testPrimes() {
        Assert.assertThat(
                SequenceOf.primes().terms(5),
                contains(2L, 3L, 5L, 7L, 11L)
        );
    }

    @Test
    public void testRange() {
        Assert.assertThat(
                SequenceOf.range(10L, 5L),
                contains(10L, 9L, 8L, 7L, 6L, 5L)
        );
        Assert.assertThat(
                SequenceOf.range(1000L, -500L, -1000L),
                contains(1000L, 500L, 0L, -500L, -1000L)
        );
        Assert.assertThat(
                SequenceOf.range(6L, 10L),
                contains(6L, 7L, 8L, 9L, 10L)
        );
        Assert.assertThat(
                SequenceOf.range(0L, 2L, 10L),
                contains(0L, 2L, 4L, 6L, 8L, 10L)
        );
    }

    @Test
    public void testEquality() {
        var a = SequenceOf.powers(2).terms(5);
        var b = SequenceOf.squares().terms(5);
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