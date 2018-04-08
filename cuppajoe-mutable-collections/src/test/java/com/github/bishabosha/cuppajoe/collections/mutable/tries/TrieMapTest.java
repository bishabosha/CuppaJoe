/*
 * Copyright (c) 2017. Jamie Thompson <bishbashboshjt@gmail.com>
 */

package com.github.bishabosha.cuppajoe.collections.mutable.tries;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class TrieMapTest {

    @Test
    public void test_Put() {
        TrieMap<String> map = new TrieMap<>();
        assertFalse(map.containsKey("Hello"));
        map.put("Hello", "A greeting");
        assertTrue(map.containsKey("Hello"));
        assertEquals("A greeting", map.get("Hello"));
    }
}
