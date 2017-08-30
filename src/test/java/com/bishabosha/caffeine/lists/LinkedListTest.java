/*
 * Copyright (c) 2017. Jamie Thompson <bishbashboshjt@gmail.com>
 */

package com.bishabosha.caffeine.lists;

import com.bishabosha.caffeine.base.Iterables;
import com.bishabosha.caffeine.lists.LinkedList;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

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
    void testAddFirst() {
        LinkedList<String> list = new LinkedList<>();

        list.addFirst("kiwifruit");
        list.addFirst("pear");
        list.addFirst("apple");
        list.addFirst("orange");

        Assertions.assertIterableEquals(
            Iterables.of("orange", "apple", "pear", "kiwifruit"),
            list
        );
    }

    @Test
    void testAddLast() {
        Assertions.assertIterableEquals(
            Iterables.of(1, 2, 3, 4, 5),
            getList()
        );
    }

    @Test
    void testSubList() {
        Assertions.assertIterableEquals(
            Iterables.of(3, 4),
            getList().subList(2, 4)
        );
    }

    @Test
    void testSize() {
        Assertions.assertEquals(
            5,
            getList().size()
        );
    }

    @Test
    void testToArray() {
        Assertions.assertArrayEquals(
            new Integer[] {1, 2, 3, 4, 5},
            getList().toArray(new Integer[0])
        );
    }

    @Test
    void testSort() {
        List<Integer> list = getList();
        list.sort(Comparator.reverseOrder());
        Assertions.assertIterableEquals(
            Iterables.of(5,4,3,2,1),
            list
        );
    }
}
