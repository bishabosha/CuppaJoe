/*
 * Copyright (c) 2017. Jamie Thompson <bishbashboshjt@gmail.com>
 */

package com.bishabosha.caffeine.trees;

import com.bishabosha.caffeine.trees.TreeMap;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by Jamie on 26/02/2017.
 */
public class TreeMapTest {

    TreeMap<Integer, String> getMap() {
        TreeMap<Integer, String> map = new TreeMap<>();

        map.put(1, "Hello");
        map.put(2, "Everyone");
        map.put(2, "World");

        return map;
    }

    @Test
    void testKeySet() {
        Set<Integer> keySet = new HashSet<>();
        keySet.add(1);
        keySet.add(2);
        Assertions.assertEquals(keySet, getMap().keySet());
    }

    @Test
    void testValues() {
        Assertions.assertIterableEquals(Arrays.asList("Hello", "World"), getMap().values());
    }

    @Test
    void testGet() {
        Assertions.assertEquals("Hello", getMap().get(1));
    }

    @Test
    void testContainsKey() {
        Assertions.assertTrue(getMap().containsKey(2));
    }

    @Test
    void testContainsValue() {
        Assertions.assertTrue(getMap().containsValue("World"));
    }
}
