/*
 * Copyright (c) 2017. Jamie Thompson <bishbashboshjt@gmail.com>
 */

package com.bishabosha.cuppajoe.tuples;

import com.bishabosha.cuppajoe.API;
import com.bishabosha.cuppajoe.Library;
import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Test;

import static com.bishabosha.cuppajoe.API.Queue;
import static com.bishabosha.cuppajoe.API.Tuple;

public class TupleTest {

    @Test
    public void testFlatten() {
        Product toFlatten = Tuple(Tuple(Tuple(Tuple("One"), Tuple("Two", Tuple()), Tuple()), "Three"));
        Assert.assertEquals(
            Queue("One", "Two", "Three"),
            toFlatten.flatten(API::Queue, (xs, x) -> xs.enqueue(x))
        );
        Assert.assertThat(
            Library.inOrder(Product.class, toFlatten),
            Matchers.contains("One", "Two", "Three")
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
