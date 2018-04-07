/*
 * Copyright (c) 2017. Jamie Thompson <bishbashboshjt@gmail.com>
 */

package com.github.bishabosha.cuppajoe.collections.mutable.pipelines;

import com.github.bishabosha.cuppajoe.API;
import com.github.bishabosha.cuppajoe.collections.mutable.hashtables.HashTable;
import com.github.bishabosha.cuppajoe.control.Option;
import com.github.bishabosha.cuppajoe.functions.Func1;
import com.github.bishabosha.cuppajoe.util.Iterators;

import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.BinaryOperator;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.function.UnaryOperator;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Pipeline diverges from the Stream interface with new methods
 * <ul>
 * <li>Elements are consumed successively from the head until they reach the tail.</li>
 * <li>
 * Each node may be perform the following, as specified by the {@code Stream} interface:
 * <ol>
 * <li>Terminate the pipeline computation</li>
 * <li>Select which elements will pass through</li>
 * <li>Mutate a passing element</li>
 * <li>map a passing element to a new Type</li>
 * <li>Collect passing elements to a variable</li>
 * <li>Any combination with the above</li>
 * </ol>
 * </li>
 * </ul>
 *
 * @param <T> the Type with elements outputted by the Pipeline
 */
public class Pipeline<T> extends AbstractPipeline<T> {

    private Pipeline(Iterable head) {
        super(head);
    }

    private Pipeline(AbstractPipeline pipeline) {
        super(pipeline);
    }

    @SafeVarargs
    public static <R> Pipeline<R> ofAll(R... values) {
        return new Pipeline<>(() -> Iterators.array(values));
    }

    public static <R> Pipeline<R> ofAll(Iterable<R> it) {
        return new Pipeline<>(it);
    }

    public static <R> Pipeline<R> fromOptional(Optional<R> option) {
        return new Pipeline<>(() -> Iterators.fromOptional(option));
    }

    public static <R> Pipeline<R> empty() {
        return ofAll(Collections::emptyIterator);
    }

    public static <R> Pipeline<R> copy(AbstractPipeline pipeline) {
        return new Pipeline<>(pipeline);
    }

    public static <R> Pipeline<R> cycle(Iterable<R> source) {
        return Pipeline.ofAll(() -> Iterators.cycle(source));
    }

    public static <R> Pipeline<R> cycle(R... values) {
        return Pipeline.ofAll(() -> Iterators.cycle(values));
    }

    public static <R> Pipeline<R> stream(Stream<R> stream) {
        return new Pipeline<>(() -> stream.iterator());
    }

    public static <R> Pipeline<R> iterate(R identity,
                                          UnaryOperator<R> accumulator) {
        return new Pipeline<>(() -> Iterators.iterate(identity, accumulator));
    }

    public static <R> Pipeline<R> iterate(R identity,
                                          Predicate<R> terminatingCondition,
                                          UnaryOperator<R> accumulator) {
        return new Pipeline<>(() -> Iterators.iterate(identity, terminatingCondition, accumulator));
    }

    /**
     * Concatenate pipelines lazily, even if they depend on each other.
     *
     * @param pipelines the list of pipelines to concatenate. Please order by execution.
     * @return A stream that will iterate through each stream sequentially.
     */
    @SafeVarargs
    public static <R> Pipeline<R> concat(Pipeline<R>... pipelines) {
        Iterable<R> concatIterable = () -> new Iterator<>() {
            Iterator<R> current;
            int i = 0;

            @Override
            public boolean hasNext() {
                return (current != null && current.hasNext())
                        || getNext();
            }

            private boolean getNext() {
                while (i < pipelines.length) {
                    if ((current = pipelines[i++].iterator()).hasNext()) {
                        return true;
                    }
                }
                return false;
            }

            @Override
            public R next() {
                return current.next();
            }
        };
        return Pipeline.ofAll(concatIterable);
    }

    @SuppressWarnings("unchecked")
    public Pipeline<T> filter(Predicate<? super T> predicate) {
        return (Pipeline<T>) addFunction(ComputeNode.filter((Predicate<T>) predicate, false));
    }

    public <R> Pipeline<R> map(Func1<? super T, ? extends R> mapper) {
        return new Pipeline<>(addFunction(ComputeNode.map(mapper)));
    }

    public <R, V> Pipeline<V> zipWith(Iterable<R> other, BiFunction<T, R, V> function) {
        var otherIt = other.iterator();
        takeWhile(x -> otherIt.hasNext());
        return map(x -> function.apply(x, otherIt.next()));
    }

    public <R, V> Pipeline<V> product(Iterable<R> other, BiFunction<T, R, V> function) {
        return flatMap(x -> Pipeline.ofAll(other)
                .map(y -> function.apply(x, y)));
    }

    public <R> Pipeline<R> flatMap(Func1<? super T, ? extends Pipeline> mapper) {
        return new Pipeline<>(addFunction(ComputeNode.flatMap(mapper)));
    }

    @SuppressWarnings("unchecked")
    public Pipeline<T> peek(Consumer<? super T> action) {
        return (Pipeline<T>) addFunction(ComputeNode.peek(action));
    }

    @SuppressWarnings("unchecked")
    public Pipeline<T> distinct() {
        var distinct = new HashTable<T>();
        return (Pipeline<T>) addFunction(ComputeNode.filter(distinct::add, false));
    }

    public Pipeline<T> sorted() {
        return sorted(Comparator.naturalOrder());
    }

    @SuppressWarnings("unchecked")
    public Pipeline<T> sorted(Comparator comparator) {
        var toSort = toArray();
        Arrays.sort(toSort, comparator);
        return new Pipeline<>(() -> Iterators.array(toSort));
    }

    @SuppressWarnings("unchecked")
    public Pipeline<T> takeWhile(Predicate<? super T> predicate) {
        return (Pipeline<T>) addFunction(ComputeNode.filter(predicate, true));
    }

    @SuppressWarnings("unchecked")
    public Pipeline<T> dropWhile(Predicate<? super T> predicate) {
        return (Pipeline<T>) addFunction(ComputeNode.filter(new Predicate<T>() {
            boolean willDrop = true;

            @Override
            public boolean test(T t) {
                if (willDrop) {
                    willDrop = predicate.test(t);
                }
                return !willDrop;
            }
        }, false));
    }

    /**
     * See {@link Stream#limit(long)}
     */
    @SuppressWarnings("unchecked")
    public Pipeline<T> limit(long maxSize) {
        return (Pipeline<T>) addFunction(getLimit(maxSize));
    }

    @SuppressWarnings("unchecked")
    public Pipeline<T> pushLimit(long maxSize) {
        return (Pipeline<T>) pushFunction(getLimit(maxSize));
    }

    @SuppressWarnings("unchecked")
    public Pipeline<T> skip(long n) {
        return (Pipeline<T>) addFunction(getSkip(n));
    }

    @SuppressWarnings("unchecked")
    public Pipeline<T> pushSkip(long n) {
        return (Pipeline<T>) pushFunction(getSkip(n));
    }

    public Option<T> findFirst() {
        var it = iterator();
        return API.Option(it::hasNext, it::next);
    }

    /**
     * See {@link Stream#reduce(Object, BinaryOperator)}
     */
    public T reduce(T identity, BinaryOperator<T> accumulator) {
        var it = iterator();
        while (it.hasNext()) {
            identity = accumulator.apply(identity, it.next());
        }
        return identity;
    }

    /**
     * See {@link Stream#reduce(BinaryOperator)}
     */
    public Option<T> reduce(BinaryOperator<T> accumulator) {
        T result = null;
        var foundAny = false;
        for (var element : this) {
            if (!foundAny) {
                foundAny = true;
                result = element;
            } else {
                result = accumulator.apply(result, element);
            }
        }
        return foundAny ? API.Some(result) : API.None();
    }

    /**
     * {@link Stream#reduce(Object, BiFunction, BinaryOperator)}
     */
    public <U> U reduce(U identity, BiFunction<U, ? super T, U> accumulator, BinaryOperator<U> combiner) {
        var result = identity;
        for (var element : this) {
            result = combiner.apply(result, accumulator.apply(identity, element));
        }
        return result;
    }

    /**
     * {@link Stream#collect(Collector)}
     */
    public <R, A> R collect(Collector<? super T, A, R> collector) {
        var result = collector.supplier().get();
        forEach(x -> collector.accumulator().accept(result, x));
        return collector.finisher().apply(result);
    }

    /**
     * {@link Stream#collect(Supplier, BiConsumer, BiConsumer)}
     */
    public <R> R collect(Supplier<R> supplier, BiConsumer<R, ? super T> accumulator) {
        var result = supplier.get();
        forEach(x -> accumulator.accept(result, x));
        return result;
    }

    /**
     * See {@link Stream#min(Comparator)}
     */
    public Option<T> min(Comparator<? super T> comparator) {
        return Option.from(collect(Collectors.minBy(comparator)));
    }

    /**
     * See {@link Stream#max(Comparator)}
     */
    public Option<T> max(Comparator<? super T> comparator) {
        return Option.from(collect(Collectors.maxBy(comparator)));
    }

    /**
     * See {@link Stream#count()}
     */
    public long count() {
        return collect(Collectors.counting());
    }

    /**
     * See {@link Stream#anyMatch(Predicate)}
     */
    public boolean anyMatch(Predicate<? super T> predicate) {
        return matcher(true, true, false, predicate);
    }

    /**
     * See {@link Stream#allMatch(Predicate)}
     */
    public boolean allMatch(Predicate<? super T> predicate) {
        return matcher(false, false, true, predicate);
    }

    /**
     * See {@link Stream#noneMatch(Predicate)}
     */
    public boolean noneMatch(Predicate<? super T> predicate) {
        return matcher(true, false, true, predicate);
    }

}