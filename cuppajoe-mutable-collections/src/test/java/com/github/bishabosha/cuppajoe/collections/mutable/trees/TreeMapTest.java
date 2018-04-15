/*
 * Copyright (c) 2017. Jamie Thompson <bishbashboshjt@gmail.com>
 */

package com.github.bishabosha.cuppajoe.collections.mutable.trees;

import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;


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
    public void test_KeySet() {
        Set<Integer> keySet = new HashSet<>();
        keySet.add(1);
        keySet.add(2);
        assertEquals(keySet, getMap().keySet());
    }

    @Test
    public void test_Values() {
        assertIterableEquals(
            List.of("Hello", "World"),
            getMap().values()
        );
    }

    @Test
    public void test_Get() {
        assertEquals("Hello", getMap().get(1));
    }

    @Test
    public void test_ContainsKey() {
        assertTrue(getMap().containsKey(2));
    }

    @Test
    public void test_ContainsValue() {
        assertTrue(getMap().containsValue("World"));
    }
}
