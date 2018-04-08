package com.github.bishabosha.cuppajoe.collections.mutable.sequences;

import com.github.bishabosha.cuppajoe.control.Option;
import com.github.bishabosha.cuppajoe.examples.sequences.SequenceOf;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class SequenceOfTest {

    @Test
    public void test_Fibonnacci() {
        assertIterableEquals(
            List.of(1L, 1L, 2L, 3L, 5L),
            SequenceOf.fibonacci().terms(5)
        );
    }

    @Test
    public void test_Cubes() {
        assertIterableEquals(
            List.of(1L, 8L, 27L, 64L, 125L),
            SequenceOf.cubes().terms(5)
        );
    }

    @Test
    public void test_Exponentials() {
        assertIterableEquals(
            List.of(2L, 4L, 8L, 16L, 32L),
            SequenceOf.exponentials(2).terms(5)
        );
    }

    @Test
    public void test_Primes() {
        assertIterableEquals(
            List.of(2L, 3L, 5L, 7L, 11L),
            SequenceOf.primes().terms(5)
        );
    }

    @Test
    public void test_Range() {
        assertIterableEquals(
            List.of(10L, 9L, 8L, 7L, 6L, 5L),
            SequenceOf.range(10L, 5L)
        );
        assertIterableEquals(
            List.of(1000L, 500L, 0L, -500L, -1000L),
            SequenceOf.range(1000L, -500L, -1000L)
        );
        assertIterableEquals(
            List.of(6L, 7L, 8L, 9L, 10L),
            SequenceOf.range(6L, 10L)
        );
        assertIterableEquals(
            List.of(0L, 2L, 4L, 6L, 8L, 10L),
            SequenceOf.range(0L, 2L, 10L)
        );
    }

    @Test
    public void test_Equality() {
        var a = SequenceOf.powers(2).terms(5);
        var b = SequenceOf.squares().terms(5);
        assertEquals(a, b);
        assertNotEquals(a, b.terms(5, 10));
    }

    @Test
    public void test_Term() {
        assertEquals(
            Option.of(25L),
            SequenceOf.squares().term(5) // non-recursive
        );
        assertEquals(
            Option.of(1000L),
            SequenceOf.cubes().terms(8, 11).term(10) // non-recursive
        );
        assertEquals(
            Option.of(25L),
            SequenceOf.integers().term(26) // recursive
        );
        assertEquals(
            Option.of(30L),
            SequenceOf.integers().terms(26, 35).term(31) // recursive
        );
        assertEquals(
            Option.of(30L),
            SequenceOf.integers().term(31) // recursive
        );
        assertThrows(IllegalArgumentException.class, () -> {
            SequenceOf.integers().terms(15, 20).term(21); // out with range
            fail("Expected Exception.");
        });
        assertThrows(IllegalArgumentException.class, () -> {
            SequenceOf.integers().terms(15, 20).term(14); // out with range
            fail("Expected Exception.");
        });
        assertThrows(IllegalArgumentException.class, () -> {
            SequenceOf.integers().terms(10).term(11); // out with range
            fail("Expected Exception.");
        });
        assertThrows(IllegalArgumentException.class, () -> {
            SequenceOf.integers().terms(10).term(-1); // out with range
            fail("Expected Exception.");
        });
    }

}