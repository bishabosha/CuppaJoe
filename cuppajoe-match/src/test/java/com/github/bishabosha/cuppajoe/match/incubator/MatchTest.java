package com.github.bishabosha.cuppajoe.match.incubator;

import com.github.bishabosha.cuppajoe.collections.immutable.tuples.Tuple;
import org.junit.jupiter.api.Test;

import java.util.function.Function;

import static com.github.bishabosha.cuppajoe.API.None;
import static com.github.bishabosha.cuppajoe.API.Some;
import static com.github.bishabosha.cuppajoe.collections.immutable.API.Tuple;
import static com.github.bishabosha.cuppajoe.match.incubator.API.With;
import static com.github.bishabosha.cuppajoe.match.incubator.patterns.Standard.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

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
     *    && [some@2 canBranch [tuple@2 extract2 VAL]] // need to chain paths here, or cache vars stack frame
     *    // have an object stack that the predicates consume from, and func to say what to push
     * }</pre>
     * <p>pseudo code for the compiled extract function:</p>
     * <pre>{@code
     * extractPath1(T: Tuple2<Option<?>, Tuple2<?, Option<?>>>) -> ?
     *   [some@2 extract1 [tuple@2 extract2 [tuple@1 extract2 t]]]
     * }</pre>
     */
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
    public void test() {
        var identity = Function.identity();
        var identity2 = Function.identity();
        System.out.println(identity);
        System.out.println(identity2);
    }
}
