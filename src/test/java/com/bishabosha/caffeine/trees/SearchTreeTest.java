/*
 * Copyright (c) 2017. Jamie Thompson <bishbashboshjt@gmail.com>
 */

package com.bishabosha.caffeine.trees;

import org.hamcrest.CoreMatchers;
import org.junit.Assert;
import org.junit.Test;

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
    public void testContains() {
        Assert.assertFalse(getTree().contains(7));
        Assert.assertTrue(getTree().contains(-1));
    }

    @Test
    public void testRemove() {
        SearchTree<Integer> tree = getTree();
        tree.remove(4);
        tree.remove(-1);
        tree.remove(2);
        Assert.assertEquals(false, tree.contains(4));
    }

    @Test
    public void testInOrder() {
        Assert.assertThat(getTree().inOrder(), CoreMatchers.hasItems(-6, -5, -4, -3, -2, -1, 0, 1, 2, 3, 4, 5));
    }
}
