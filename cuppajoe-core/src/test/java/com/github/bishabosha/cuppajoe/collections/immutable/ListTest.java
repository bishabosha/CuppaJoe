package com.github.bishabosha.cuppajoe.collections.immutable;

import org.junit.jupiter.api.Test;

import static com.github.bishabosha.cuppajoe.API.Tuple;
import static com.github.bishabosha.cuppajoe.collections.immutable.API.List;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ListTest {

    @Test
    public void test_factoryMethodsEquivalent() {
        assertEquals(List(), List.empty());
        assertEquals(List(1), List.singleton(1));
        assertEquals(List(1, 2), List.ofAll(1, 2));
        assertEquals(List(1, 2, 3), List.ofAll(1, 2, 3));
        assertEquals(List(1, 2, 3, 4), List.ofAll(1, 2, 3, 4));
        assertEquals(List(1, 2, 3, 4, 5), List.ofAll(1, 2, 3, 4, 5));
        assertEquals(List(1, 2, 3, 4, 5, 6), List.ofAll(1, 2, 3, 4, 5, 6));
        assertEquals(List(1, 2, 3, 4, 5, 6, 7), List.ofAll(1, 2, 3, 4, 5, 6, 7));
        assertEquals(List(1, 2, 3, 4, 5, 6, 7, 8), List.ofAll(1, 2, 3, 4, 5, 6, 7, 8));
    }

    @Test
    public void test_concat_with_IntHeadAndNullTail_throws_NPE() {
        assertThrows(NullPointerException.class, () ->
            List.concat(1, null)
        );
    }

    @Test
    public void test_emptyListTakeZero_noError() {
        assertEquals(List.empty(), List.empty().take(0));
    }

    @Test
    public void test_emptyListTakeOne_IndexOutOfBounds() {
        assertThrows(IndexOutOfBoundsException.class, () ->
            List.empty().take(1)
        );
    }

    @Test
    public void test_takeFiveFromList10_noError() {
        final var list = List(1, 2, 3, 4, 5, 6, 7, 8);
        assertEquals(List.ofAll(1, 2, 3, 4, 5), list.take(5));
    }

    @Test
    public void test_takeRightFiveFromList10_noError() {
        final var list = List(1, 2, 3, 4, 5, 6, 7, 8);
        assertEquals(List.ofAll(4, 5, 6, 7, 8), list.takeRight(5));
    }

    @Test
    public void test_subsequence4To10OnList10_noError() {
        final var cons = List(1, 2, 3, 4, 5, 6, 7, 8);
        assertEquals(List.ofAll(5, 6, 7, 8), cons.subsequence(4, 8));
    }

    @Test
    public void test_subsequence4To5OnList10_noError() {
        final var cons = List(1, 2, 3, 4, 5, 6, 7, 8);
        assertEquals(List.singleton(5), cons.subsequence(4, 5));
    }

    @Test
    public void test_subsequence4To4OnList10_noError() {
        final var cons = List(1, 2, 3, 4, 5, 6, 7, 8);
        assertEquals(List.empty(), cons.subsequence(4, 4));
    }

    @Test
    public void test_singleList_contains1() {
        assertTrue(List(1).contains(1));
    }

    @Test
    public void test_longList_contains10() {
        assertTrue(List(1, 2, 3, 4, 5, 6, 7, 8).contains(5));
    }

    @Test
    public void test_listOfNullsContainsNull() {
        assertTrue(List(null).contains(null));
        assertTrue(List(1, 2, null).contains(null));
    }

    @Test
    public void test_reverse() {
        assertEquals(List(3, 2, 1), List(1, 2, 3).reverse());
        assertEquals(List(), List().reverse());
        assertEquals(List(1), List(1).reverse());
    }

    @Test
    public void test_append() {
        assertEquals(List(1, 2, 3), List().append(1).append(2).append(3));
        assertEquals(List(1, 2, 3), List(1).append(2).append(3));
        assertEquals(List(1, 2, 3), List(1, 2).append(3));
    }

    @Test
    public void test_remove() {
        assertEquals(List(), List(1).removeAll(1));
        assertEquals(List(1), List(1, 2).removeAll(2));
        assertEquals(List(1, 1, 1), List(1, 2, 1, 2, 2, 2, 1).removeAll(2));
        assertEquals(List(1), List(null, null, null, 1).removeAll(null));
    }

    @Test
    public void test_print() {
        assertEquals("[1, 2, 3]", List(1, 2, 3).toString());
        assertEquals("[1, 2, null]", List(1, 2, null).toString());
        assertEquals("[null]", List(null).toString());
        assertEquals("[1]", List(1).toString());
    }

    @Test
    public void test_unapply() {
        final var list = List.concat(1, List.concat(2, List.empty()));
        assertEquals(
            Tuple(1, List.singleton(2)),
            list.unapply()
        );
    }

    @Test
    public void test_get() {
        final var list = List(1, 2, 3);
        assertEquals(1, list.get(0).intValue());
        assertEquals(2, list.get(1).intValue());
        assertEquals(3, list.get(2).intValue());
    }

    @Test
    public void test_size() {
        assertEquals(0, List().size());
        assertEquals(1, List(1).size());
        assertEquals(2, List(1, 2).size());
        assertEquals(3, List(1, 2, 3).size());
    }

    @Test
    public void test_map() {
        assertEquals(List(2, 3, 4), List(1, 2, 3).map(x -> x + 1));
    }
}