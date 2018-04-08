package com.github.bishabosha.cuppajoe.collections.immutable;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertIterableEquals;

public class TreeTest {

    @Test
    public void test_print() {
        assertEquals("[1, 2, 3, 4, 5, 6]", Tree.ofAll(4, 3, 6, 1, 2, 5).toString());
    }

    @Test
    public void test_size() {
        assertEquals(0, Tree.Leaf().size());
        assertEquals(5, Tree.ofAll(4, 3, 6, 1, 2).size());
    }

    @Test
    public void test_inOrder() {
        assertIterableEquals(
            List.of(1, 2, 3, 4, 5, 6),
            Tree.ofAll(4, 3, 6, 1, 2, 5).inOrder()
        );
        assertIterableEquals(
            List.of(6, 5, 4, 3, 2, 1),
            Tree.ofAll(4, 3, 6, 1, 2, 5).inOrder().foldRight(Queue.empty(), Queue::enqueue)
        );
        assertIterableEquals(
            List.of(),
            Tree.Leaf().inOrder()
        );
        assertIterableEquals(
            List.of(1),
            Tree.ofAll(1).inOrder()
        );
    }

    @Test
    public void test_preOrder() {
        assertIterableEquals(
            List.of(4, 2, 1, 3, 6, 5),
            Tree.ofAll(4, 2, 1, 3, 6, 5).preOrder()
        );
    }

    @Test
    public void test_postOrder() {
        assertIterableEquals(
            List.of(1, 3, 2, 5, 6, 4),
            Tree.ofAll(4, 2, 1, 3, 6, 5).postOrder()
        );
    }

    @Test
    public void test_levelOrder() {
        assertIterableEquals(
            List.of(4, 2, 6, 1, 3, 5),
            Tree.ofAll(4, 2, 1, 3, 6, 5).levelOrder()
        );
    }
}