/*
 * Copyright (c) 2017. Jamie Thompson <bishbashboshjt@gmail.com>
 */

package com.bishabosha.cuppajoe.tuples;

import com.bishabosha.cuppajoe.API;
import com.bishabosha.cuppajoe.collections.immutable.Queue;
import org.junit.Assert;
import org.junit.Test;

import static com.bishabosha.cuppajoe.API.Queue;
import static com.bishabosha.cuppajoe.API.Tuple;

public class TupleTest {

    @Test
    public void testFlatten() {
        final Product toFlatten = Tuple(Tuple(Tuple(Tuple("One"), Tuple("Two", Tuple()), Tuple()), "Three"));
        Assert.assertEquals(
            Queue("One", "Two", "Three"),
            toFlatten.flatten(API::Queue, Queue::enqueue)
        );
    }

    @Test
    public void lifted() {
        Product2<Boolean, Boolean> tuple = Tuple(true, false);
        Assert.assertTrue(
            tuple.try$(1).isPresent()
        );
        Assert.assertFalse(
            tuple.try$(4).isPresent()
        );
    }
}
