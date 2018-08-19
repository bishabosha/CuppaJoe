package com.github.bishabosha.cuppajoe.match;

import com.github.bishabosha.cuppajoe.collections.immutable.List;
import com.github.bishabosha.cuppajoe.match.patterns.Result;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertIterableEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ResultTest {

    @Test
    public void test_iterating() {
        assertTrue(ListCapture.capture(Result.empty()).isEmpty());
        assertIterableEquals(
            List.singleton(1),
            ListCapture.capture(Result.of(1))
        );
        assertIterableEquals(
            List.ofAll(1, 2),
            ListCapture.capture(Result.compose(Result.of(1), Result.of(2)))
        );
        assertIterableEquals(
            List.ofAll(1, 2, 3, 4),
            ListCapture.capture(Result.compose(Result.compose(Result.of(1), Result.of(2), Result.of(3)), Result.of(4)))
        );
        assertIterableEquals(
            List.ofAll("One", "Two", "Three"),
            ListCapture.capture(Result.compose(Result.compose(Result.of("One"), Result.compose(Result.of("Two"), Result.empty()), Result.empty()), Result.of("Three")))
        );
    }
}