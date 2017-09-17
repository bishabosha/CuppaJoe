/*
 * Copyright (c) 2017. Jamie Thompson <bishbashboshjt@gmail.com>
 */

package com.bishabosha.cuppajoe.tuples;

import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;

import static com.bishabosha.cuppajoe.API.Tuple;

public class TupleTest {

    @Test
    public void testFlatten() {
        Assert.assertEquals(
            Arrays.asList("Hi", "there", "World"),
            Tuple(Tuple(Tuple(Tuple("Hi"), Tuple("there", Unit.UNIT), Unit.UNIT), "World")).flatten()
        );
    }

    @Test
    public void testLimits() {
        Assert.assertEquals(
            Unit.UNIT,
            Tuple()
        );
    }

    @Test
    public void lifted() {
        Tuple2<Integer, Boolean> tuple = Tuple(1, false);
        Assert.assertTrue(
            tuple.try$(1).isPresent()
        );
        Assert.assertFalse(
            tuple.try$(4).isPresent()
        );
    }
}
