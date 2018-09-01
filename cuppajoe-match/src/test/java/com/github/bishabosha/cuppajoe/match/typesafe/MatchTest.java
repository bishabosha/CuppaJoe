package com.github.bishabosha.cuppajoe.match.typesafe;

import org.junit.jupiter.api.Test;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;

import static com.github.bishabosha.cuppajoe.API.None;
import static com.github.bishabosha.cuppajoe.API.Some;
import static com.github.bishabosha.cuppajoe.collections.immutable.API.Tuple;
import static com.github.bishabosha.cuppajoe.match.typesafe.API.With;
import static com.github.bishabosha.cuppajoe.match.typesafe.patterns.Collections.*;
import static com.github.bishabosha.cuppajoe.match.typesafe.patterns.Standard.__;
import static com.github.bishabosha.cuppajoe.match.typesafe.patterns.Standard.id;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SuppressWarnings({"all"})
public class MatchTest {

    private static class Handles {
        private static final MethodHandle OK;
        private static final MethodHandle ID;
        private static final MethodHandle CONCAT;

        static {
            OK = MethodHandles.constant(String.class, "OK");
            ID = MethodHandles.identity(String.class);
            try {
                CONCAT = MethodHandles.lookup().findStatic(Handles.class, "concat", MethodType.methodType(String.class, String.class, String.class));
            } catch (NoSuchMethodException | IllegalAccessException e) {
                throw new Error(e);
            }
        }

        private static String concat(String a, String b) {
            return a + b;
        }
    }

    @Test
    public void test_Case0Strict_wildcard() throws Throwable {
        assertEquals("OK", With(__(), Handles.OK).get().invoke("OK"));
    }

    @Test
    public void test_Case1_identity() throws Throwable {
        assertEquals("id", With(id(), Handles.ID).get().invoke("id"));
    }

    @Test
    public void test_Case1_some_identity() throws Throwable {
        assertEquals("id_nested", With(some(id()), Handles.ID).get().invoke(Some("id_nested")));
    }

    @Test
    public void test_Case0Strict_some_wildcard() throws Throwable {
        assertEquals("OK", With(some(__()), Handles.OK).get().invoke(Some("anything")));
    }

    @Test
    public void test_Case0Strict_some_wildcard_withNone() throws Throwable {
        assertEquals(None(), With(some(__()), Handles.OK).match().invoke(None()));
    }

    @Test
    public void test_Case0Strict_none() throws Throwable {
        assertEquals("OK", With(none(), Handles.OK).get().invoke(None()));
    }

    @Test
    public void test_Case1_some_some_identity() throws Throwable {
        assertEquals("id_nested_nested", With(some(some(id())), Handles.ID).get().invoke(Some(Some("id_nested_nested"))));
    }

    @Test
    public void test_Case1_tuple2_identity_wildcard() throws Throwable {
        assertEquals("id", With(tuple(id(), __()), Handles.ID).get().invoke(Tuple("id", "whatever")));
    }

    @Test
    public void test_Case1_tuple2_some_identity_wildcard() throws Throwable {
        assertEquals("id", With(tuple(some(id()), __()), Handles.ID).get().invoke(Tuple(Some("id"), "whatever")));
    }

    @Test
    public void test_Case1_tuple2_some_wildcard_tuple2_wildcard_some_identity() throws Throwable {
        assertEquals("id", With(tuple(some(__()), tuple(__(), some(id()))), Handles.ID).get().invoke(Tuple(Some("whatever"), Tuple("Foo", Some("id")))));
    }

    @Test
    public void test_Case1_tuple2_some_wildcard_tuple2_wildcard_identity() throws Throwable {
        assertEquals("id", With(tuple(some(__()), tuple(__(), id())), Handles.ID).get().invoke(Tuple(Some("whatever"), Tuple("Foo", "id"))));
    }

    @Test
    public void test_Case1_some_tuple2_wildcard_identity() throws Throwable {
        assertEquals("id", With(some(tuple(__(), id())), Handles.ID).get().invoke(Some(Tuple("whatever", "id"))));
    }

    @Test
    public void test_Case1_tuple2_some_wildcard_some_identity() throws Throwable {
        assertEquals("id", With(tuple(some(__()), some(id())), Handles.ID).get().invoke(Tuple(Some("whatever"), Some("id"))));
    }

    @Test
    public void test_Case2_tuple2_identity_identity() throws Throwable {
        assertEquals("ab", With(tuple(id(), id()), Handles.CONCAT).get().invoke(Tuple("a", "b")));
    }

    @Test
    public void test_Case2_tuple2_some_identity_some_identity() throws Throwable {
        assertEquals("ab", With(tuple(some(id()), some(id())), Handles.CONCAT).get().invoke(Tuple(Some("a"), Some("b"))));
    }
}
