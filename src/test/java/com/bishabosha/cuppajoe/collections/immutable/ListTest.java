package com.bishabosha.cuppajoe.collections.immutable;

import org.hamcrest.CoreMatchers;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static com.bishabosha.cuppajoe.API.Some;
import static com.bishabosha.cuppajoe.API.Tuple;
import static org.junit.Assert.*;

public class ListTest {

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Test
    public void factoryMethodsEquivalent() {
        assertEquals(List.concat(1, List.empty()), List.of(1));
        assertEquals(List.concat(1, List.concat(2, List.concat(3, List.empty()))), List.of(1, 2, 3));
    }

    @Test
    public void concat_with_IntHeadAndNullTail_throws_NPE() {
        expectedException.expect(NullPointerException.class);
        List.concat(1, null);
    }

    @Test
    public void emptyListTakeZero_noError() {
        assertEquals(List.empty(), List.empty().take(0));
    }

    @Test
    public void emptyListTakeOne_IndexOutOfBounds() {
        expectedException.expect(IndexOutOfBoundsException.class);
        List.empty().take(1);
    }

    @Test
    public void takeFiveFromList10_noError() {
        final List<Integer> list = List.of(1,2,3,4,5,6,7,8,9,10);
        assertEquals(List.of(1,2,3,4,5), list.take(5));
    }

    @Test
    public void takeRightFiveFromList10_noError() {
        final List<Integer> list = List.of(1,2,3,4,5,6,7,8,9,10);
        assertEquals(List.of(6,7,8,9,10), list.takeRight(5));
    }

    @Test
    public void subsequence4To10OnList10_noError() {
        final List<Integer> cons = List.of(1,2,3,4,5,6,7,8,9,10);
        assertEquals(List.of(5,6,7,8,9,10), cons.subsequence(4,10));
    }

    @Test
    public void subsequence4To5OnList10_noError() {
        final List<Integer> cons = List.of(1,2,3,4,5,6,7,8,9,10);
        assertEquals(List.of(5), cons.subsequence(4,5));
    }

    @Test
    public void subsequence4To4OnList10_noError() {
        final List<Integer> cons = List.of(1,2,3,4,5,6,7,8,9,10);
        assertEquals(List.empty(), cons.subsequence(4,4));
    }

    @Test
    public void singleList_contains1() {
        assertTrue(List.of(1).contains(1));
    }

    @Test
    public void longList_contains10() {
        assertTrue(List.of(1,2,3,4,5,6,7,8,9,10).contains(10));
    }

    @Test
    public void listOfNullsContainsNull() {
        assertTrue(List.of(null).contains(null));
        assertTrue(List.of(1, 2, null).contains(null));
    }

    @Test
    public void reverse() {
        assertEquals(List.of(3,2,1), List.of(1,2,3).reverse());
        assertEquals(List.empty(), List.empty().reverse());
        assertEquals(List.of(1), List.of(1).reverse());
    }

    @Test
    public void append() {
        assertEquals(List.of(1), List.empty().append(1));
        assertEquals(List.of(1,2,3), List.of(1).append(2).append(3));
        assertEquals(List.of(1,2,3), List.concat(1, List.of(2)).append(3));
        assertEquals(List.of(1,2,3), List.of(1,2).append(3));
    }

    @Test
    public void remove() {
        assertEquals(List.empty(), List.of(1).remove(1));
        assertEquals(List.of(1), List.of(1,2).remove(2));
        assertEquals(List.of(1,1,1), List.of(1,2,1,2,2,2,1).remove(2));
        assertEquals(List.of(1), List.of(null, null, null, 1).remove(null));
    }

    @Test
    public void print() {
        assertEquals("[1, 2, 3]", List.of(1,2,3).toString());
        assertEquals("[1, 2, null]", List.of(1,2,null).toString());
        assertEquals("[null]", List.concat(null, List.empty()).toString());
        assertEquals("[1]", List.concat(1, List.empty()).toString());
    }

    @Test
    public void unapply() {
        final List<Integer> list = List.of(1,2);
        assertThat(list, CoreMatchers.instanceOf(List.Cons.class));
        assertEquals(
            Some(Tuple(1, List.of(2))),
            list.unapply()
        );
    }

    @Test
    public void apply() {
        assertEquals(
            List.of(1,2),
            List.<Integer>Applied().apply(List.of(1,2).unapply().get())
        );
    }
}