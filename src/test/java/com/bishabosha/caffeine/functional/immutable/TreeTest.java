package com.bishabosha.caffeine.functional.immutable;

import org.junit.Test;

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
}