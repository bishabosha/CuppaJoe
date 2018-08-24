package com.github.bishabosha.cuppajoe.match.incubator;

import org.junit.jupiter.api.Test;

import static com.github.bishabosha.cuppajoe.API.None;
import static com.github.bishabosha.cuppajoe.API.Some;
import static com.github.bishabosha.cuppajoe.collections.immutable.API.Tuple;
import static com.github.bishabosha.cuppajoe.match.incubator.API.With;
import static com.github.bishabosha.cuppajoe.match.incubator.patterns.Standard.*;
import static com.github.bishabosha.cuppajoe.match.incubator.patterns.Collections.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class MatchTest {

    @Test
    public void test_Case0Strict_wildcard() throws MatchException {
        assertEquals("OK", With(__(), "OK").get("foo"));
    }

    @Test
    public void test_Case0Lazy_wildcard() throws MatchException {
        assertEquals("Lazy", With(__(), () -> "Lazy").get("bar"));
    }

    @Test
    public void test_Case1_identity() throws MatchException {
        assertEquals("id", With(id(), x -> x).get("id"));
    }

    @Test
    public void test_Case1_some_identity() throws MatchException {
        assertEquals("id_nested", With(some(id()), x -> x).get(Some("id_nested")));
    }

    @Test
    public void test_Case0Strict_some_wildcard() throws MatchException {
        assertEquals("Some(_)", With(some(__()), "Some(_)").get(Some("anything")));
    }

    @Test
    public void test_Case0Strict_some_wildcard_withNone() {
        assertEquals(None(), With(some(__()), "Some(_)").match(None()));
    }

    @Test
    public void test_Case0Strict_none() throws MatchException {
        assertEquals("None", With(none(), "None").get(None()));
    }

    @Test
    public void test_Case1_some_some_identity() throws MatchException {
        assertEquals("id_nested_nested", With(some(some(id())), x -> x).get(Some(Some("id_nested_nested"))));
    }

    @Test
    public void test_Case1_tuple2_identity_wildcard() throws MatchException {
        assertEquals("id", With(tuple(id(), __()), x -> x).get(Tuple("id", "whatever")));
    }

    @Test
    public void test_Case1_tuple2_some_identity_wildcard() throws MatchException {
        assertEquals("id", With(tuple(some(id()), __()), x -> x).get(Tuple(Some("id"), "whatever")));
    }

    /**
     * <p>pseudo code for desired compiled matches function:</p>
     * <pre>{@code
     * matches(T: Tuple2<Option<?>, Tuple2<?, Option<?>>>) -> boolean
     *   [tuple@1 canBranch t]
     *   o = [tuple@1 extract1 t]
     *   [some@1 canBranch o]
     *   t2 = [tuple@1 extract2 t]
     *   [tuple@2 canBranch t2]
     *   o2 = [tuple@2 extract2 t2]
     *   [some@2 canBranch o2]
     * }</pre>
     * <p>current stack for compiled matches function:</p>
     * <pre>{@code
     * matches(T: Tuple2<Option<?>, Tuple2<?, Option<?>>>) -> boolean
     *   Object VAL = T
     *   [tuple@1 canBranch VAL]
     *    && [some@1 canBranch [tuple@1 extract1 VAL]]
     *    && [tuple@2 canBranch [tuple@1 extract2 VAL]]
     *    && [some@2 canBranch [tuple@2 extract2 [tuple@1 extract2 VAL]]]
     * }</pre>
     * <p>pseudo code for the compiled extract function:</p>
     * <pre>{@code
     * extractPath1(T: Tuple2<Option<?>, Tuple2<?, Option<?>>>) -> ?
     *   [some@2 extract1 [tuple@2 extract2 [tuple@1 extract2 t]]]
     * }</pre>
     */
    @Test
    public void test_Case1_tuple2_some_wildcard_tuple2_wildcard_some_identity() throws MatchException {
        assertEquals("id", With(tuple(some(__()), tuple(__(), some(id()))), x -> x).get(Tuple(Some("whatever"), Tuple("Foo", Some("id")))));
    }

    @Test
    public void test_Case1_tuple2_some_wildcard_tuple2_wildcard_identity() throws MatchException {
        assertEquals("id", With(tuple(some(__()), tuple(__(), id())), x -> x).get(Tuple(Some("whatever"), Tuple("Foo", "id"))));
    }

    @Test
    public void test_Case1_some_tuple2_wildcard_identity() throws MatchException {
        assertEquals("id", With(some(tuple(__(), id())), x -> x).get(Some(Tuple("whatever", "id"))));
    }

    @Test
    public void test_Case1_tuple2_some_wildcard_some_identity() throws MatchException {
        assertEquals("id", With(tuple(some(__()), some(id())), x -> x).get(Tuple(Some("whatever"), Some("id"))));
    }

    @Test
    public void test_Case2_tuple2_identity_identity() throws MatchException {
        assertEquals("ab", With(tuple(id(), id()), (String a, String b) -> a + b).get(Tuple("a", "b")));
    }

    @Test
    public void test_Case2_tuple2_some_identity_some_identity() throws MatchException {
        assertEquals("ab", With(tuple(some(id()), some(id())), (String a, String b) -> a + b).get(Tuple(Some("a"), Some("b"))));
    }

    @Test
    public void test_Case8_tuple8_allId() throws MatchException {
        assertEquals("abcdefgh", With(tuple(id(), id(), id(), id(), id(), id(), id(), id()), (String a, String b, String c, String d, String e, String f, String g, String h) -> a + b + c + d + e + f + g + h).get(Tuple("a", "b", "c", "d", "e", "f", "g", "h")));
    }
}
