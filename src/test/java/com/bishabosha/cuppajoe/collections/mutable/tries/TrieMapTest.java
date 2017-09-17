/*
 * Copyright (c) 2017. Jamie Thompson <bishbashboshjt@gmail.com>
 */

package com.bishabosha.cuppajoe.collections.mutable.tries;

import org.junit.Assert;
import org.junit.Test;

public class TrieMapTest {

    @Test
    public void testPut() {
        TrieMap<String> map = new TrieMap<>();
        Assert.assertFalse(map.containsKey("Hello"));
        map.put("Hello", "A greeting");
        Assert.assertTrue(map.containsKey("Hello"));
        Assert.assertEquals("A greeting", map.get("Hello"));
    }
}
