/*
 * Copyright (c) 2017. Jamie Thompson <bishbashboshjt@gmail.com>
 */

package com.bishabosha.cuppajoe.tuples;

import com.bishabosha.cuppajoe.Foldable;
import com.bishabosha.cuppajoe.Library;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;

import static com.bishabosha.cuppajoe.API.Tuple;

public class TupleTest {

    @Test
    public void testFlatten() {
        Product toFlatten = Tuple(Tuple(Tuple(Tuple("Hi"), Tuple("there", Tuple()), Tuple()), "World"));
        Assert.assertEquals(
            Arrays.asList("Hi", "there", "World"),
            toFlatten.flatten()
        );
        Assert.assertEquals(
            Arrays.asList("Hi", "there", "World"),
            Foldable.foldOver(
                Library.inOrder(Product.class, toFlatten),
                new ArrayList<>(),
                (xs, x) -> {
                    xs.add(x);
                    return xs;
                }
            )
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
