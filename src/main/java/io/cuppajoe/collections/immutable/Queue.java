package io.cuppajoe.collections.immutable;

import io.cuppajoe.Foldable;
import io.cuppajoe.Iterators;
import io.cuppajoe.control.Option;
import io.cuppajoe.match.Case;
import io.cuppajoe.tuples.Product2;
import io.cuppajoe.typeclass.functor.Functor1;
import io.cuppajoe.typeclass.value.Value1;
import org.jetbrains.annotations.NotNull;

import java.util.Iterator;
import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;

import static io.cuppajoe.API.Tuple;

public class Queue<E> implements Foldable<E>, Bunch<E>, Value1<Queue, E>, Functor1<Queue, E> {
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

    @Override
    public Queue<E> or(Supplier<? extends Value1<Queue, ? extends E>> alternative) {
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
        var newTail = tail;
        for (var elem: elements) {
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
        return Iterators.concat(head, tail.reverse());
    }

    @Override
    public <A> A foldRight(A accumulator, BiFunction<A, E, A> mapper) {
        return reverse().foldLeft(accumulator, mapper);
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
        return Iterators.toString('[', ']', iterator());
    }
}
