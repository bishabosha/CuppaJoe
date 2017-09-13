/*
 * Copyright (c) 2017. Jamie Thompson <bishbashboshjt@gmail.com>
 */

package com.bishabosha.caffeine.pipelines;

import com.bishabosha.caffeine.base.Iterables;
import com.bishabosha.caffeine.functional.patterns.Case;
import com.bishabosha.caffeine.functional.control.Option;
import com.bishabosha.caffeine.hashtables.HashTable;

import java.util.*;
import java.util.function.*;
import java.util.stream.*;

/**
 * Pipeline diverges from the Stream interface with new methods
 * <ul>
 *     <li>Elements are consumed successively from the head until they reach the tail.</li>
 *     <li>
 *         Each node may be perform the following, as specified by the {@code Stream} interface:
 *         <ol>
 *             <li>Terminate the pipeline computation</li>
 *             <li>Select which elements will pass through</li>
 *             <li>Mutate a passing element</li>
 *             <li>map a passing element to a new Type</li>
 *             <li>Collect passing elements to a variable</li>
 *             <li>Any combination with the above</li>
 *         </ol>
 *     </li>
 * </ul>
 * @param <T> the Type with elements outputted by the Pipeline
 */
public class Pipeline<T> extends AbstractPipeline<T> {

    private Pipeline(Iterable head) {
        super(head);
    }

    private Pipeline(AbstractPipeline pipeline) {
        super(pipeline);
    }

    public static <R> Pipeline<R> of(R... values) {
        return new Pipeline<>(Iterables.wrap(values));
    }

    public static <R> Pipeline<R> of(Iterable<R> it) {
        return new Pipeline<>(it);
    }

    public static <R> Pipeline<R> fromOptional(Optional<R> option) {
        return new Pipeline<>(Iterables.fromOptional(option));
    }

    public static <R> Pipeline<R> empty() {
        return of(Iterables.empty());
    }

    public static <R> Pipeline<R> copy(AbstractPipeline pipeline) {
        return new Pipeline<>(pipeline);
    }

    public static <R> Pipeline<R> cycle(Iterable<R> source) {
        return Pipeline.of(Iterables.cycle(source));
    }

    public static <R> Pipeline<R> cycle(R... values) {
        return Pipeline.of(Iterables.cycle(values));
    }

    public static <R> Pipeline<R> stream(Stream<R> stream) {
        return new Pipeline<>(() -> stream.iterator());
    }

    public static <R> Pipeline<R> iterate(R identity,
                                          UnaryOperator<R> accumulator) {
        return new Pipeline<>(Iterables.iterate(identity, accumulator));
    }

    public static <R> Pipeline<R> iterate(R identity,
                                          Predicate<R> terminatingCondition,
                                          UnaryOperator<R> accumulator) {
        return new Pipeline<>(Iterables.iterate(identity, terminatingCondition, accumulator));
    }

    /**
     * Concatenate pipelines lazily, even if they depend on each other.
     * @param pipelines the list of pipelines to concatenate. Please order by execution.
     * @return A stream that will iterate through each stream sequentially.
     */
    public static <R> Pipeline<R> concat(Pipeline<R>... pipelines) {
        return Pipeline.of(() -> new Iterator<R>() {
            Iterator<R> current;
            int i = 0;

            @Override
            public boolean hasNext() {
                return (current != null && current.hasNext())
                        || getNext();
            }

            public boolean getNext() {
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
        });
    }

    public Pipeline<T> filter(Predicate<? super T> predicate) {
        return (Pipeline<T>) addFunction(ComputeNode.filter((Predicate<T>)predicate, false));
    }

    public <R> Pipeline<R> map(Function<? super T, ? extends R> mapper) {
        return new Pipeline<>(addFunction(ComputeNode.map(mapper)));
    }

    public <R, V> Pipeline<V> zipWith(Iterable<R> other, BiFunction<T, R, V> function) {
        Iterator<R> otherIt = other.iterator();
        takeWhile(x -> otherIt.hasNext());
        return map(x -> function.apply(x, otherIt.next()));
    }

    public <R, V> Pipeline<V> product(Iterable<R> other, BiFunction<T, R, V> function) {
        return flatMap(x -> Pipeline.of(other)
                                    .map(y -> function.apply(x, y)));
    }

    public <R> Pipeline<R> flatMap(Function<? super T, ? extends Pipeline> mapper) {
        return new Pipeline<>(addFunction(ComputeNode.flatMap(mapper)));
    }

    public <R> Pipeline<R> match(Case<T, R> testCase) {
        return flatMap(x -> of(testCase.match(x)));
    }

    public Pipeline<T> peek(Consumer<? super T> action) {
        return (Pipeline<T>) addFunction(ComputeNode.peek(action));
    }

    public Pipeline<T> distinct() {
        Set<T> distinct = new HashTable<>();
        return (Pipeline<T>) addFunction(ComputeNode.filter(distinct::add, false));
    }

    public Pipeline<T> sorted() {
        return sorted(Comparator.naturalOrder());
    }

    public Pipeline<T> sorted(Comparator comparator) {
        Object[] toSort = toArray();
        Arrays.sort(toSort, comparator);
        return new Pipeline<>(Iterables.wrap(toSort));
    }

    public Pipeline<T> takeWhile(Predicate<? super T> predicate) {
        return (Pipeline<T>) addFunction(ComputeNode.filter(predicate, true));
    }

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
    public Pipeline<T> limit(long maxSize) {
        return (Pipeline<T>) addFunction(getLimit(maxSize));
    }

    public Pipeline<T> pushLimit(long maxSize) {
        return (Pipeline<T>) pushFunction(getLimit(maxSize));
    }

    public Pipeline<T> skip(long n) {
        return (Pipeline<T>) addFunction(getSkip(n));
    }

    public Pipeline<T> pushSkip(long n) {
        return (Pipeline<T>) pushFunction(getSkip(n));
    }

    public Option<T> findFirst() {
        Iterator<T> it = iterator();
        return it.hasNext() ? Option.ofUnknown(it.next()) : Option.nothing();
    }

    /**
     * See {@link Stream#reduce(Object, BinaryOperator)}
     */
    public T reduce(T identity, BinaryOperator<T> accumulator) {
        Iterator<T> it = iterator();
        while (it.hasNext()) {
            identity = accumulator.apply(identity, it.next());
        }
        return identity;
    }

    /**
     * See {@link Stream#reduce(BinaryOperator)}
     */
    public Option<T> reduce(BinaryOperator<T> accumulator) {
        boolean foundAny = false;
        T result = null;
        for (T element: this) {
            if (!foundAny) {
                foundAny = true;
                result = element;
            } else {
                result = accumulator.apply(result, element);
            }
        }
        return foundAny ? Option.ofUnknown(result) : Option.nothing();
    }

    /**
     * @see {@link Stream#reduce(Object, BiFunction, BinaryOperator)}
     */
    public <U> U reduce(U identity, BiFunction<U, ? super T, U> accumulator, BinaryOperator<U> combiner) {
        U result = identity;
        for (T element: this) {
            result = combiner.apply(result, accumulator.apply(identity, element));
        }
        return result;
    }

    /**
     * See {@link Stream#collect(Collector)}
     */
    public <R, A> R collect(Collector<? super T, A, R> collector) {
        A result = collector.supplier().get();
        forEach(x -> collector.accumulator().accept(result, x));
        return collector.finisher().apply(result);
    }

    /**
     * See {@link Stream#collect(Supplier, BiConsumer, BiConsumer)}
     */
    public <R> R collect(Supplier<R> supplier, BiConsumer<R, ? super T> accumulator) {
        R result = supplier.get();
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