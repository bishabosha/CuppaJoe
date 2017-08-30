/*
 * Copyright (c) 2017. Jamie Thompson <bishbashboshjt@gmail.com>
 */

package com.bishabosha.caffeine.tries;

import com.bishabosha.caffeine.base.Iterables;
import com.bishabosha.caffeine.tries.TrieSet;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

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
    void testContains() {
        TrieSet trie = getTrieSet();
        Assertions.assertTrue(trie.contains("row"));
        Assertions.assertFalse(trie.contains("zoo"));
    }

    @Test
    void testAdd() {
        TrieSet trie = getTrieSet();
        Assertions.assertFalse(trie.contains("mango"));
        trie.add("mango");
        Assertions.assertTrue(trie.contains("mango"));
    }

    @Test
    void testRemove() {
        TrieSet trie = getTrieSet();
        Assertions.assertTrue(trie.contains("release"));
        trie.remove("release");
        Assertions.assertFalse(trie.contains("release"));
    }

    @Test
    void testCompletions() {
        TrieSet trie = getTrieSet();
        Assertions.assertTrue(Iterables.equalElements(trie.getCompletions("red"), "red", "reddit"));
        Assertions.assertTrue(Iterables.equalElements(trie.getCompletions("ren"), "rent", "render"));
        Assertions.assertTrue(Iterables.equalElements(trie.getCompletions("ro"), "road", "row"));
        Assertions.assertTrue(Iterables.equalElements(trie.getCompletions("rea"), "real", "read"));
        Assertions.assertTrue(Iterables.equalElements(trie.getCompletions("he")));
    }

    @Test
    void testNumCompletions() {
        TrieSet trie = getTrieSet();
        Assertions.assertEquals(9, trie.numCompletions(""));
        Assertions.assertEquals(0, trie.numCompletions("hel"));
        Assertions.assertEquals(1, trie.numCompletions("read"));
        Assertions.assertEquals(0, trie.numCompletions("zoo"));
    }
}
