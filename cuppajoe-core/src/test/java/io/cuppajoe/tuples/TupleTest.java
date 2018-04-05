/*
 * Copyright (c) 2017. Jamie Thompson <bishbashboshjt@gmail.com>
 */

package io.cuppajoe.tuples;

import org.junit.Assert;
import org.junit.Test;

import static io.cuppajoe.API.Tuple;

public class TupleTest {

    @Test
    public void lifted() {
        var tuple = Tuple(true, false);
        Assert.assertFalse(
                tuple.tryGet(1).isEmpty()
        );
        Assert.assertTrue(
                tuple.tryGet(4).isEmpty()
        );
    }

    @Test
    public void contains() {
        var tuple = Tuple("a", 1, 'c', true);
        Assert.assertTrue(tuple.contains("a"));
        Assert.assertTrue(tuple.contains(1));
        Assert.assertTrue(tuple.contains('c'));
        Assert.assertTrue(tuple.contains(true));
        Assert.assertFalse(tuple.contains(false));
    }
}
