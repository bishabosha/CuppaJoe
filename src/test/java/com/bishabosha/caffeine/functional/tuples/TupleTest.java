/*
 * Copyright (c) 2017. Jamie Thompson <bishbashboshjt@gmail.com>
 */

package com.bishabosha.caffeine.functional.tuples;

import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;

import static com.bishabosha.caffeine.functional.tuples.Tuple0.EMPTY;
import static com.bishabosha.caffeine.functional.API.Tuple;

public class TupleTest {

    @Test
    public void testFlatten() {
        Assert.assertEquals(
            Arrays.asList("Hi", "there", "World"),
            Tuple(Tuple(Tuple(Tuple("Hi"), Tuple("there", EMPTY), EMPTY), "World")).flatten()
        );
    }

    @Test
    public void testLimits() {
        Assert.assertEquals(
            Tuple0.EMPTY,
            Tuple()
        );
    }
}
