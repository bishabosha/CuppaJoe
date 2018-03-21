package com.bishabosha.cuppajoe.collections.immutable;

import com.bishabosha.cuppajoe.collections.immutable.List.Cons;
import org.hamcrest.CoreMatchers;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static com.bishabosha.cuppajoe.API.List;
import static com.bishabosha.cuppajoe.API.Tuple;
import static org.junit.Assert.*;

public class ListTest {

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Test
    public void factoryMethodsEquivalent() {
        assertEquals(List(), List.empty());
        assertEquals(List(1), List.of(1));
        assertEquals(List(1, 2), List.of(1, 2));
        assertEquals(List(1, 2, 3), List.of(1, 2, 3));
        assertEquals(List(1, 2, 3, 4), List.of(1, 2, 3, 4));
        assertEquals(List(1, 2, 3, 4, 5), List.of(1, 2, 3, 4, 5));
        assertEquals(List(1, 2, 3, 4, 5, 6), List.of(1, 2, 3, 4, 5, 6));
        assertEquals(List(1, 2, 3, 4, 5, 6, 7), List.of(1, 2, 3, 4, 5, 6, 7));
        assertEquals(List(1, 2, 3, 4, 5, 6, 7, 8), List.of(1, 2, 3, 4, 5, 6, 7, 8));
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
        final List<Integer> list = List(1,2,3,4,5,6,7,8);
        assertEquals(List.of(1,2,3,4,5), list.take(5));
    }

    @Test
    public void takeRightFiveFromList10_noError() {
        final List<Integer> list = List(1,2,3,4,5,6,7,8);
        assertEquals(List.of(4, 5, 6, 7, 8), list.takeRight(5));
    }

    @Test
    public void subsequence4To10OnList10_noError() {
        final List<Integer> cons = List(1,2,3,4,5,6,7,8);
        assertEquals(List.of(5,6,7,8), cons.subsequence(4,8));
    }

    @Test
    public void subsequence4To5OnList10_noError() {
        final List<Integer> cons = List(1,2,3,4,5,6,7,8);
        assertEquals(List.of(5), cons.subsequence(4,5));
    }

    @Test
    public void subsequence4To4OnList10_noError() {
        final List<Integer> cons = List(1,2,3,4,5,6,7,8);
        assertEquals(List.empty(), cons.subsequence(4,4));
    }

    @Test
    public void singleList_contains1() {
        assertTrue(List(1).contains(1));
    }

    @Test
    public void longList_contains10() {
        assertTrue(List(1,2,3,4,5,6,7,8).contains(5));
    }

    @Test
    public void listOfNullsContainsNull() {
        assertTrue(List(null).contains(null));
        assertTrue(List(1, 2, null).contains(null));
    }

    @Test
    public void reverse() {
        assertEquals(List(3,2,1), List(1,2,3).reverse());
        assertEquals(List(), List().reverse());
        assertEquals(List(1), List(1).reverse());
    }

    @Test
    public void append() {
        assertEquals(List(1,2,3), List().append(1).append(2).append(3));
        assertEquals(List(1,2,3), List(1).append(2).append(3));
        assertEquals(List(1,2,3), List(1,2).append(3));
    }

    @Test
    public void remove() {
        assertEquals(List(), List(1).removeAll(1));
        assertEquals(List(1), List(1,2).removeAll(2));
        assertEquals(List(1,1,1), List(1,2,1,2,2,2,1).removeAll(2));
        assertEquals(List(1), List(null, null, null, 1).removeAll(null));
    }

    @Test
    public void print() {
        assertEquals("[1, 2, 3]", List(1,2,3).toString());
        assertEquals("[1, 2, null]", List(1,2,null).toString());
        assertEquals("[null]", List(null).toString());
        assertEquals("[1]", List(1).toString());
    }

    @Test
    public void unapply() {
        final Cons<Integer> list = List.concat(1, List.concat(2, List.empty()));
        assertThat(list, CoreMatchers.instanceOf(Cons.class));
        assertEquals(
            Tuple(1, List.of(2)),
            list.unapply()
        );
    }

    @Test
    public void apply() {
        assertEquals(
            List.of(1,2),
            Cons.<Integer>Applied().apply(List.concat(1, List.concat(2, List.empty())).unapply())
        );
    }

    @Test
    public void get() {
        final List<Integer> list = List(1,2,3);
        assertEquals(1, list.get(0).intValue());
        assertEquals(2, list.get(1).intValue());
        assertEquals(3, list.get(2).intValue());
    }

    @Test
    public void size() {
        assertEquals(0, List().size());
        assertEquals(1, List(1).size());
        assertEquals(2, List(1,2).size());
        assertEquals(3, List(1,2,3).size());
    }

    @Test
    public void map() {
        assertEquals(List(2,3,4), List(1,2,3).map(x -> x + 1));
    }
}