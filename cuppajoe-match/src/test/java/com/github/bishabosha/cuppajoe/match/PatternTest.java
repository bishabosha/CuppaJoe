/*
 * Copyright (c) 2017. Jamie Thompson <bishbashboshjt@gmail.com>
 */

package com.github.bishabosha.cuppajoe.match;

import org.junit.jupiter.api.Test;

import java.util.stream.IntStream;

import static com.github.bishabosha.cuppajoe.API.None;
import static com.github.bishabosha.cuppajoe.API.Some;
import static com.github.bishabosha.cuppajoe.collections.immutable.API.Tuple;
import static com.github.bishabosha.cuppajoe.match.API.Cases;
import static com.github.bishabosha.cuppajoe.match.API.With;
import static com.github.bishabosha.cuppajoe.match.patterns.Collections.*;
import static com.github.bishabosha.cuppajoe.match.patterns.Standard.id;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class PatternTest {

    @Test
    public void test_typeSafety() {
        var cases = Cases(
            With(some(tuple(id(), id())), (x, y) ->
                "Some(Tuple(" + x + ", " + y + "))"),
            With(none(), () ->
                "None")/*,
            With(lazy(id()), () -> "will not compile")*/
        );

        assertEquals("Some(Tuple(1, 2))", cases.get(Some(Tuple(1, 2))));
        assertEquals("None", cases.get(None()));
//      assertEquals("will not compile", cases.get(Lazy(() -> 0)));
    }

    @Test
    public void test_dupStream_fails() {
        var stream = IntStream.range(0, 10);
        var streamChild1 = stream.mapToObj(String::valueOf);
        assertThrows(IllegalStateException.class, () -> stream.mapToObj(Long::valueOf), "stream has already been operated upon or closed");
        streamChild1.forEach(System.out::println); // unreachable
    }
}
