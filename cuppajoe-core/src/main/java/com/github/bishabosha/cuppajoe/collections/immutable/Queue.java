package com.github.bishabosha.cuppajoe.collections.immutable;

import com.github.bishabosha.cuppajoe.annotation.NonNull;
import com.github.bishabosha.cuppajoe.control.Option;
import com.github.bishabosha.cuppajoe.tuples.Tuple2;
import com.github.bishabosha.cuppajoe.typeclass.foldable.Foldable;
import com.github.bishabosha.cuppajoe.typeclass.functor.Functor1;
import com.github.bishabosha.cuppajoe.typeclass.value.Value1;
import com.github.bishabosha.cuppajoe.util.Iterators;

import java.util.Iterator;
import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;

import static com.github.bishabosha.cuppajoe.API.Tuple;

public class Queue<E> implements Foldable<E>, Bunch<E>, Value1<Queue, E>, Functor1<Queue, E> {
    private List<E> head;
    private List<E> tail;

    private Queue(List<E> head, List<E> tail) {
        this.head = head;
        this.tail = tail;
    }

    public static <T> Queue<T> empty() {
        return new Queue<>(List.empty(), List.empty());
    }

    public static <T> Queue<T> singleton(T elem) {
        return new Queue<>(List.singleton(elem), List.empty());
    }

    @SafeVarargs
    public static <T> Queue<T> ofAll(T... elems) {
        return new Queue<>(List.ofAll(elems), List.empty());
    }

    public final boolean isEmpty() {
        return head.isEmpty() && tail.isEmpty();
    }

    @Override
    public Queue<E> or(@NonNull Supplier<? extends Value1<Queue, ? extends E>> alternative) {
        Objects.requireNonNull(alternative, "alternative");
        return isEmpty() ? Value1.Type.narrow(alternative.get()) : this;
    }

    public final int size() {
        return head.size() + tail.size();
    }

    @Override
    public E get() {
        return head.isEmpty() ? tail.reverse().head() : head.head();
    }

    @Override
    public <R> Queue<R> map(@NonNull Function<? super E, ? extends R> mapper) {
        Objects.requireNonNull(mapper, "mapper");
        return foldLeft(empty(), (xs, x) -> xs.enqueue(mapper.apply(x)));
    }

    public final Queue<E> enqueue(E element) {
        return new Queue<>(head, tail.push(element));
    }

    @SafeVarargs
    public final Queue<E> enqueueAll(@NonNull E... elements) {
        Objects.requireNonNull(elements, "elements");
        var newTail = tail;
        for (var elem : elements) {
            newTail = newTail.push(elem);
        }
        return new Queue<>(head, newTail);
    }

    public Option<Tuple2<E, Queue<E>>> dequeue() {
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
                .map(t ->
                        t.flatMap((head, tail) ->
                                Tuple(head, new Queue<>(tail, newTail))));
    }

    public Queue<E> reverse() {
        return new Queue<>(tail, head.reverse());
    }

    @Override
    public Iterator<E> iterator() {
        return Iterators.concat(head, tail.reverse());
    }

    @Override
    public <A> A foldRight(A accumulator, @NonNull BiFunction<A, E, A> mapper) {
        return reverse().foldLeft(accumulator, mapper);
    }

    @Override
    public int hashCode() {
        return foldLeft(1, (hash, x) -> 31 * hash + (x == null ? 0 : Objects.hashCode(x)));
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
        return Iterators.toString('[', ']', iterator());
    }
}
