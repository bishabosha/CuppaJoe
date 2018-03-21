package com.bishabosha.cuppajoe.collections.immutable;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.util.function.Function;

import static com.bishabosha.cuppajoe.API.Some;
import static com.bishabosha.cuppajoe.API.Tuple;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class ArrayTest {

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Test
    public void factoryMethodsEquivalent() {
        assertEquals(Array.empty(), Array.of());
        assertEquals(Array.of(1, 2).size(), Array.of(new Integer[]{1,2}).size());
    }

    @Test
    public void emptyArrayTakeZero_noError() {
        assertEquals(Array.empty(), Array.empty().take(0));
    }

    @Test
    public void emptyArrayTakeOne_IndexOutOfBounds() {
        expectedException.expect(IndexOutOfBoundsException.class);
        Array.empty().take(1);
    }

    @Test
    public void takeFiveFromArray10_noError() {
        final Array<Integer> list = Array.of(1,2,3,4,5,6,7,8,9,10);
        assertEquals(Array.of(1,2,3,4,5), list.take(5));
    }

    @Test
    public void takeRightFiveFromArray10_noError() {
        final Array<Integer> list = Array.of(1,2,3,4,5,6,7,8,9,10);
        assertEquals(Array.of(6,7,8,9,10), list.takeRight(5));
    }

    @Test
    public void subsequence4To10OnArray10_noError() {
        final Array<Integer> cons = Array.of(1,2,3,4,5,6,7,8,9,10);
        assertEquals(Array.of(5,6,7,8,9,10), cons.subsequence(4,10));
    }

    @Test
    public void subsequence4To5OnArray10_noError() {
        final Array<Integer> cons = Array.of(1,2,3,4,5,6,7,8,9,10);
        assertEquals(Array.of(5), cons.subsequence(4,5));
    }

    @Test
    public void subsequence4To4OnArray10_noError() {
        final Array<Integer> cons = Array.of(1,2,3,4,5,6,7,8,9,10);
        assertEquals(Array.empty(), cons.subsequence(4,4));
    }

    @Test
    public void singleArray_contains1() {
        assertTrue(Array.of(1).contains(1));
    }

    @Test
    public void longArray_contains10() {
        assertTrue(Array.of(1,2,3,4,5,6,7,8,9,10).contains(10));
    }

    @Test
    public void listOfNullsContainsNull() {
        assertTrue(Array.of(null).contains(null));
        assertTrue(Array.of(1, 2, null).contains(null));
    }

    @Test
    public void reverse() {
        assertEquals(Array.of(3,2,1), Array.of(1,2,3).reverse());
        assertEquals(Array.empty(), Array.empty().reverse());
        assertEquals(Array.of(1), Array.of(1).reverse());
    }

    @Test
    public void push() {
        assertEquals(Array.of(1), Array.empty().push(1));
        assertEquals(Array.of(1,2,3), Array.of(3).push(2).push(1));
        assertEquals(Array.of(1,2,3), Array.of(2,3).push(1));
    }

    @Test
    public void append() {
        assertEquals(Array.of(1), Array.empty().append(1));
        assertEquals(Array.of(1,2,3), Array.of(1).append(2).append(3));
        assertEquals(Array.of(1,2,3), Array.of(1,2).append(3));
    }

    @Test
    public void pop() {
        assertEquals(Some(Tuple(1, Array.empty())), Array.of(1).pop());
        assertEquals(Some(Tuple(1, Array.of(2))), Array.of(1,2).pop());
        assertEquals(Some(Tuple(1, Array.of(2, 3))), Array.of(1,2,3).pop());
    }

    @Test
    public void remove() {
        assertEquals(Array.empty(), Array.of(1).removeAll(1));
        assertEquals(Array.of(1), Array.of(1,2).removeAll(2));
        assertEquals(Array.of(1,1,1), Array.of(1,2,1,2,2,2,1).removeAll(2));
        assertEquals(Array.of(1), Array.of(null, null, null, 1).removeAll(null));
    }

    @Test
    public void distinct() {
        assertEquals(Array.of(1,2,3), Array.of(1,1,2,2,3,3).distinct(Function.identity()));
    }

    @Test
    public void flatMap() {
        assertEquals(Array.of(1, 1, 1, 2, 4, 8, 3, 9, 27), Array.of(1, 2, 3).flatMap(x -> Array.of(x, x * x, x * x * x)));
    }

    @Test
    public void map() {
        assertEquals(Array.of(1, 4, 9), Array.of(1, 2, 3).map(x -> x * x));
    }

    @Test
    public void print() {
        assertEquals("[1, 2, 3]", Array.of(1,2,3).toString());
        assertEquals("[1, 2, null]", Array.of(1,2,null).toString());
    }

    @Test
    public void get() {
        final Array<Integer> list = Array.of(1,2,3);
        assertEquals(1, list.get(0).intValue());
        assertEquals(2, list.get(1).intValue());
        assertEquals(3, list.get(2).intValue());
    }

    @Test
    public void size() {
        assertEquals(0, Array.empty().size());
        assertEquals(1, Array.of(1).size());
        assertEquals(2, Array.of(1,2).size());
        assertEquals(3, Array.of(1,2,3).size());
    }

    @Test
    public void or() {
        assertEquals(Array.of(1), Array.empty().or(() -> Array.of(1)));
    }
}