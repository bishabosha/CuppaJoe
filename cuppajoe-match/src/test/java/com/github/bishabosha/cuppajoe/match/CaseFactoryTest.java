package com.github.bishabosha.cuppajoe.match;

import com.github.bishabosha.cuppajoe.collections.immutable.tuples.Tuple;
import com.github.bishabosha.cuppajoe.collections.immutable.tuples.Tuple2;
import com.github.bishabosha.cuppajoe.control.Option;
import com.github.bishabosha.cuppajoe.match.patterns.Collections;
import com.github.bishabosha.cuppajoe.match.patterns.Standard;
import org.junit.jupiter.api.Test;

import static com.github.bishabosha.cuppajoe.match.internal.CaseFactory.with;
import static com.github.bishabosha.cuppajoe.match.patterns.Standard.$;
import static com.github.bishabosha.cuppajoe.match.patterns.Collections.Tuple2$;
import static com.github.bishabosha.cuppajoe.match.patterns.Standard.__;
import static org.junit.jupiter.api.Assertions.*;

public class CaseFactoryTest {

    @Test
    public void test_valueBinds_2() {
        assertEquals("1, 2", with(Collections.<Integer, Integer>Tuple2$($(), $()), (Integer x, Integer y) ->
            x + ", " + y
        )
        .get(Tuple.of(1, 2)));
    }

    @Test
    public void test_stringRep() {
        assertEquals("Some({<1>, <2>})", Collections.<Integer, Integer>Tuple2$($(), $()).test(Tuple.of(1, 2)).toString());
        assertEquals("Some({{<1>, <2>}, *})", Collections.<Tuple2<Integer, Integer>, Integer>Tuple2$(Tuple2$($(), $()), __()).test(Tuple.of(Tuple.of(1, 2), 3)).toString());
        assertEquals("Some({<1>, *})", Collections.<Integer, Integer>Tuple2$($(), __()).test(Tuple.of(1, 2)).toString());
        assertEquals("Some(*)", __().test(Option.empty()).toString());
        assertEquals("Some(<1>)", Standard.<Integer>Some$($()).test(Option.some(1)).toString());
        assertEquals("Some(*)", Standard.<Integer>Some$(__()).test(Option.some(1)).toString());
        assertEquals("None", Standard.<Integer>Some$(__()).test(Option.empty()).toString());
    }

    @Test
    public void test_valueBinds_1_left() {
        assertEquals("1, ", with(Collections.<Integer, Integer>Tuple2$($(), __()), (Integer x) ->
            x + ", "
        )
        .get(Tuple.of(1, 2)));
    }

    @Test
    public void test_valueBinds_1_right() {
        assertEquals(", 2", with(Collections.<Integer, Integer>Tuple2$(__(), $()), (Integer y) ->
                ", " + y
        )
        .get(Tuple.of(1, 2)));
    }

    @Test
    public void test_valueBinds_none() {
        assertEquals("None", with(Standard.<Integer>None$(), () ->
            "None"
        )
        .get(Option.empty()));
    }

    @Test
    public void test_valueBinds_some() {
        assertEquals("Some(1)", with(Standard.<Integer>Some$($()), (Integer val) ->
            "Some(" + val + ")"
        )
        .get(Option.some(1)));
    }

    @Test
    public void test_valueBinds_tuple8() {
        assertEquals(8,
             with(Collections.<Integer, Integer, Integer, Integer, Integer, Integer, Integer, Integer>Tuple8$($(), $(), $(), $(), $(), $(), $(), $()), (Integer a, Integer b, Integer c, Integer d, Integer e, Integer f, Integer g, Integer h) ->
                a + b + c + d + e + f + g + h
             )
             .get(Tuple.of(1, 1, 1, 1, 1, 1, 1, 1)).intValue()
        );
    }
}