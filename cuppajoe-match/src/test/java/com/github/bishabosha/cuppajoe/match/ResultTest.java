package com.github.bishabosha.cuppajoe.match;

import com.github.bishabosha.cuppajoe.collections.immutable.List;
import com.github.bishabosha.cuppajoe.match.patterns.Result;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertIterableEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ResultTest {

    @Test
    public void test_iterating() {
        assertTrue(Result.empty().capture().isEmpty());
        assertIterableEquals(
            List.singleton(1),
            Result.of(1).capture()
        );
        assertIterableEquals(
            List.ofAll(1, 2),
            Result.compose(Result.of(1), Result.of(2)).capture()
        );
        assertIterableEquals(
            List.ofAll(1, 2, 3, 4),
            Result.compose(Result.compose(Result.of(1), Result.of(2), Result.of(3)), Result.of(4)).capture()
        );
        assertIterableEquals(
            List.ofAll("One", "Two", "Three"),
            Result.compose(Result.compose(Result.of("One"), Result.compose(Result.of("Two"), Result.empty()), Result.empty()), Result.of("Three")).capture()
        );
    }
}