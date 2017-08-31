/*
 * Copyright (c) 2017. Jamie Thompson <bishbashboshjt@gmail.com>
 */

package com.bishabosha.caffeine.tries;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class TrieMapTest {

    @Test
    void testPut() {
        TrieMap<String> map = new TrieMap<>();
        Assertions.assertFalse(map.containsKey("Hello"));
        map.put("Hello", "A greeting");
        Assertions.assertTrue(map.containsKey("Hello"));
        Assertions.assertEquals("A greeting", map.get("Hello"));
    }
}
