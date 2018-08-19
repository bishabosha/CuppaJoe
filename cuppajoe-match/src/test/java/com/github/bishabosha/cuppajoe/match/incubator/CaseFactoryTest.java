package com.github.bishabosha.cuppajoe.match.incubator;

import com.github.bishabosha.cuppajoe.collections.immutable.tuples.Tuple;
import com.github.bishabosha.cuppajoe.control.Option;
import org.junit.jupiter.api.Test;

import static com.github.bishabosha.cuppajoe.match.incubator.CaseFactory.with;
import static com.github.bishabosha.cuppajoe.match.incubator.Patterns.$;
import static com.github.bishabosha.cuppajoe.match.incubator.Patterns.__;
import static org.junit.jupiter.api.Assertions.*;

public class CaseFactoryTest {

    @Test
    public void test_valueBinds_2() {
        assertEquals("1, 2", with(Patterns.<Integer, Integer>Tuple2$($(), $()), (Integer x, Integer y) ->
            x + ", " + y
        )
        .get(Tuple.of(1, 2)));
    }

    @Test
    public void test_stringRep() {
        assertEquals("Some({<1>, <2>})", Patterns.<Integer, Integer>Tuple2$($(), $()).test(Tuple.of(1, 2)).toString());
        assertEquals("Some({<1>, *})", Patterns.<Integer, Integer>Tuple2$($(), __()).test(Tuple.of(1, 2)).toString());
        assertEquals("Some(*)", __().test(Option.empty()).toString());
        assertEquals("Some(<1>)", Patterns.<Integer>Some$($()).test(Option.some(1)).toString());
        assertEquals("Some(*)", Patterns.<Integer>Some$(__()).test(Option.some(1)).toString());
        assertEquals("None", Patterns.<Integer>Some$(__()).test(Option.empty()).toString());
    }

    @Test
    public void test_valueBinds_1_left() {
        assertEquals("1, ", with(Patterns.<Integer, Integer>Tuple2$($(), __()), (Integer x) ->
            x + ", "
        )
        .get(Tuple.of(1, 2)));
    }

    @Test
    public void test_valueBinds_1_right() {
        assertEquals(", 2", with(Patterns.<Integer, Integer>Tuple2$(__(), $()), (Integer y) ->
                ", " + y
        )
        .get(Tuple.of(1, 2)));
    }

    @Test
    public void test_valueBinds_none() {
        assertEquals("None", with(Patterns.<Integer>None$(), () ->
            "None"
        )
        .get(Option.empty()));
    }

    @Test
    public void test_valueBinds_some() {
        assertEquals("Some(1)", with(Patterns.<Integer>Some$($()), (Integer val) ->
            "Some(" + val + ")"
        )
        .get(Option.some(1)));
    }
}