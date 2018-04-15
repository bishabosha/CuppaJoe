package com.github.bishabosha.cuppajoe;


import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class IteratorsTest {

    @Test
    public void test_javaUtilListIteratorImpotence() {
        var javaList = List.of(1, 2, 3, 4, 5);
        var javaListIt = javaList.iterator();

        assertEquals(1, (int) javaListIt.next());
        assertEquals(2, (int) javaListIt.next());
        assertEquals(3, (int) javaListIt.next());
        assertEquals(4, (int) javaListIt.next());
        assertEquals(5, (int) javaListIt.next());

        javaListIt = javaList.iterator();

        assertTrue(javaListIt.hasNext());
        assertTrue(javaListIt.hasNext());
        assertTrue(javaListIt.hasNext());
        assertEquals(1, (int) javaListIt.next());
    }
}