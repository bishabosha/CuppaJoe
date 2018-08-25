package com.github.bishabosha.cuppajoe.match;

import com.github.bishabosha.cuppajoe.collections.immutable.Array;
import com.github.bishabosha.cuppajoe.control.Option;
import org.junit.jupiter.api.Test;

import static com.github.bishabosha.cuppajoe.API.None;
import static com.github.bishabosha.cuppajoe.API.Some;
import static com.github.bishabosha.cuppajoe.collections.immutable.API.Tuple;
import static com.github.bishabosha.cuppajoe.match.API.With;
import static com.github.bishabosha.cuppajoe.match.patterns.Standard.*;
import static com.github.bishabosha.cuppajoe.match.patterns.Collections.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

@SuppressWarnings({"all"})
public class MatchTest {

    @Test
    public void test_Case0Strict_wildcard() {
        assertEquals("OK", With(__(), "OK").get("foo"));
    }

    @Test
    public void test_Case0Lazy_wildcard() {
        assertEquals("Lazy", With(__(), () -> "Lazy").get("bar"));
    }

    @Test
    public void test_Case1_identity() {
        assertEquals("id", With(id(), x -> x).get("id"));
    }

    @Test
    public void test_Case1_some_identity() {
        assertEquals("id_nested", With(some(id()), x -> x).get(Some("id_nested")));
    }

    @Test
    public void test_Case0Strict_some_wildcard() {
        assertEquals("Some(_)", With(some(__()), "Some(_)").get(Some("anything")));
    }

    @Test
    public void test_Case0Strict_some_wildcard_withNone() {
        assertEquals(None(), With(some(__()), "Some(_)").match(None()));
    }

    @Test
    public void test_Case0Strict_none() {
        assertEquals("None", With(none(), "None").get(None()));
    }

    @Test
    public void test_Case1_some_some_identity() {
        assertEquals("id_nested_nested", With(some(some(id())), x -> x).get(Some(Some("id_nested_nested"))));
    }

    @Test
    public void test_Case1_tuple2_identity_wildcard() {
        assertEquals("id", With(tuple(id(), __()), x -> x).get(Tuple("id", "whatever")));
    }

    @Test
    public void test_Case1_tuple2_some_identity_wildcard() {
        assertEquals("id", With(tuple(some(id()), __()), x -> x).get(Tuple(Some("id"), "whatever")));
    }

    @Test
    public void test_Case1_tuple2_some_wildcard_tuple2_wildcard_some_identity() {
        assertEquals("id", With(tuple(some(__()), tuple(__(), some(id()))), x -> x).get(Tuple(Some("whatever"), Tuple("Foo", Some("id")))));
    }

    @Test
    public void test_Case1_tuple2_some_wildcard_tuple2_wildcard_identity() {
        assertEquals("id", With(tuple(some(__()), tuple(__(), id())), x -> x).get(Tuple(Some("whatever"), Tuple("Foo", "id"))));
    }

    @Test
    public void test_Case1_some_tuple2_wildcard_identity() {
        assertEquals("id", With(some(tuple(__(), id())), x -> x).get(Some(Tuple("whatever", "id"))));
    }

    @Test
    public void test_Case1_tuple2_some_wildcard_some_identity() {
        assertEquals("id", With(tuple(some(__()), some(id())), x -> x).get(Tuple(Some("whatever"), Some("id"))));
    }

    @Test
    public void test_Case2_tuple2_identity_identity() {
        assertEquals("ab", With(tuple(id(), id()), (String a, String b) -> a + b).get(Tuple("a", "b")));
    }

    @Test
    public void test_Case2_tuple2_some_identity_some_identity() {
        assertEquals("ab", With(tuple(some(id()), some(id())), (String a, String b) -> a + b).get(Tuple(Some("a"), Some("b"))));
    }

    @Test
    public void test_Case8_tuple8_allId() {
        assertEquals("abcdefgh", With(tuple(id(), id(), id(), id(), id(), id(), id(), id()), (String a, String b, String c, String d, String e, String f, String g, String h) -> a + b + c + d + e + f + g + h).get(Tuple("a", "b", "c", "d", "e", "f", "g", "h")));
    }

    @Test
    public void test_arr2_case2() {
        assertEquals(1, With(arr(some(id()), none()), (Integer i) -> i).get(new Option[]{Some(1), None(), None(), Some("hello")}).intValue());
    }

    @Test
    public void test_array2_case2() {
        assertEquals(1, With(array(some(id()), none()), (Integer i) -> i).get(Array.of(Some(1), None(), None(), Some("hello"))).intValue());
        assertFalse(With(array(some(id()), none()), (Integer i) -> i).matches(Array.of(Some(1), Some("hello"))));
    }
}
