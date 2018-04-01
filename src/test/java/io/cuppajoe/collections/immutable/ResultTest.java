package io.cuppajoe.collections.immutable;

import io.cuppajoe.match.Result;
import org.hamcrest.Matchers;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

public class ResultTest {

    @Test
    public void print() {
        assertEquals("[]", Result.empty().toString());
        assertEquals("[1]", Result.of(1).toString());
        assertEquals("[1, 2]", Result.compose(Result.of(1), Result.of(2)).toString());
        assertEquals("[1, 2, 3, 4]", Result.compose(Result.compose(Result.of(1), Result.of(2), Result.of(3)), Result.of(4)).toString());
    }

    @Test
    public void iterating() {
        assertThat(
            Result.empty(),
            Matchers.emptyIterable()
        );
        assertThat(
            Result.of(1),
            Matchers.contains(1)
        );
        assertThat(
            Result.compose(Result.of(1), Result.of(2)),
            Matchers.contains(1, 2)
        );
        assertThat(
            Result.compose(Result.compose(Result.of(1), Result.of(2), Result.of(3)), Result.of(4)),
            Matchers.contains(1, 2, 3, 4)
        );
        assertThat(
            Result.compose(Result.compose(Result.of("One"), Result.compose(Result.of("Two"), Result.empty()), Result.empty()), Result.of("Three")),
            Matchers.contains("One", "Two", "Three")
        );
    }
}