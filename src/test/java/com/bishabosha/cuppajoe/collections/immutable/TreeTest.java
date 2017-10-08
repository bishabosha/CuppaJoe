package com.bishabosha.cuppajoe.collections.immutable;

import org.junit.Test;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

public class TreeTest {

    @Test
    public void print() {
        assertEquals("[1, 2, 3, 4, 5, 6]", Tree.of(4, 3, 6, 1, 2, 5).toString());
    }

    @Test
    public void size() {
        assertEquals(0, Tree.leaf().size());
        assertEquals(5, Tree.of(4, 3, 6, 1, 2).size());
    }

    @Test
    public void inOrder() {
        assertThat(
            Tree.of(4, 3, 6, 1, 2, 5).inOrder(),
            contains(1, 2, 3, 4, 5, 6)
        );
        assertThat(
            Tree.leaf().inOrder(),
            emptyIterable()
        );
        assertThat(
            Tree.of(1).inOrder(),
            contains(1)
        );
    }

    @Test
    public void preOrder() {
        assertThat(
            Tree.of(4, 2, 1, 3, 6, 5).preOrder(),
            contains(4, 2, 1, 3, 6, 5)
        );
    }

    @Test
    public void postOrder() {
        assertThat(
            Tree.of(4, 2, 1, 3, 6, 5).postOrder(),
            contains(1, 3, 2, 5, 6, 4)
        );
    }

    @Test
    public void levelOrder() {
        assertThat(
            Tree.of(4, 2, 1, 3, 6, 5).levelOrder(),
            contains(4, 2, 6, 1, 3, 5)
        );
//        assertThat(
//            Tree.of(4, 2, 1, 3, 6, 5).levelOrder().reverse(),
//            contains(5, 3, 1, 6, 2, 4)
//        );
    }
}