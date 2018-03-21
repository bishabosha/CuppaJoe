/*
 * Copyright (c) 2017. Jamie Thompson <bishbashboshjt@gmail.com>
 */

package io.cuppajoe.collections.mutable.trees;

import org.junit.Assert;
import org.junit.Test;

import static org.hamcrest.Matchers.contains;

public class SearchTreeTest {

    SearchTree<Integer> getTree() {
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
    public void testContains() {
        Assert.assertFalse(getTree().contains(7));
        Assert.assertTrue(getTree().contains(-1));
    }

    @Test
    public void testRemove() {
        var tree = getTree();
        tree.remove(4);
        tree.remove(-1);
        tree.remove(2);
        Assert.assertEquals(false, tree.contains(4));
    }

    @Test
    public void testInOrder() {
        Assert.assertThat(getTree().inOrder(), contains(-6, -5, -4, -3, -2, -1, 0, 1, 2, 3, 4, 5));
    }
}
