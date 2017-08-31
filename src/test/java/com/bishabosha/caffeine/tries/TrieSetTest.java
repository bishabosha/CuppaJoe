/*
 * Copyright (c) 2017. Jamie Thompson <bishbashboshjt@gmail.com>
 */

package com.bishabosha.caffeine.tries;

import com.bishabosha.caffeine.base.Iterables;
import org.junit.Assert;
import org.junit.Test;

public class TrieSetTest {

    TrieSet getTrieSet() {
        TrieSet trie = new TrieSet();

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
        TrieSet trie = getTrieSet();
        Assert.assertTrue(trie.contains("row"));
        Assert.assertFalse(trie.contains("zoo"));
    }

    @Test
    public void testAdd() {
        TrieSet trie = getTrieSet();
        Assert.assertFalse(trie.contains("mango"));
        trie.add("mango");
        Assert.assertTrue(trie.contains("mango"));
    }

    @Test
    public void testRemove() {
        TrieSet trie = getTrieSet();
        Assert.assertTrue(trie.contains("release"));
        trie.remove("release");
        Assert.assertFalse(trie.contains("release"));
    }

    @Test
    public void testCompletions() {
        TrieSet trie = getTrieSet();
        Assert.assertTrue(Iterables.equalElements(trie.getCompletions("red"), "red", "reddit"));
        Assert.assertTrue(Iterables.equalElements(trie.getCompletions("ren"), "rent", "render"));
        Assert.assertTrue(Iterables.equalElements(trie.getCompletions("ro"), "road", "row"));
        Assert.assertTrue(Iterables.equalElements(trie.getCompletions("rea"), "real", "read"));
        Assert.assertTrue(Iterables.equalElements(trie.getCompletions("he")));
    }

    @Test
    public void testNumCompletions() {
        TrieSet trie = getTrieSet();
        Assert.assertEquals(9, trie.numCompletions(""));
        Assert.assertEquals(0, trie.numCompletions("hel"));
        Assert.assertEquals(1, trie.numCompletions("read"));
        Assert.assertEquals(0, trie.numCompletions("zoo"));
    }
}
