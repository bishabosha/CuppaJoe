/*
 * Copyright (c) 2017. Jamie Thompson <bishbashboshjt@gmail.com>
 */

package com.bishabosha.caffeine.functional.tuples;

import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;

import static com.bishabosha.caffeine.functional.API.Nothing;
import static com.bishabosha.caffeine.functional.API.Some;
import static com.bishabosha.caffeine.functional.tuples.Unit.UNIT;
import static com.bishabosha.caffeine.functional.API.Tuple;

public class TupleTest {

    @Test
    public void testFlatten() {
        Assert.assertEquals(
            Arrays.asList("Hi", "there", "World"),
            Tuple(Tuple(Tuple(Tuple("Hi"), Tuple("there", UNIT), UNIT), "World")).flatten()
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
        Assert.assertEquals(
            Some(1),
            tuple._$(1)
        );
        Assert.assertEquals(
            Nothing(),
            tuple._$(4)
        );
    }
}
