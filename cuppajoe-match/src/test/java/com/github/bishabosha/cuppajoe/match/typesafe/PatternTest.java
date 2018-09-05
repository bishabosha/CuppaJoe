package com.github.bishabosha.cuppajoe.match.typesafe;

import com.github.bishabosha.cuppajoe.match.typesafe.patterns.Pattern;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class PatternTest {

    @Test
    public void test_Pattern_classEq_isCached() {
        assertSame(Pattern.classEq(String.class), Pattern.classEq(String.class));
    }

    @Test
    public void test_Pattern_classAssignable_isCached() {
        assertSame(Pattern.classAssignable(String.class), Pattern.classAssignable(String.class));
    }

    @Test
    public void test_Pattern_classEq_finalOnly() {
        assertThrows(IllegalArgumentException.class, () -> Pattern.classEq(List.class), "should fail as " + List.class + " is not final");
    }
}
