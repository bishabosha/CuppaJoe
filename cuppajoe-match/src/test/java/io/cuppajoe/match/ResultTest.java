package io.cuppajoe.match;

import org.junit.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertIterableEquals;

public class ResultTest {

    @Test
    public void iterating() {
        assertFalse(Result.empty().values().hasNext());
        assertIterableEquals(
            List.of(1),
            () -> Result.of(1).values()
        );
        assertIterableEquals(
            List.of(1,2),
            () -> Result.compose(Result.of(1), Result.of(2)).values()
        );
        assertIterableEquals(
            List.of(1, 2, 3, 4),
            () -> Result.compose(Result.compose(Result.of(1), Result.of(2), Result.of(3)), Result.of(4)).values()
        );
        assertIterableEquals(
            List.of("One", "Two", "Three"),
            () -> Result.compose(Result.compose(Result.of("One"), Result.compose(Result.of("Two"), Result.empty()), Result.empty()), Result.of("Three")).values()
        );
    }
}