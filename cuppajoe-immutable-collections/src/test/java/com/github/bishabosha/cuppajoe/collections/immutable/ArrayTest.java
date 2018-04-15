package com.github.bishabosha.cuppajoe.collections.immutable;

import org.junit.jupiter.api.Test;

import java.util.function.Function;

import static com.github.bishabosha.cuppajoe.API.Some;
import static com.github.bishabosha.cuppajoe.collections.immutable.API.Tuple;
import static org.junit.jupiter.api.Assertions.*;

public class ArrayTest {

    @Test
    public void test_factoryMethodsEquivalent() {
        assertEquals(Array.empty(), Array.of());
        assertEquals(Array.of(1, 2).size(), Array.of(new Integer[]{1, 2}).size());
    }

    @Test
    public void test_emptyArrayTakeZero_noError() {
        assertEquals(Array.empty(), Array.empty().take(0));
    }

    @Test
    public void test_emptyArrayTakeOne_IndexOutOfBounds() {
        assertThrows(IndexOutOfBoundsException.class, () ->
            Array.empty().take(1)
        );
    }

    @Test
    public void test_takeFiveFromArray10_noError() {
        final var list = Array.of(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);
        assertEquals(Array.of(1, 2, 3, 4, 5), list.take(5));
    }

    @Test
    public void test_takeRightFiveFromArray10_noError() {
        final var list = Array.of(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);
        assertEquals(Array.of(6, 7, 8, 9, 10), list.takeRight(5));
    }

    @Test
    public void test_subsequence4To10OnArray10_noError() {
        final var cons = Array.of(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);
        assertEquals(Array.of(5, 6, 7, 8, 9, 10), cons.subsequence(4, 10));
    }

    @Test
    public void test_subsequence4To5OnArray10_noError() {
        final var cons = Array.of(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);
        assertEquals(Array.of(5), cons.subsequence(4, 5));
    }

    @Test
    public void test_subsequence4To4OnArray10_noError() {
        final var cons = Array.of(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);
        assertEquals(Array.empty(), cons.subsequence(4, 4));
    }

    @Test
    public void test_singleArray_contains1() {
        assertTrue(Array.of(1).contains(1));
    }

    @Test
    public void test_longArray_contains10() {
        assertTrue(Array.of(1, 2, 3, 4, 5, 6, 7, 8, 9, 10).contains(10));
    }

    @Test
    public void test_listOfNullsContainsNull() {
        assertTrue(Array.of(null).contains(null));
        assertTrue(Array.of(1, 2, null).contains(null));
    }

    @Test
    public void test_reverse() {
        assertEquals(Array.of(3, 2, 1), Array.of(1, 2, 3).reverse());
        assertEquals(Array.empty(), Array.empty().reverse());
        assertEquals(Array.of(1), Array.of(1).reverse());
    }

    @Test
    public void test_push() {
        assertEquals(Array.of(1), Array.empty().push(1));
        assertEquals(Array.of(1, 2, 3), Array.of(3).push(2).push(1));
        assertEquals(Array.of(1, 2, 3), Array.of(2, 3).push(1));
    }

    @Test
    public void test_append() {
        assertEquals(Array.of(1), Array.empty().append(1));
        assertEquals(Array.of(1, 2, 3), Array.of(1).append(2).append(3));
        assertEquals(Array.of(1, 2, 3), Array.of(1, 2).append(3));
    }

    @Test
    public void test_pop() {
        assertEquals(Some(Tuple(1, Array.empty())), Array.of(1).pop());
        assertEquals(Some(Tuple(1, Array.of(2))), Array.of(1, 2).pop());
        assertEquals(Some(Tuple(1, Array.of(2, 3))), Array.of(1, 2, 3).pop());
    }

    @Test
    public void test_remove() {
        assertEquals(Array.empty(), Array.of(1).removeAll(1));
        assertEquals(Array.of(1), Array.of(1, 2).removeAll(2));
        assertEquals(Array.of(1, 1, 1), Array.of(1, 2, 1, 2, 2, 2, 1).removeAll(2));
        assertEquals(Array.of(1), Array.of(null, null, null, 1).removeAll(null));
    }

    @Test
    public void test_distinct() {
        assertEquals(Array.of(1, 2, 3), Array.of(1, 1, 2, 2, 3, 3).distinct(Function.identity()));
    }

    @Test
    public void test_flatMap() {
        assertEquals(Array.of(1, 1, 1, 2, 4, 8, 3, 9, 27), Array.of(1, 2, 3).flatMap(x -> Array.of(x, x * x, x * x * x)));
    }

    @Test
    public void test_map() {
        assertEquals(Array.of(1, 4, 9), Array.of(1, 2, 3).map(x -> x * x));
    }

    @Test
    public void test_print() {
        assertEquals("[1, 2, 3]", Array.of(1, 2, 3).toString());
        assertEquals("[1, 2, null]", Array.of(1, 2, null).toString());
    }

    @Test
    public void test_get() {
        final var list = Array.of(1, 2, 3);
        assertEquals(1, list.get(0).intValue());
        assertEquals(2, list.get(1).intValue());
        assertEquals(3, list.get(2).intValue());
    }

    @Test
    public void test_size() {
        assertEquals(0, Array.empty().size());
        assertEquals(1, Array.of(1).size());
        assertEquals(2, Array.of(1, 2).size());
        assertEquals(3, Array.of(1, 2, 3).size());
    }

    @Test
    public void test_or() {
        assertEquals(Array.of(1), Array.empty().or(() -> Array.of(1)));
    }
}