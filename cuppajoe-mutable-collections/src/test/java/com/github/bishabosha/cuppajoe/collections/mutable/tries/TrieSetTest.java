/*
 * Copyright (c) 2017. Jamie Thompson <bishbashboshjt@gmail.com>
 */

package com.github.bishabosha.cuppajoe.collections.mutable.tries;

import com.github.bishabosha.cuppajoe.util.Iterators;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

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
    public void test_Contains() {
        var trie = getTrieSet();
        assertTrue(trie.contains("row"));
        assertFalse(trie.contains("zoo"));
    }

    @Test
    public void test_Add() {
        var trie = getTrieSet();
        assertFalse(trie.contains("mango"));
        trie.add("mango");
        assertTrue(trie.contains("mango"));
    }

    @Test
    public void test_Remove() {
        var trie = getTrieSet();
        assertTrue(trie.contains("release"));
        trie.remove("release");
        assertFalse(trie.contains("release"));
    }

    @Test
    public void test_Completions() {
        var trie = getTrieSet();
        assertTrue(Iterators.equalElements(trie.getCompletions("red"), "red", "reddit"));
        assertTrue(Iterators.equalElements(trie.getCompletions("ren"), "rent", "render"));
        assertTrue(Iterators.equalElements(trie.getCompletions("ro"), "road", "row"));
        assertTrue(Iterators.equalElements(trie.getCompletions("rea"), "real", "read"));
        assertTrue(Iterators.equalElements(trie.getCompletions("he")));
    }

    @Test
    public void test_NumCompletions() {
        var trie = getTrieSet();
        assertEquals(9, trie.numCompletions(""));
        assertEquals(0, trie.numCompletions("hel"));
        assertEquals(1, trie.numCompletions("read"));
        assertEquals(0, trie.numCompletions("zoo"));
    }
}
