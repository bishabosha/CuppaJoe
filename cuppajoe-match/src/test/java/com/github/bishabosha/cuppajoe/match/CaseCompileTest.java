package com.github.bishabosha.cuppajoe.match;

import org.junit.jupiter.api.Test;

import static com.github.bishabosha.cuppajoe.match.API.With;
import static com.github.bishabosha.cuppajoe.match.patterns.Standard.*;
import static com.github.bishabosha.cuppajoe.match.patterns.Collections.*;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class CaseCompileTest {

    /**
     * This fails as it is impossible to bind 1 variable from the {@code __()} wildcard
     */
    @Test
    public void test_Case1_failsOn_Empty() {
        assertThrows(ExtractionFailedException.class, () -> With(__(), x -> "Fail"));
    }

    /**
     * This succeeds as the {@code id()} pattern binds a value
     */
    @Test
    public void test_Case1_succeedsOn_Value() {
        With(id(), x -> "OK");
    }

    /**
     * This fails as {@code id()} expects to bind a value and there is no consumer to accept it
     */
    @Test
    public void test_Case0Strict_failsOn_Value() {
        assertThrows(ExtractionFailedException.class, () -> With(id(), "Fail"));
    }

    /**
     * This fails as {@code id()} expects to bind a value and there is no consumer to accept it
     */
    @Test
    public void test_Case0Lazy_failsOn_Value() {
        assertThrows(ExtractionFailedException.class, () -> With(id(), () -> "Fail"));
    }

    @Test
    public void test_Case0Lazy_failsOn_Branch1Value() {
        assertThrows(ExtractionFailedException.class, () -> With(some(id()), () -> "Fail"));
    }

    @Test
    public void test_Case0Strict_failsOn_Branch1Value() {
        assertThrows(ExtractionFailedException.class, () -> With(some(id()), "Fail"));
    }

    @Test
    public void test_Case1_succeedsOn_Branch1Branch1Value() {
        With(some(some(id())), x -> "OK");
    }

    @Test
    public void test_Case1_failsOn_Branch1Branch1Empty() {
        assertThrows(ExtractionFailedException.class, () -> With(some(some(__())), x -> "Fail"));
    }

    @Test
    public void test_Case0Lazy_succeedsOn_Branch1Empty() {
        With(some(__()), () -> "OK");
    }

    @Test
    public void test_Case0Strict_succeedsOn_Branch1Empty() {
        With(some(__()), "OK");
    }

    @Test
    public void test_Case0Strict_succeedsOn_Branch2EmptyEmpty() {
        With(tuple(__(), __()), "OK");
    }

    @Test
    public void test_Case1_succeedsOn_Branch2EmptyValue() {
        With(tuple(__(), id()), x -> "OK");
    }

    @Test
    public void test_Case1_failsOn_Branch2ValueValue() {
        assertThrows(ExtractionFailedException.class, () -> With(tuple(id(), id()), x -> "Fail"));
    }

    @Test
    public void test_Case0Strict_failsOn_Branch2EmptyValue() {
        assertThrows(ExtractionFailedException.class, () -> With(tuple(__(), id()), "Fail"));
    }

    @Test
    public void test_Case2_failsOn_Branch2EmptyValue() {
        assertThrows(ExtractionFailedException.class, () -> With(tuple(__(), id()), (x, y) -> "Fail"));
    }

    @Test
    public void test_Case2_failsOn_Branch2EmptyEmpty() {
        assertThrows(ExtractionFailedException.class, () -> With(tuple(__(), __()), (x, y) -> "Fail"));
    }

    @Test
    public void test_Case2_succeedsOn_Branch2ValueValue() {
        With(tuple(id(), id()), (x, y) -> "OK");
    }

    @Test
    public void test_Case3_failsOn_Branch2Branch2EmptyEmptyEmpty() {
        assertThrows(ExtractionFailedException.class, () -> With(tuple(tuple(__(), __()), __()), (x, y, z) -> "Fail"));
    }

    @Test
    public void test_Case3_failsOn_Branch2Branch2ValueEmptyEmpty() {
        assertThrows(ExtractionFailedException.class, () -> With(tuple(tuple(id(), __()), __()), (x, y, z) -> "Fail"));
    }

    @Test
    public void test_Case3_failsOn_Branch2Branch2ValueValueEmpty() {
        assertThrows(ExtractionFailedException.class, () -> With(tuple(tuple(id(), id()), __()), (x, y, z) -> "Fail"));
    }

    @Test
    public void test_Case3_succeedsOn_Branch2Branch2ValueValueValue() {
        With(tuple(tuple(id(), id()), id()), (x, y, z) -> "OK");
    }

    @Test
    public void test_Case3_succeedsOn_varargsValueValueValue() {
        With(arr(id(), id(), id()), (x, y, z) -> "OK");
    }

    @Test
    public void test_Case2_succeedsOn_varargsValueValue() {
        With(arr(id(), id()), (x, y) -> "OK");
    }

    @Test
    public void test_Case1_failsOn_varargsValueValue() {
        assertThrows(ExtractionFailedException.class, () -> With(arr(id(), id()), (x) -> "FAIL"));
    }
}
