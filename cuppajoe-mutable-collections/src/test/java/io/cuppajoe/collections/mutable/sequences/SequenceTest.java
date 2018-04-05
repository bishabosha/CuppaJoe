/*
 * Copyright (c) 2017. Jamie Thompson <bishbashboshjt@gmail.com>
 */

package io.cuppajoe.collections.mutable.sequences;

import org.junit.Assert;
import org.junit.Test;

import java.util.List;
import java.util.function.Function;

import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.emptyIterable;

public class SequenceTest {

    @Test
    public void testTerms() {
        Sequence<Integer> naturals = new NonRecursiveSequence<>(Function.identity());
        Assert.assertThat(
                naturals.terms(5),
                contains(1, 2, 3, 4, 5)
        );
        Assert.assertThat(
                naturals.terms(6, 10),
                contains(6, 7, 8, 9, 10)
        );
        Assert.assertThat(
                new RecursiveSequence<>(List::size)
                        .terms(5),
                contains(0, 1, 2, 3, 4)
        );
        Assert.assertThat(
                new RecursiveSequence<>(List::size)
                        .terms(6, 10),
                contains(5, 6, 7, 8, 9)
        );
    }

    @Test
    public void testFilterTerms() {
        Sequence<Integer> naturals = new NonRecursiveSequence<>(Function.identity());
        Assert.assertThat(
                naturals.filter(x -> x > 3).terms(5),
                contains(4, 5)
        );
        Assert.assertThat(
                naturals.removeRearNode(),
                contains(1, 2, 3, 4, 5)
        );
        Assert.assertThat(
                naturals.filter(x -> x > 5).takeWhile(x -> x <= 5).terms(10),
                emptyIterable()
        );
        Assert.assertThat(
                naturals.removeFrontNode(),
                contains(1, 2, 3, 4, 5)
        );
    }
}
