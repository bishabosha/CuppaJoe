/*
 * Copyright (c) 2017. Jamie Thompson <bishbashboshjt@gmail.com>
 */

package io.cuppajoe.tuples;

import io.cuppajoe.collections.immutable.Queue;
import org.junit.Assert;
import org.junit.Test;

import static io.cuppajoe.API.Queue;
import static io.cuppajoe.API.Tuple;

public class TupleTest {

    @Test
    public void testFlatten() {
        final Tuple toFlatten = Tuple(Tuple(Tuple(Tuple("One"), Tuple("Two", Tuple()), Tuple()), "Three"));
        Assert.assertEquals(
            Queue("One", "Two", "Three"),
            toFlatten.flatten(Queue(), Queue::enqueue)
        );
    }

    @Test
    public void lifted() {
        var tuple = Tuple(true, false);
        Assert.assertFalse(
            tuple.lift$(1).isEmpty()
        );
        Assert.assertTrue(
            tuple.lift$(4).isEmpty()
        );
    }
}
