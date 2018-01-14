package com.bishabosha.cuppajoe.collections.immutable;

import com.bishabosha.cuppajoe.Iterables;
import com.bishabosha.cuppajoe.control.Option;
import com.bishabosha.cuppajoe.functions.*;
import com.bishabosha.cuppajoe.tuples.Product2;
import com.bishabosha.cuppajoe.typeclass.functor.Functor;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Objects;
import java.util.function.Function;

public class Array<E> implements Seq<E>, Functor<Array, E> {
    private Object[] array;

    private Array(Object[] array) {
        this.array = array;
    }

    public static <O> Array<O> of(O... elems) {
        return new Array<>(elems == null ? new Object[]{null} : elems);
    }

    public static <O> Func1<O, Array<O>> of1() {
        return a -> new Array<>(new Object[]{ a });
    }

    public static <O> Func2<O, O, Array<O>> of2() {
        return (a, b) -> new Array<>(new Object[]{ a, b });
    }

    public static <O> Func3<O, O, O, Array<O>> of3() {
        return (a, b, c) -> new Array<>(new Object[]{ a, b, c });
    }

    public static <O> Func4<O, O, O, O, Array<O>> of4() {
        return (a, b, c, d) -> new Array<>(new Object[]{ a, b, c, d });
    }

    public static <O> Func5<O, O, O, O, O, Array<O>> of5() {
        return (a, b, c, d, e) -> new Array<>(new Object[]{ a, b, c, d, e });
    }

    public static <O> Func6<O, O, O, O, O, O, Array<O>> of6() {
        return (a, b, c, d, e, f) -> new Array<>(new Object[]{ a, b, c, d, e, f });
    }

    public static <O> Func7<O, O, O, O, O, O, O, Array<O>> of7() {
        return (a, b, c, d, e, f, g) -> new Array<>(new Object[]{ a, b, c, d, e, f, g });
    }

    public static <O> Func8<O, O, O, O, O, O, O, O, Array<O>> of8() {
        return (a, b, c, d, e, f, g, h) -> new Array<>(new Object[]{ a, b, c, d, e, f, g, h });
    }

    public static <R> Array<R> empty() {
        return new Array<>(new Object[0]);
    }

    @Override
    public List<E> push(E elem) {
        return null;
    }

    @Override
    public Option<? extends Product2<E, ? extends Seq<E>>> pop() {
        return null;
    }

    @Override
    public E get() {
        return get(0);
    }

    @SuppressWarnings("unchecked")
    public E get(int i) {
        return (E) array[i];
    }

    @Override
    public E head() {
        return get();
    }

    @Override
    public Array<E> tail() {
        int length = size() - 1;
        final Object[] destination = new Object[length];
        System.arraycopy(array, 1, destination, 0, length);
        return new Array<>(destination);
    }

    @Override
    public Array<E> take(int limit) {
        final Object[] destination = new Object[limit];
        System.arraycopy(array, 0, destination, 0, limit);
        return new Array<>(destination);
    }

    @Override
    public Array<E> takeRight(int limit) {
        final Object[] destination = new Object[limit];
        System.arraycopy(array, size()-limit, destination, 0, limit);
        return new Array<>(destination);
    }

    @Override
    public Array<E> subsequence(int from, int limit) {
        final int length = limit-from;
        final Object[] destination = new Object[length];
        System.arraycopy(array, from, destination, 0, length);
        return new Array<>(destination);
    }

    @Override
    public Array<E> reverse() {
        final int size = size();
        final Object[] destination = new Object[size];
        for(int i = 0; i < size; i++) {
            destination[i] = array[size-1-i];
        }
        return new Array<>(destination);
    }

    @Override
    public Array<E> append(E elem) {
        final Object[] destination = Arrays.copyOf(array, size() + 1);
        destination[size()] = elem;
        return new Array<>(destination);
    }

    @Override
    public Array<E> removeAll(E elem) {
        java.util.List<E> fastList = foldLeft(new ArrayList<>(), (xs, x) -> {
            if (!Objects.equals(elem, x)) {
                xs.add(x);
            }
            return xs;
        });
        return new Array<>(fastList.toArray());
    }

    @Override
    public boolean isEmpty() {
        return array.length == 0;
    }

    @Override
    public int size() {
        return array.length;
    }

    @Override
    public <U> Array<U> map(Function<? super E, ? extends U> mapper) {
        final int size = size();
        Object[] result = new Object[size];
        for (int i = 0; i < size; i++) {
            result[i] = mapper.apply(get(i));
        }
        return new Array<>(result);
    }

    @NotNull
    @Override
    public Iterator<E> iterator() {
        return Iterables.cast(array);
    }

    @Override
    public <A> A fold(A accumulator, Func2<A, E, A> mapper) {
        return foldLeft(accumulator, mapper);
    }

    @Override
    public <A> A foldLeft(A accumulator, Func2<A, E, A> mapper) {
        for(int i = 0; i < size(); i++) {
            accumulator = mapper.apply(accumulator, get(i));
        }
        return accumulator;
    }

    @Override
    public <A> A foldRight(A accumulator, Func2<A, E, A> mapper) {
        for (int i = size() - 1; i >= 0; i--) {
            accumulator = mapper.apply(accumulator, get(i));
        }
        return accumulator;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(array.hashCode());
    }

    @Override
    public boolean equals(Object obj) {
        return obj == this || Option.of(obj)
                                    .cast(Array.class)
                                    .map(x -> allMatchExhaustive(x, Objects::equals))
                                    .orElse(false);
    }

    @Override
    public String toString() {
        return Iterables.toString('[', ']', iterator());
    }
}
