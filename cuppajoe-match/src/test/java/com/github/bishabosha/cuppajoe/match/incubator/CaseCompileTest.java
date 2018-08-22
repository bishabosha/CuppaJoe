package com.github.bishabosha.cuppajoe.match.incubator;

import org.junit.jupiter.api.Test;

import static com.github.bishabosha.cuppajoe.match.incubator.API.With;
import static com.github.bishabosha.cuppajoe.match.incubator.patterns.Standard.*;
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
}
