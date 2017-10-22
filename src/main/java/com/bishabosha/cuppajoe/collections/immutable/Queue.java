package com.bishabosha.cuppajoe.collections.immutable;

import com.bishabosha.cuppajoe.Iterables;
import com.bishabosha.cuppajoe.Value;
import com.bishabosha.cuppajoe.functions.Func2;
import com.bishabosha.cuppajoe.patterns.Case;
import com.bishabosha.cuppajoe.Foldable;
import com.bishabosha.cuppajoe.control.Option;
import com.bishabosha.cuppajoe.tuples.Product2;
import com.bishabosha.cuppajoe.tuples.Tuple2;
import org.jetbrains.annotations.NotNull;

import java.util.Iterator;
import java.util.Objects;
import java.util.function.Function;

import static com.bishabosha.cuppajoe.API.Tuple;

public class Queue<E> implements Foldable<E>, Bunch<E> {
    private List<E> head;
    private List<E> tail;

    private Queue(List<E> head, List<E> tail) {
        Objects.requireNonNull(head, "Queue head was null.");
        Objects.requireNonNull(tail, "Queue tail was null.");
        this.head = head;
        this.tail = tail;
    }

    @NotNull
    public static <T> Queue<T> empty() {
        return new Queue<>(List.empty(), List.empty());
    }

    @NotNull
    public static <T> Queue<T> of(T elem) {
        return new Queue<>(List.of(elem), List.empty());
    }

    @NotNull
    @SafeVarargs
    public static <T> Queue<T> of(T... elems) {
        return new Queue<>(List.of(elems), List.empty());
    }

    public final boolean isEmpty() {
        return head.isEmpty() && tail.isEmpty();
    }

    public final int size() {
        return head.size() + tail.size();
    }

    @Override
    public E get() {
        return head.isEmpty() ? tail.reverse().head() : head.head();
    }

    @Override
    public <R> Queue<R> map(Function<? super E, ? extends R> mapper) {
        return foldLeft(empty(), (xs, x) -> xs.enqueue(mapper.apply(x)));
    }

    @NotNull
    public final Queue<E> enqueue(E element) {
        return new Queue<>(head, tail.push(element));
    }

    @NotNull
    @SafeVarargs
    public final Queue<E> enqueueAll(E... elements) {
        List<E> newTail = tail;
        for (E elem: elements) {
            newTail = newTail.push(elem);
        }
        return new Queue<>(head, newTail);
    }

    public Option<Product2<E, Queue<E>>> dequeue() {
        final List<E> newHead;
        final List<E> newTail;
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

    public <U> Option<U> dequeue(Case<Product2<E, Queue<E>>, U> matcher) {
        return dequeue().match(matcher);
    }

    public Queue<E> reverse() {
        return new Queue<>(tail, head.reverse());
    }

    @Override
    public Iterator<E> iterator() {
        return Iterables.concat(head, tail.reverse()).iterator();
    }

    @Override
    public <A> A foldRight(A accumulator, Func2<A, E, A> mapper) {
        return reverse().fold(accumulator, mapper);
    }

    @Override
    public int hashCode() {
        return foldLeft(1, (hash, x) -> 31*hash + (x == null ? 0 : Objects.hashCode(x)));
    }

    @Override
    public boolean equals(Object obj) {
        return obj == this || !Option.of(obj)
            .cast(Queue.class)
            .filter(q -> allMatchExhaustive(q, Objects::equals))
            .isEmpty();
    }

    @Override
    public String toString() {
        return Iterables.toString('[', ']', iterator());
    }
}
