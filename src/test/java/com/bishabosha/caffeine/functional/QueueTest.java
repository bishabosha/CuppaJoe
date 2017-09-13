package com.bishabosha.caffeine.functional;

import org.junit.Test;

import static com.bishabosha.caffeine.functional.API.Some;
import static org.hamcrest.Matchers.contains;
import static org.junit.Assert.*;

public class QueueTest {

    @Test
    public void size() {
        assertTrue(Queue.empty().size() == 0);
        assertTrue(Queue.of(1).size() == 1);
        assertTrue(Queue.of(1,2).size() == 2);
        assertTrue(Queue.empty().enqueue(1).size() == 1);
    }

    @Test
    public void elements_dequeued_when_added_to_empty() {
        assertTrue(Queue.empty().enqueue(1).dequeue().map(t -> t.$1().isSome()).orElse(false));
    }

    @Test
    public void iterate_over_of() {
        assertThat(
            Queue.of(1, 2, 3, 4, 5),
            contains(Some(1), Some(2), Some(3), Some(4), Some(5))
        );
    }

    @Test
    public void iterate_over_empty_concat() {
        assertThat(
            Queue.empty().enqueue(1).enqueue(2).enqueue(3).enqueue(4).enqueue(5),
            contains(Some(1), Some(2), Some(3), Some(4), Some(5))
        );
        assertThat(
            Queue.empty().enqueueAll(1, 2, 3, 4, 5),
            contains(Some(1), Some(2), Some(3), Some(4), Some(5))
        );
    }

    @Test
    public void print() {
        assertEquals("[1, 2, 3, 4, 5]", Queue.of(1,2,3,4,5).toString());
        assertEquals("[1, 2, 3, 4, 5]", Queue.empty().enqueueAll(1, 2, 3, 4, 5));
    }
}