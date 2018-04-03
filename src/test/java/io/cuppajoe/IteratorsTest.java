package io.cuppajoe;

import org.junit.Test;

import java.util.Arrays;

import static org.junit.Assert.assertTrue;

public class IteratorsTest {

    @Test
    public void javaUtilListIteratorIdempotency() {
        var javaList = Arrays.asList(1,2,3,4,5);
        var javaListIt = javaList.iterator();

        assertTrue(javaListIt.next() == 1);
        assertTrue(javaListIt.next() == 2);
        assertTrue(javaListIt.next() == 3);
        assertTrue(javaListIt.next() == 4);
        assertTrue(javaListIt.next() == 5);

        javaListIt = javaList.iterator();

        javaListIt.hasNext();
        javaListIt.hasNext();
        javaListIt.hasNext();
        assertTrue(javaListIt.next() == 1);
    }
}