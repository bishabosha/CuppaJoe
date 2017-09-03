package com.bishabosha.caffeine.functional.immutable;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static org.junit.Assert.*;

public class ConsTest {

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Test
    public void factoryMethodsEquivalent() {
        assertEquals(Cons.concat(1, Cons.empty()), Cons.of(1));
        assertEquals(Cons.concat(1, Cons.concat(2, Cons.concat(3, Cons.empty()))), Cons.of(1, 2, 3));
    }

    @Test
    public void concat_with_IntHeadAndNullTail_throws_NPE() {
        expectedException.expect(NullPointerException.class);
        Cons.concat(1, null);
    }

    @Test
    public void emptyListTakeZero_noError() {
        assertEquals(Cons.empty(), Cons.empty().take(0));
    }

    @Test
    public void emptyListTakeOne_IndexOutOfBounds() {
        expectedException.expect(IndexOutOfBoundsException.class);
        Cons.empty().take(1);
    }

    @Test
    public void takeFiveFromCons10_noError() {
        Cons<Integer> cons = Cons.of(1,2,3,4,5,6,7,8,9,10);
        assertEquals(Cons.of(1,2,3,4,5), cons.take(5));
    }

    @Test
    public void subsequence4To10OnCons10_noError() {
        Cons<Integer> cons = Cons.of(1,2,3,4,5,6,7,8,9,10);
        assertEquals(Cons.of(5,6,7,8,9,10), cons.subsequence(4,10));
    }

    @Test
    public void subsequence4To5OnCons10_noError() {
        Cons<Integer> cons = Cons.of(1,2,3,4,5,6,7,8,9,10);
        assertEquals(Cons.of(5), cons.subsequence(4,5));
    }

    @Test
    public void subsequence4To4OnCons10_noError() {
        Cons<Integer> cons = Cons.of(1,2,3,4,5,6,7,8,9,10);
        assertEquals(Cons.empty(), cons.subsequence(4,4));
    }

    @Test
    public void singleCons_contains1() {
        assertTrue(Cons.of(1).contains(1));
    }

    @Test
    public void longCons_contains10() {
        assertTrue(Cons.of(1,2,3,4,5,6,7,8,9,10).contains(10));
    }

    @Test
    public void consOfNullsContainsNull() {
        assertTrue(Cons.of(null).contains(null));
        assertTrue(Cons.of(1, 2, null).contains(null));
    }

    @Test
    public void reverseWorks() {
        assertEquals(Cons.of(3,2,1), Cons.of(1,2,3).reverse());
        assertEquals(Cons.empty(), Cons.empty().reverse());
        assertEquals(Cons.of(1), Cons.of(1).reverse());
    }

    @Test
    public void print() {
        assertEquals("[1, 2, 3]", Cons.of(1,2,3).toString());
        assertEquals("[1, 2, null]", Cons.of(1,2,null).toString());
        assertEquals("[null]", Cons.concat(null, Cons.empty()).toString());
        assertEquals("[1]", Cons.concat(1, Cons.empty()).toString());
    }
}