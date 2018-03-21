/*
 * Copyright (c) 2017. Jamie Thompson <bishbashboshjt@gmail.com>
 */

package io.cuppajoe.tuples;

import io.cuppajoe.API;
import io.cuppajoe.collections.immutable.Queue;
import org.junit.Assert;
import org.junit.Test;

import static io.cuppajoe.API.Queue;
import static io.cuppajoe.API.Tuple;

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
        var tuple = Tuple(true, false);
        Assert.assertTrue(
            tuple.try$(1).isSuccess()
        );
        Assert.assertFalse(
            tuple.try$(4).isSuccess()
        );
    }
}
