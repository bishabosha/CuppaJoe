package com.github.bishabosha.cuppajoe.match.typesafe;

import com.github.bishabosha.cuppajoe.match.MatchException;
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
import static org.junit.jupiter.api.Assertions.*;

@SuppressWarnings({"all"})
public class MatchTest {

    private static class Handles {
        private static final MethodHandle OK;
        private static final MethodHandle STRING_ID;
        private static final MethodHandle CONCAT;
        private static final MethodHandle CONCAT_8;

        static {
            OK = MethodHandles.constant(String.class, "OK");
            STRING_ID = MethodHandles.identity(String.class);
            try {
                CONCAT = MethodHandles.lookup().findStatic(Handles.class, "concat", MethodType.methodType(String.class, String.class, String.class));
                CONCAT_8 = MethodHandles.lookup().findStatic(Handles.class, "concat", MethodType.methodType(String.class, String.class, String.class, String.class, String.class, String.class, String.class, String.class, String.class));
            } catch (NoSuchMethodException | IllegalAccessException e) {
                throw new Error(e);
            }
        }

        private static String concat(String a, String b) {
            return a + b;
        }

        private static String concat(String a, String b, String c, String d, String e, String f, String g, String h) {
            return a + b + c + d + e + f + g + h;
        }
    }

    @Test
    public void test_Case0Strict_wildcard() throws Throwable {
        assertEquals("OK", With(__(), Handles.OK).get().invoke("OK"));
    }

    @Test
    public void test_Case1_identity() throws Throwable {
        assertEquals("id", With(id(), Handles.STRING_ID).get().invoke("id"));
    }

    @Test
    public void test_Case1_some_identity() throws Throwable {
        assertEquals("id_nested", With(some(id()), Handles.STRING_ID).get().invoke(Some("id_nested")));
    }

    @Test
    public void test_Case0Strict_some_wildcard() throws Throwable {
        assertEquals("OK", With(some(__()), Handles.OK).get().invoke(Some("anything")));
    }

    @Test
    public void test_Case0Strict_fails_some_wildcard_withNone() throws Throwable {
        assertThrows(MatchException.class, () -> With(some(__()), Handles.OK).get().invoke(None()));
    }

    @Test
    public void test_Case0Strict_some_wildcard_withNone() throws Throwable {
        assertEquals(None(), With(some(__()), Handles.OK).match().invoke(None()));
    }

    @Test
    public void test_Case0Strict_some_identity_withSome() throws Throwable {
        assertEquals(Some("id"), With(some(id()), Handles.STRING_ID).match().invoke(Some("id")));
    }

    @Test
    public void test_Case0Strict_none() throws Throwable {
        assertEquals("OK", With(none(), Handles.OK).get().invoke(None()));
    }

    @Test
    public void test_Case1_some_some_identity() throws Throwable {
        assertEquals("id_nested_nested", With(some(some(id())), Handles.STRING_ID).get().invoke(Some(Some("id_nested_nested"))));
    }

    @Test
    public void test_Case1_tuple2_identity_wildcard() throws Throwable {
        assertEquals("id", With(tuple(id(), __()), Handles.STRING_ID).get().invoke(Tuple("id", "whatever")));
    }

    /**
     * New desired behaviour is that if a value node is encountered with the {@code ALWAYS_TRUE} intrinsic predicate,
     * that it should check the current path, and if it does not fit the current parameter of the action MethodHandle,
     * replace {@code ALWAYS_TRUE} with an appropriate type test. (Requiring coupling the extractor with the case).
     * - Extra Note: should not defer path type checking to {@code extract()} - should be in {@code notInstantiated()}
     * - Another migration: defer generification of final MH as late as possible - i.e. for combined cases
     */
    @Test
    public void test_Case1_tuple2_stringIdentity_wildcard_classCastException() throws Throwable {
        var tuplecase = With(tuple(id(), __()), Handles.STRING_ID);
        assertFalse((boolean) tuplecase.matches().invoke(Tuple(0, "whatever")), "`id()` did not infer a type test");
        assertThrows(ClassCastException.class, () -> tuplecase.extract().invoke(Tuple(0, "whatever")));
    }

    @Test
    public void test_Case1_tuple2_some_identity_wildcard() throws Throwable {
        assertEquals("id", With(tuple(some(id()), __()), Handles.STRING_ID).get().invoke(Tuple(Some("id"), "whatever")));
    }

    @Test
    public void test_Case1_tuple2_some_wildcard_tuple2_wildcard_some_identity() throws Throwable {
        assertEquals("id", With(tuple(some(__()), tuple(__(), some(id()))), Handles.STRING_ID).get().invoke(Tuple(Some("whatever"), Tuple("Foo", Some("id")))));
    }

    @Test
    public void test_Case1_tuple2_some_wildcard_tuple2_wildcard_identity() throws Throwable {
        assertEquals("id", With(tuple(some(__()), tuple(__(), id())), Handles.STRING_ID).get().invoke(Tuple(Some("whatever"), Tuple("Foo", "id"))));
    }

    @Test
    public void test_Case1_some_tuple2_wildcard_identity() throws Throwable {
        assertEquals("id", With(some(tuple(__(), id())), Handles.STRING_ID).get().invoke(Some(Tuple("whatever", "id"))));
    }

    @Test
    public void test_Case1_tuple2_some_wildcard_some_identity() throws Throwable {
        assertEquals("id", With(tuple(some(__()), some(id())), Handles.STRING_ID).get().invoke(Tuple(Some("whatever"), Some("id"))));
    }

    @Test
    public void test_Case2_tuple2_identity_identity() throws Throwable {
        assertEquals("ab", With(tuple(id(), id()), Handles.CONCAT).get().invoke(Tuple("a", "b")));
    }

    @Test
    public void test_Case2_tuple2_some_identity_some_identity() throws Throwable {
        assertEquals("ab", With(tuple(some(id()), some(id())), Handles.CONCAT).get().invoke(Tuple(Some("a"), Some("b"))));
    }

    @Test
    public void test_Case8_tuple8_allId() throws Throwable {
        assertEquals("abcdefgh", With(tuple(id(), id(), id(), id(), id(), id(), id(), id()), Handles.CONCAT_8).get().invoke(Tuple("a", "b", "c", "d", "e", "f", "g", "h")));
    }
}
