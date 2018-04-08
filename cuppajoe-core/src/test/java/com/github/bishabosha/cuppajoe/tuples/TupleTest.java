/*
 * Copyright (c) 2017. Jamie Thompson <bishbashboshjt@gmail.com>
 */

package com.github.bishabosha.cuppajoe.tuples;

import com.github.bishabosha.cuppajoe.API;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class TupleTest {

    @Test
    public void test_lifted() {
        var tuple = API.Tuple(true, false);
        assertFalse(
            tuple.tryGet(1).isEmpty()
        );
        assertTrue(
            tuple.tryGet(4).isEmpty()
        );
    }

    @Test
    public void test_contains() {
        var tuple = API.Tuple("a", 1, 'c', true);
        assertTrue(tuple.contains("a"));
        assertTrue(tuple.contains(1));
        assertTrue(tuple.contains('c'));
        assertTrue(tuple.contains(true));
        assertFalse(tuple.contains(false));
    }
}
