package com.github.bishabosha.cuppajoe.match.typesafe;

import com.github.bishabosha.cuppajoe.match.ExtractionFailedException;
import org.junit.jupiter.api.Test;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.util.List;

import static com.github.bishabosha.cuppajoe.match.typesafe.API.With;
import static com.github.bishabosha.cuppajoe.match.typesafe.patterns.Collections.some;
import static com.github.bishabosha.cuppajoe.match.typesafe.patterns.Collections.tuple;
import static com.github.bishabosha.cuppajoe.match.typesafe.patterns.Standard.__;
import static com.github.bishabosha.cuppajoe.match.typesafe.patterns.Standard.id;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class CaseCompileTest {

    private static class Handles {
        private static final MethodHandle FAIL_0;
        private static final MethodHandle FAIL_1;
        private static final MethodHandle FAIL_2;
        private static final MethodHandle FAIL_3;
        private static final MethodHandle OK_0;
        private static final MethodHandle OK_1;
        private static final MethodHandle OK_3;

        static {
            FAIL_0 = MethodHandles.constant(String.class, "FAIL");
            FAIL_1 = MethodHandles.dropArgumentsToMatch(FAIL_0, 0, List.of(Object.class), 0);
            FAIL_2 = MethodHandles.dropArgumentsToMatch(FAIL_0, 0, List.of(Object.class, Object.class), 0);
            FAIL_3 = MethodHandles.dropArgumentsToMatch(FAIL_0, 0, List.of(Object.class, Object.class, Object.class), 0);

            OK_0 = MethodHandles.constant(String.class, "OK");
            OK_1 = MethodHandles.dropArgumentsToMatch(OK_0, 0, List.of(Object.class), 0);
            OK_3 = MethodHandles.dropArgumentsToMatch(OK_0, 0, List.of(Object.class, Object.class, Object.class), 0);
        }
    }

    /**
     * This fails as it is impossible to bind 1 variable from the {@code __()} wildcard
     */
    @Test
    public void test_Case1_failsOn_Empty() {
        assertThrows(ExtractionFailedException.class, () -> With(__(), Handles.FAIL_1));
    }

    /**
     * This succeeds as the {@code id()} pattern binds a value
     */
    @Test
    public void test_Case1_succeedsOn_Value() {
        With(id(), Handles.OK_1);
    }

    /**
     * This fails as {@code id()} expects to bind a value and there is no consumer to accept it
     */
    @Test
    public void test_Case0Strict_failsOn_Value() {
        assertThrows(ExtractionFailedException.class, () -> With(id(), Handles.FAIL_0));
    }

    /**
     * This fails as {@code id()} expects to bind a value and there is no consumer to accept it
     */
    @Test
    public void test_Case0Lazy_failsOn_Value() {
        assertThrows(ExtractionFailedException.class, () -> With(id(), Handles.FAIL_0));
    }

    @Test
    public void test_Case0Lazy_failsOn_Branch1Value() {
        assertThrows(ExtractionFailedException.class, () -> With(some(id()), Handles.FAIL_0));
    }

    @Test
    public void test_Case0Strict_failsOn_Branch1Value() {
        assertThrows(ExtractionFailedException.class, () -> With(some(id()), Handles.FAIL_0));
    }

    @Test
    public void test_Case1_succeedsOn_Branch1Branch1Value() {
        With(some(some(id())), Handles.OK_1);
    }

    @Test
    public void test_Case1_failsOn_Branch1Branch1Empty() {
        assertThrows(ExtractionFailedException.class, () -> With(some(some(__())), Handles.FAIL_1));
    }

    @Test
    public void test_Case0Lazy_succeedsOn_Branch1Empty() {
        With(some(__()), Handles.OK_0);
    }

    @Test
    public void test_Case0Strict_succeedsOn_Branch1Empty() {
        With(some(__()), Handles.OK_0);
    }

    @Test
    public void test_Case0Strict_succeedsOn_Branch2EmptyEmpty() {
        With(tuple(__(), __()), Handles.OK_0);
    }

    @Test
    public void test_Case1_succeedsOn_Branch2EmptyValue() {
        With(tuple(__(), id()), Handles.OK_1);
    }

    @Test
    public void test_Case1_failsOn_Branch2ValueValue() {
        assertThrows(ExtractionFailedException.class, () -> With(tuple(id(), id()), Handles.FAIL_1));
    }

    @Test
    public void test_Case0Strict_failsOn_Branch2EmptyValue() {
        assertThrows(ExtractionFailedException.class, () -> With(tuple(__(), id()), Handles.FAIL_0));
    }

    @Test
    public void test_Case2_failsOn_Branch2EmptyValue() {
        assertThrows(ExtractionFailedException.class, () -> With(tuple(__(), id()), Handles.FAIL_2));
    }

    @Test
    public void test_Case2_failsOn_Branch2EmptyEmpty() {
        assertThrows(ExtractionFailedException.class, () -> With(tuple(__(), __()), Handles.FAIL_2));
    }

    @Test
    public void test_Case2_succeedsOn_Branch2ValueValue() {
        With(tuple(id(), id()), Handles.FAIL_2);
    }

    @Test
    public void test_Case3_failsOn_Branch2Branch2EmptyEmptyEmpty() {
        assertThrows(ExtractionFailedException.class, () -> With(tuple(tuple(__(), __()), __()), Handles.FAIL_3));
    }

    @Test
    public void test_Case3_failsOn_Branch2Branch2ValueEmptyEmpty() {
        assertThrows(ExtractionFailedException.class, () -> With(tuple(tuple(id(), __()), __()), Handles.FAIL_3));
    }

    @Test
    public void test_Case3_failsOn_Branch2Branch2ValueValueEmpty() {
        assertThrows(ExtractionFailedException.class, () -> With(tuple(tuple(id(), id()), __()), Handles.FAIL_3));
    }

    @Test
    public void test_Case3_succeedsOn_Branch2Branch2ValueValueValue() {
        With(tuple(tuple(id(), id()), id()), Handles.OK_3);
    }

}
