/*
 * Copyright (c) 2017. Jamie Thompson <bishbashboshjt@gmail.com>
 */

package com.github.bishabosha.cuppajoe.collections.mutable.lists;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Comparator;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertIterableEquals;

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
    public void test_AddFirst() {
        LinkedList<String> list = new LinkedList<>();

        list.addFirst("kiwifruit");
        list.addFirst("pear");
        list.addFirst("apple");
        list.addFirst("orange");

        assertIterableEquals(
            List.of("orange", "apple", "pear", "kiwifruit"),
            list
        );
    }

    @Test
    public void test_AddLast() {
        assertIterableEquals(
            List.of(1, 2, 3, 4, 5),
            getList()
        );
    }

    @Test
    public void test_SubList() {
        assertIterableEquals(
            List.of(3, 4),
            getList().subList(2, 4)
        );
    }

    @Test
    public void test_Size() {
        Assertions.assertEquals(
            5,
            getList().size()
        );
    }

    @Test
    public void test_ToArray() {
        Assertions.assertArrayEquals(
            new Integer[]{1, 2, 3, 4, 5},
            getList().toArray(new Integer[0])
        );
    }

    @Test
    public void test_Sort() {
        List<Integer> list = getList();
        list.sort(Comparator.reverseOrder());
        assertIterableEquals(
            List.of(5, 4, 3, 2, 1),
            list
        );
    }
}
