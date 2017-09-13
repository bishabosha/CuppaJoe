package com.bishabosha.caffeine.functional.immutable;

import com.bishabosha.caffeine.base.Iterables;
import com.bishabosha.caffeine.functional.patterns.Case;
import com.bishabosha.caffeine.functional.Foldable;
import com.bishabosha.caffeine.functional.control.Option;
import com.bishabosha.caffeine.functional.tuples.Tuple2;
import org.jetbrains.annotations.NotNull;

import java.util.Iterator;
import java.util.Objects;

import static com.bishabosha.caffeine.functional.API.Tuple;

public class Queue<E> implements Foldable<Option<E>> {
    private Cons<E> head;
    private Cons<E> tail;

    private Queue(Cons<E> head, Cons<E> tail) {
        Objects.requireNonNull(head, "Queue head was null.");
        Objects.requireNonNull(tail, "Queue tail was null.");
        this.head = head;
        this.tail = tail;
    }

    @NotNull
    public static <T> Queue<T> empty() {
        return new Queue<>(Cons.empty(), Cons.empty());
    }

    @NotNull
    public static <T> Queue<T> of(T elem) {
        return new Queue<>(Cons.of(elem), Cons.empty());
    }

    @NotNull
    @SafeVarargs
    public static <T> Queue<T> of(T... elems) {
        return new Queue<>(Cons.of(elems), Cons.empty());
    }

    public final boolean isEmpty() {
        return head.isEmpty() && tail.isEmpty();
    }

    public final int size() {
        return head.size() + tail.size();
    }

    @NotNull
    public final Queue<E> enqueue(E element) {
        return new Queue<>(head, tail.push(element));
    }

    @NotNull
    @SafeVarargs
    public final Queue<E> enqueueAll(E... elements) {
        Cons<E> newTail = tail;
        for (E elem: elements) {
            newTail = newTail.push(elem);
        }
        return new Queue<>(head, newTail);
    }

    public Option<Tuple2<Option<E>, Queue<E>>> dequeue() {
        final Cons<E> newHead;
        final Cons<E> newTail;
        if (head.isEmpty()) {
            newHead = tail.reverse();
            newTail = head;
        } else {
            newHead = head;
            newTail = tail;
        }
        return newHead.pop()
                      .map(t -> Tuple(t.$1(), new Queue<>(t.$2(), newTail)));
    }

    public <U> Option<U> dequeue(Case<Tuple2<Option<E>, Queue<E>>, U> matcher) {
        return dequeue().match(matcher);
    }

    @Override
    public Queue<E> reverse() {
        return new Queue<>(tail, head.reverse());
    }

    @Override
    public Iterator<Option<E>> iterator() {
        return Iterables.concat(head, tail.reverse()).iterator();
    }

    public Iterable<E> flatten() {
        return Iterables.concat(head.flatten(), tail.reverse().flatten());
    }

    @Override
    public String toString() {
        return Iterables.toString('[', ']', flatten().iterator());
    }
}
