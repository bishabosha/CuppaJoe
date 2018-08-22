package com.github.bishabosha.cuppajoe.match;

import com.github.bishabosha.cuppajoe.collections.immutable.API;
import com.github.bishabosha.cuppajoe.collections.immutable.tuples.Tuple;
import com.github.bishabosha.cuppajoe.control.Option;
import org.junit.jupiter.api.Test;

import static com.github.bishabosha.cuppajoe.API.Some;
import static com.github.bishabosha.cuppajoe.match.internal.CaseFactory.with;
import static com.github.bishabosha.cuppajoe.match.patterns.Collections.*;
import static com.github.bishabosha.cuppajoe.match.patterns.Standard.__;
import static com.github.bishabosha.cuppajoe.match.patterns.Standard.id;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class CaseFactoryTest {

    @Test
    public void test_valueBinds_2() {
        assertEquals("1, 2", with(tuple(id(), id()), (Integer x, Integer y) ->
            x + ", " + y
        )
        .get(Tuple.of(1, 2)));
    }

    @Test
    public void test_stringRep() {
        assertEquals("Some({<1>, <2>})", tuple(id(), id()).test(Tuple.of(1, 2)).toString());
        assertEquals("Some({{<1>, <2>}, *})", tuple(tuple(id(), id()), __()).test(Tuple.of(Tuple.of(1, 2), 3)).toString());
        assertEquals("Some({<1>, *})", tuple(id(), __()).test(Tuple.of(1, 2)).toString());
        assertEquals("Some(*)", __().test(Option.empty()).toString());
        assertEquals("Some(<1>)", some(id()).test(Option.some(1)).toString());
        assertEquals("Some(*)", some(__()).test(Option.some(1)).toString());
        assertEquals("None", some(__()).test(Option.empty()).toString());
    }

    @Test
    public void test_valueBinds_1_left() {
        assertEquals("1, ", with(tuple(id(), __()), (Integer x) ->
            x + ", "
        )
        .get(Tuple.of(1, 2)));
    }

    @Test
    public void test_valueBinds_1_right() {
        assertEquals(", 2", with(tuple(__(), id()), (Integer y) ->
                ", " + y
        )
        .get(Tuple.of(1, 2)));
    }

    @Test
    public void test_valueBinds_none() {
        assertEquals("None", with(none(),
            "None"
        )
        .get(Option.empty()));
    }

    @Test
    public void test_valueBinds_some() {
        assertEquals("Some(1)", with(some(id()), (Integer val) ->
            "Some(" + val + ")"
        )
        .get(Option.some(1)));
    }

    @Test
    public void test_valueBinds_tuple8() {
        assertEquals(8,
             with(tuple(id(), id(), id(), id(), id(), id(), id(), id()), (Integer a, Integer b, Integer c, Integer d, Integer e, Integer f, Integer g, Integer h) ->
                a + b + c + d + e + f + g + h
             )
             .get(Tuple.of(1, 1, 1, 1, 1, 1, 1, 1)).intValue()
        );
    }

    @Test
    public void test_stress() {
        assertEquals("id", with(tuple(some(__()), tuple(__(), some(id()))), x -> x).get(API.Tuple(Some("whatever"), API.Tuple("Foo", Some("id")))));
    }
}