/*
 * Copyright (c) 2017. Jamie Thompson <bishbashboshjt@gmail.com>
 */

package com.bishabosha.caffeine.trees;

import org.junit.Assert;
import org.junit.Test;

import java.util.HashSet;
import java.util.Set;

import static org.hamcrest.Matchers.contains;

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
    public void testKeySet() {
        Set<Integer> keySet = new HashSet<>();
        keySet.add(1);
        keySet.add(2);
        Assert.assertEquals(keySet, getMap().keySet());
    }

    @Test
    public void testValues() {
        Assert.assertThat(getMap().values(), contains("Hello", "World"));
    }

    @Test
    public void testGet() {
        Assert.assertEquals("Hello", getMap().get(1));
    }

    @Test
    public void testContainsKey() {
        Assert.assertTrue(getMap().containsKey(2));
    }

    @Test
    public void testContainsValue() {
        Assert.assertTrue(getMap().containsValue("World"));
    }
}
