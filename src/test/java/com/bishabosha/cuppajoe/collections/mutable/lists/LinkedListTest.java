/*
 * Copyright (c) 2017. Jamie Thompson <bishbashboshjt@gmail.com>
 */

package com.bishabosha.cuppajoe.collections.mutable.lists;

import org.junit.Assert;
import org.junit.Test;

import java.util.Comparator;
import java.util.List;

import static org.hamcrest.Matchers.contains;

public class LinkedListTest {

    public static LinkedList<Integer> getList() {
        LinkedList<Integer> list = new LinkedList<>();

        list.add(1);
        list.add(2);
        list.add(3);
        list.add(4);
        list.add(5);

        return list;
    }

    @Test
    public void testAddFirst() {
        LinkedList<String> list = new LinkedList<>();

        list.addFirst("kiwifruit");
        list.addFirst("pear");
        list.addFirst("apple");
        list.addFirst("orange");

        Assert.assertThat(
            list,
            contains("orange", "apple", "pear", "kiwifruit")
        );
    }

    @Test
    public void testAddLast() {
        Assert.assertThat(
            getList(),
            contains(1, 2, 3, 4, 5)
        );
    }

    @Test
    public void testSubList() {
        Assert.assertThat(
            getList().subList(2, 4),
            contains(3, 4)
        );
    }

    @Test
    public void testSize() {
        Assert.assertEquals(
            5,
            getList().size()
        );
    }

    @Test
    public void testToArray() {
        Assert.assertArrayEquals(
            new Integer[] {1, 2, 3, 4, 5},
            getList().toArray(new Integer[0])
        );
    }

    @Test
    public void testSort() {
        List<Integer> list = getList();
        list.sort(Comparator.reverseOrder());
        Assert.assertThat(
            list,
            contains(5,4,3,2,1)
        );
    }
}
