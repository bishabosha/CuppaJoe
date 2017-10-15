package com.bishabosha.cuppajoe.collections.immutable;

import com.bishabosha.cuppajoe.Iterables;
import com.bishabosha.cuppajoe.control.Option;
import com.bishabosha.cuppajoe.functions.Func2;
import com.bishabosha.cuppajoe.tuples.Product2;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Objects;
import java.util.function.Function;

public class Array<E> implements Seq<E>{
    private Object[] array;

    private Array(Object[] array) {
        this.array = array;
    }

    public static <O> Array<O> of(O... elems) {
        return new Array<>(elems);
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
        final int length = limit-from+1;
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
    public <R> Array<R> map(Function<? super E, ? extends R> mapper) {
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
}
