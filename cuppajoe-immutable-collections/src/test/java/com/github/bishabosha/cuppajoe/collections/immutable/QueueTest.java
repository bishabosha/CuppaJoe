package com.github.bishabosha.cuppajoe.collections.immutable;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class QueueTest {

    @Test
    public void test_size() {
        assertEquals(0, Queue.empty().size());
        assertEquals(1, Queue.singleton(1).size());
        assertEquals(2, Queue.ofAll(1, 2).size());
        assertEquals(1, Queue.empty().enqueue(1).size());
    }

    @Test
    public void test_elements_dequeued_when_added_to_empty() {
        assertTrue(
            Queue.<Integer>empty()
                .enqueue(1)
                .dequeue()
                .filter(t -> t.compose((head, __) -> head == 1))
                .containsValue()
        );
    }

    @Test
    public void test_iterate_over_of() {
        assertIterableEquals(
            List.of(1, 2, 3, 4, 5),
            Queue.ofAll(1, 2, 3, 4, 5)
        );
    }

    @Test
    public void test_iterate_over_empty_concat() {
        assertIterableEquals(
            List.of(1, 2, 3, 4, 5),
            Queue.empty().enqueue(1).enqueue(2).enqueue(3).enqueue(4).enqueue(5)
        );
        assertIterableEquals(
            List.of(1, 2, 3, 4, 5),
            Queue.empty().enqueueAll(1, 2, 3, 4, 5)
        );
    }

    @Test
    public void test_print() {
        assertEquals("[1, 2, 3, 4, 5]", Queue.ofAll(1, 2, 3, 4, 5).toString());
        assertEquals("[1, 2, 3, 4, 5]", Queue.empty().enqueueAll(1, 2, 3, 4, 5).toString());
    }
}