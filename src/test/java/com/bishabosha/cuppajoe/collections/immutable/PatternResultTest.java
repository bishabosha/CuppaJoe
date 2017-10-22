package com.bishabosha.cuppajoe.collections.immutable;

import com.bishabosha.cuppajoe.patterns.PatternResult;
import org.hamcrest.Matchers;
import org.junit.Test;

import static org.junit.Assert.*;

public class PatternResultTest {

    @Test
    public void print() {
        assertEquals("[]", PatternResult.of().toString());
        assertEquals("[1]", PatternResult.of(1).toString());
        assertEquals("[1, 2]", PatternResult.of(1,2).toString());
        assertEquals("[1, 2]", PatternResult.compose(PatternResult.of(1), PatternResult.of(2)).toString());
        assertEquals("[1, 2, 3, 4]", PatternResult.compose(PatternResult.compose(PatternResult.of(1), PatternResult.of(2), PatternResult.of(3)), PatternResult.of(4)).toString());
    }

    @Test
    public void iterating() {
        assertThat(
            PatternResult.of(),
            Matchers.emptyIterable()
        );
        assertThat(
            PatternResult.of(1),
            Matchers.contains(1)
        );
        assertThat(
            PatternResult.of(1, 2),
            Matchers.contains(1, 2)
        );
        assertThat(
            PatternResult.compose(PatternResult.of(1), PatternResult.of(2)),
            Matchers.contains(1, 2)
        );
        assertThat(
            PatternResult.compose(PatternResult.compose(PatternResult.of(1), PatternResult.of(2), PatternResult.of(3)), PatternResult.of(4)),
            Matchers.contains(1, 2, 3, 4)
        );
        assertThat(
            PatternResult.compose(PatternResult.compose(PatternResult.of("One"), PatternResult.compose(PatternResult.of("Two"), PatternResult.empty()), PatternResult.empty()), PatternResult.of("Three")),
            Matchers.contains("One", "Two", "Three")
        );
    }
}