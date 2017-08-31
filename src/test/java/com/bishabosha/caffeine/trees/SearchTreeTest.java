/*
 * Copyright (c) 2017. Jamie Thompson <bishbashboshjt@gmail.com>
 */

package com.bishabosha.caffeine.trees;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

public class SearchTreeTest {

    SearchTree<Integer> getTree() {
        SearchTree<Integer> tree = new SearchTree<>(0);

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
    void testContains() {
        Assertions.assertFalse(getTree().contains(7));
        Assertions.assertTrue(getTree().contains(-1));
    }

    @Test
    void testRemove() {
        SearchTree<Integer> tree = getTree();
        tree.remove(4);
        tree.remove(-1);
        tree.remove(2);
        Assertions.assertEquals(false, tree.contains(4));
    }

    @Test
    void testInOrder() {
        Assertions.assertIterableEquals(Arrays.asList(-6, -5, -4, -3, -2, -1, 0, 1, 2, 3, 4, 5), getTree().inOrder());
    }
}
