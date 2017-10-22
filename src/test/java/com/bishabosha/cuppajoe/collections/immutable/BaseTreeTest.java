package com.bishabosha.cuppajoe.collections.immutable;

import org.hamcrest.Matchers;
import org.junit.Test;

import static org.junit.Assert.*;

public class BaseTreeTest {

    @Test
    public void print() {
        assertEquals("[]", BaseTree.of().toString());
        assertEquals("[1]", BaseTree.of(1).toString());
        assertEquals("[1, 2]", BaseTree.of(1,2).toString());
        assertEquals("[1, 2]", BaseTree.compose(BaseTree.of(1),BaseTree.of(2)).toString());
        assertEquals("[1, 2, 3, 4]", BaseTree.compose(BaseTree.compose(BaseTree.of(1),BaseTree.of(2),BaseTree.of(3)), BaseTree.of(4)).toString());
    }

    @Test
    public void iterating() {
        assertThat(
            BaseTree.of(),
            Matchers.emptyIterable()
        );
        assertThat(
            BaseTree.of(1),
            Matchers.contains(1)
        );
        assertThat(
            BaseTree.of(1, 2),
            Matchers.contains(1, 2)
        );
        assertThat(
            BaseTree.compose(BaseTree.of(1),BaseTree.of(2)),
            Matchers.contains(1, 2)
        );
        assertThat(
            BaseTree.compose(BaseTree.compose(BaseTree.of(1),BaseTree.of(2),BaseTree.of(3)), BaseTree.of(4)),
            Matchers.contains(1, 2, 3, 4)
        );
        assertThat(
            BaseTree.compose(BaseTree.compose(BaseTree.of("One"), BaseTree.compose(BaseTree.of("Two"), BaseTree.empty()), BaseTree.empty()), BaseTree.of("Three")),
            Matchers.contains("One", "Two", "Three")
        );
    }
}