/*
 * Copyright (c) 2017. Jamie Thompson <bishbashboshjt@gmail.com>
 */

package com.bishabosha.caffeine.functional.tuples;

import com.bishabosha.caffeine.functional.tuples.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static com.bishabosha.caffeine.functional.tuples.Tuple.EMPTY;
import static com.bishabosha.caffeine.functional.tuples.Tuples.*;

public class TupleTest {

    @Test
    void testFlatten() {
        Assertions.assertAll(
            () -> System.out.println(Tuple(Tuple(Tuple(Tuple("Hi"), Tuple("there", EMPTY), EMPTY), "World")).flatten())
        );
        Assertions.assertIterableEquals(
            Tuple("Hi", "there", "World"),
            Tuple(Tuple(Tuple(Tuple("Hi"), Tuple("there", EMPTY), EMPTY), "World")).flatten()
        );
    }

    @Test
    void testLimits() {
        Assertions.assertEquals(
            Tuple.EMPTY,
            Tuple()
        );
    }
}
