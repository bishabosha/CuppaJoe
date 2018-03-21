/*
 * Copyright (c) 2017. Jamie Thompson <bishbashboshjt@gmail.com>
 */

package io.cuppajoe.collections.mutable.tries;

import io.cuppajoe.Iterables;
import org.junit.Assert;
import org.junit.Test;

public class TrieSetTest {

    TrieSet getTrieSet() {
        var trie = new TrieSet();

        trie.add("row");
        trie.add("road");
        trie.add("release");
        trie.add("real");
        trie.add("read");
        trie.add("red");
        trie.add("reddit");
        trie.add("render");
        trie.add("rent");

        return trie;
    }

    @Test
    public void testContains() {
        var trie = getTrieSet();
        Assert.assertTrue(trie.contains("row"));
        Assert.assertFalse(trie.contains("zoo"));
    }

    @Test
    public void testAdd() {
        var trie = getTrieSet();
        Assert.assertFalse(trie.contains("mango"));
        trie.add("mango");
        Assert.assertTrue(trie.contains("mango"));
    }

    @Test
    public void testRemove() {
        var trie = getTrieSet();
        Assert.assertTrue(trie.contains("release"));
        trie.remove("release");
        Assert.assertFalse(trie.contains("release"));
    }

    @Test
    public void testCompletions() {
        var trie = getTrieSet();
        Assert.assertTrue(Iterables.equalElements(trie.getCompletions("red"), "red", "reddit"));
        Assert.assertTrue(Iterables.equalElements(trie.getCompletions("ren"), "rent", "render"));
        Assert.assertTrue(Iterables.equalElements(trie.getCompletions("ro"), "road", "row"));
        Assert.assertTrue(Iterables.equalElements(trie.getCompletions("rea"), "real", "read"));
        Assert.assertTrue(Iterables.equalElements(trie.getCompletions("he")));
    }

    @Test
    public void testNumCompletions() {
        var trie = getTrieSet();
        Assert.assertEquals(9, trie.numCompletions(""));
        Assert.assertEquals(0, trie.numCompletions("hel"));
        Assert.assertEquals(1, trie.numCompletions("read"));
        Assert.assertEquals(0, trie.numCompletions("zoo"));
    }
}
