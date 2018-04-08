/*
 * Copyright (c) 2017. Jamie Thompson <bishbashboshjt@gmail.com>
 */

package com.github.bishabosha.cuppajoe.collections.mutable.trees;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class SearchTreeTest {

    private SearchTree<Integer> getTree() {
        var tree = new SearchTree<>(0);

        tree.add(2);
        tree.add(-4);
        tree.add(-2);
        tree.add(-3);
        tree.add(-1);
        tree.add(3);
        tree.add(-6);
        tree.add(1);
        tree.add(-5);
        tree.add(4);
        tree.add(5);

        return tree;
    }

    @Test
    public void test_Contains() {
        assertFalse(getTree().contains(7));
        assertTrue(getTree().contains(-1));
    }

    @Test
    public void test_Remove() {
        var tree = getTree();
        tree.remove(4);
        tree.remove(-1);
        tree.remove(2);
        Assertions.assertFalse(tree.contains(4));
    }

    @Test
    public void test_InOrder() {
        assertIterableEquals(
            List.of(-6, -5, -4, -3, -2, -1, 0, 1, 2, 3, 4, 5),
            getTree().inOrder()
        );
    }
}
