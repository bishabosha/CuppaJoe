package io.cuppajoe.collections.immutable;

import io.cuppajoe.match.Result;
import org.hamcrest.Matchers;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;

public class ResultTest {

    @Test
    public void iterating() {
        assertFalse(Result.empty().values().hasNext());
        assertThat(
            () -> Result.of(1).values(),
            Matchers.contains(1)
        );
        assertThat(
            () -> Result.compose(Result.of(1), Result.of(2)).values(),
            Matchers.contains(1, 2)
        );
        assertThat(
            () -> Result.compose(Result.compose(Result.of(1), Result.of(2), Result.of(3)), Result.of(4)).values(),
            Matchers.contains(1, 2, 3, 4)
        );
        assertThat(
            () -> Result.compose(Result.compose(Result.of("One"), Result.compose(Result.of("Two"), Result.empty()), Result.empty()), Result.of("Three")).values(),
            Matchers.contains("One", "Two", "Three")
        );
    }
}