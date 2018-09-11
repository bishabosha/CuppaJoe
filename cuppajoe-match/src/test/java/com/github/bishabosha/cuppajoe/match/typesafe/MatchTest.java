package com.github.bishabosha.cuppajoe.match.typesafe;

import com.github.bishabosha.cuppajoe.collections.immutable.tuples.Tuple;
import com.github.bishabosha.cuppajoe.match.MatchException;
import com.github.bishabosha.cuppajoe.match.typesafe.patterns.Pattern;
import com.github.bishabosha.cuppajoe.match.typesafe.patterns.Pattern.Branch;
import org.junit.jupiter.api.Test;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.util.List;

import static com.github.bishabosha.cuppajoe.API.None;
import static com.github.bishabosha.cuppajoe.API.Some;
import static com.github.bishabosha.cuppajoe.collections.immutable.API.Tuple;
import static com.github.bishabosha.cuppajoe.match.typesafe.API.With;
import static com.github.bishabosha.cuppajoe.match.typesafe.MatchTest.IntPair.int_pair;
import static com.github.bishabosha.cuppajoe.match.typesafe.MatchTest.IntegerPair.integer_pair;
import static com.github.bishabosha.cuppajoe.match.typesafe.MatchTest.StringPair.string_pair;
import static com.github.bishabosha.cuppajoe.match.typesafe.patterns.Collections.*;
import static com.github.bishabosha.cuppajoe.match.typesafe.patterns.Standard.__;
import static com.github.bishabosha.cuppajoe.match.typesafe.patterns.Standard.id;
import static java.lang.invoke.MethodType.methodType;
import static org.junit.jupiter.api.Assertions.*;

@SuppressWarnings({"all"})
public class MatchTest {

    private static class Handles {
        private static final MethodHandle OK;
        private static final MethodHandle STRING_ID;
        private static final MethodHandle INT_ID;
        private static final MethodHandle CONCAT;
        private static final MethodHandle INT_SUM;
        private static final MethodHandle CONCAT_8;
        private static final MethodHandle LIST_SIZE;

        static {
            OK = MethodHandles.constant(String.class, "OK");
            STRING_ID = MethodHandles.identity(String.class);
            INT_ID = MethodHandles.identity(int.class);
            var lookup = MethodHandles.lookup();
            try {
                INT_SUM = lookup.findStatic(Handles.class, "sum", methodType(int.class, int.class, int.class));
                CONCAT = lookup.findStatic(Handles.class, "concat", methodType(String.class, String.class, String.class));
                CONCAT_8 = lookup.findStatic(Handles.class, "concat", methodType(String.class, String.class, String.class, String.class, String.class, String.class, String.class, String.class, String.class));
                LIST_SIZE = lookup.findStatic(Handles.class, "listSize", methodType(int.class, List.class));
            } catch (NoSuchMethodException | IllegalAccessException e) {
                throw new Error(e);
            }
        }

        private static int listSize(List list) {
            return list.size();
        }

        private static int sum(int a, int b) {
            return a + b;
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

    @Test
    public void test_Tuple2StringAny_with_id_wildcard_infers() throws Throwable {
        var tuplecase = With(tuple(id(), __()), Handles.STRING_ID);
        assertFalse((boolean) tuplecase.matches().invoke(Tuple(0, "whatever")), "`id()` did not infer an [Object as? String] test that rejects Integer");
        assertTrue((boolean) tuplecase.matches().invoke(Tuple("id", "whatever")), "`id()` did not infer an [Object as? String] test that accepts String");
        assertThrows(ClassCastException.class, () -> tuplecase.extract().invoke(Tuple(0, "whatever")));
    }

    @Test
    public void test_Func$int$int$int_Value$Tuple2$Integer$Integer_Pattern$id$id_infers_intBoxingTests() throws Throwable {
        var tupleCase = With(tuple(id(), id()), Handles.INT_SUM);
        assertFalse((boolean) tupleCase.matches().invoke(Tuple("invalid", "invalid")), "`id()` did not infer an [Object as? int] test that rejects String");
        assertTrue((boolean) tupleCase.matches().invoke(Tuple(1, 2)), "`id()` did not infer an [Object as? int] test that matches Integer");
        assertEquals(3, (int)tupleCase.extract().invoke(Tuple(1, 2)));
    }

    @Test
    public void test_Func$String$String_Value$StringPair_Pattern$string_pair$id$wildcard_infers_classEq_StringPair() throws Throwable {
        assertSame(
            Pattern.classEq(StringPair.class),
            With(string_pair(id(), __()), Handles.STRING_ID).matches(),
            "`id()` inferred an extra test on StringPair branches"
        );
    }

    @Test
    public void test_Func$int$int$int_Value$IntPair_Pattern$int_pair$id$id_infers_classEq_IntPair() throws Throwable {
        assertSame(
            Pattern.classEq(IntPair.class),
            With(int_pair(id(), id()), Handles.INT_SUM).matches(),
            "`id()` inferred an extra tests on IntPair branches"
        );
    }

    @Test
    public void test_Func$int$int$int_Value$IntegerPair_Pattern$integer_pair$id$id_infers_classEq_IntegerPair() throws Throwable {
        assertSame(
            Pattern.classEq(IntegerPair.class),
            With(integer_pair(id(), id()), Handles.INT_SUM).matches(),
            "`id()` inferred an extra tests on IntegerPair branches"
        );
    }

    /**
     * Next step: defer generification of final MH as late as possible - i.e. for combined cases
     * Proposal: patterns have an domain class and a specific class.
     * If do not match, use instanceof domain, else eq specific.
     * Check specific is subclass of domain.
     * {@code id()} infers specific from pType, domain from current path.
     * If {@code id()} is the root pattern, both specific and domain come from first pType
     * {@code matches} MH input type becomes domain of pattern
     * When cases are combined, the common domain of all branches must be inferred and type tests of the specific added
     */
    @Test
    public void test_Func$int$int_Pattern$id_infers_classEq_Integer() throws Throwable {
        var integerEq = Pattern.classEq(Integer.class);
        assertSame(integerEq, With(id(), Handles.INT_ID).matches());
        assertEquals(methodType(boolean.class, Object.class), integerEq.type());
    }

    @Test
    public void test_Func$int$List_Pattern$id_infers_classAssignable_List() throws Throwable {
        var listAssignable = Pattern.classAssignable(List.class);
        assertSame(listAssignable, With(id(), Handles.LIST_SIZE).matches());
        assertEquals(methodType(boolean.class, Object.class), listAssignable.type());
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

    public static final class StringPair {

        private static final MethodHandle A;
        private static final MethodHandle B;

        static {
            var lookup = MethodHandles.lookup();
            try {
                A = lookup.findGetter(StringPair.class, "a", String.class);
                B = lookup.findGetter(StringPair.class, "b", String.class);
            } catch (NoSuchFieldException | IllegalAccessException e) {
                throw new Error(e);
            }
        }

        public static Branch string_pair(Pattern a, Pattern b) {
            return Pattern.branchN(StringPair.class, Tuple.of(a, A), Tuple.of(b, B));
        }

        public static StringPair StringPair(String a, String b) {
            return new StringPair(a, b);
        }

        private final String a;
        private final String b;

        private StringPair(String a, String b) {
            this.a = a;
            this.b = b;
        }
    }

    public static final class IntegerPair {

        private static final MethodHandle A;
        private static final MethodHandle B;

        static {
            var lookup = MethodHandles.lookup();
            try {
                A = lookup.findGetter(IntegerPair.class, "a", Integer.class);
                B = lookup.findGetter(IntegerPair.class, "b", Integer.class);
            } catch (NoSuchFieldException | IllegalAccessException e) {
                throw new Error(e);
            }
        }

        public static Branch integer_pair(Pattern a, Pattern b) {
            return Pattern.branchN(IntegerPair.class, Tuple.of(a, A), Tuple.of(b, B));
        }

        public static IntegerPair IntegerPair(Integer a, Integer b) {
            return new IntegerPair(a, b);
        }

        private final Integer a;
        private final Integer b;

        private IntegerPair(Integer a, Integer b) {
            this.a = a;
            this.b = b;
        }
    }

    public static final class IntPair {

        private static final MethodHandle A;
        private static final MethodHandle B;

        static {
            var lookup = MethodHandles.lookup();
            try {
                A = lookup.findGetter(IntPair.class, "a", int.class);
                B = lookup.findGetter(IntPair.class, "b", int.class);
            } catch (NoSuchFieldException | IllegalAccessException e) {
                throw new Error(e);
            }
        }

        public static Branch int_pair(Pattern a, Pattern b) {
            return Pattern.branchN(IntPair.class, Tuple.of(a, A), Tuple.of(b, B));
        }

        public static IntPair IntPair(int a, int b) {
            return new IntPair(a, b);
        }

        private final int a;
        private final int b;

        private IntPair(int a, int b) {
            this.a = a;
            this.b = b;
        }
    }
}
