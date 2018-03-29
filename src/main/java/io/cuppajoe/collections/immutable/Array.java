package io.cuppajoe.collections.immutable;

import io.cuppajoe.Iterables;
import io.cuppajoe.control.Option;
import io.cuppajoe.functions.*;
import io.cuppajoe.math.PredicateFor;
import io.cuppajoe.tuples.Product2;
import io.cuppajoe.typeclass.applicative.Applicative1;
import io.cuppajoe.typeclass.monad.Monad1;
import io.cuppajoe.typeclass.monoid.Monoid1;
import io.cuppajoe.typeclass.value.Value1;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;

import static io.cuppajoe.API.*;

public class Array<E> implements Seq<Array, E>, Value1<Array, E> {
    private Object[] array;

    private Array(Object[] array) {
        this.array = array;
    }

    public static <O> Array<O> of(O... elems) {
        return new Array<>(elems == null ? new Object[]{null} : elems);
    }

    public static <R> Array<R> empty() {
        return new Array<>(new Object[0]);
    }

    @Override
    public Array<E> or(Supplier<? extends Value1<Array, ? extends E>> alternative) {
        return isEmpty() ? Value1.Type.narrow(alternative.get()) : this;
    }

    @Override
    public Array<E> push(E elem) {
        var newArr = new Object[size() + 1];
        newArr[0] = elem;
        System.arraycopy(array, 0, newArr, 1, size());
        return new Array<>(newArr);
    }

    @Override
    public Option<? extends Product2<E, ? extends Seq<Array, E>>> pop() {
        return isEmpty() ? Nothing() : Some(Tuple(get(0), takeRight(size() - 1)));
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
        var length = size() - 1;
        var newArr = new Object[length];
        System.arraycopy(array, 1, newArr, 0, length);
        return new Array<>(newArr);
    }

    @Override
    public Array<E> take(int limit) {
        var newArr = new Object[limit];
        System.arraycopy(array, 0, newArr, 0, limit);
        return new Array<>(newArr);
    }

    @Override
    public Array<E> takeRight(int limit) {
        var newArr = new Object[limit];
        System.arraycopy(array, size()-limit, newArr, 0, limit);
        return new Array<>(newArr);
    }

    @Override
    public Array<E> subsequence(int from, int limit) {
        var length = limit-from;
        var newArr = new Object[length];
        System.arraycopy(array, from, newArr, 0, length);
        return new Array<>(newArr);
    }

    @Override
    public Array<E> reverse() {
        var size = size();
        var newArr = new Object[size];
        for(var i = 0; i < size; i++) {
            newArr[i] = array[size-1-i];
        }
        return new Array<>(newArr);
    }

    @Override
    public <O> Array<E> distinct(Function<E, O> propertyGetter) {
        var isDistinct = PredicateFor.distinctProperty(propertyGetter);
        var distinctElems = foldLeft(new ArrayList<>(), (xs, x) -> {
            if (isDistinct.test(x)) {
                xs.add(x);
                return xs;
            }
            return xs;
        });
        return new Array<>(distinctElems.toArray());
    }

    @Override
    public Array<E> append(E elem) {
        var newArr = Arrays.copyOf(array, size() + 1);
        newArr[size()] = elem;
        return new Array<>(newArr);
    }

    @Override
    public Array<E> removeAll(E elem) {
        var fastList = foldLeft(new ArrayList<>(), (xs, x) -> {
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
        var size = size();
        var newArr = new Object[size];
        for (var i = 0; i < size; i++) {
            newArr[i] = mapper.apply(get(i));
        }
        return new Array<>(newArr);
    }

    @Override
    public <U> Array<U> flatMap(Function<? super E, Monad1<Array, ? extends U>> mapper) {
        var resultArr = new Object[0];
        for (var e: this) {
            var computed = Monad1.Type.<Array<U>, Array, U>narrow(mapper.apply(e));
            var bufferArr = new Object[resultArr.length + computed.size()];
            System.arraycopy(resultArr, 0, bufferArr, 0, resultArr.length);
            System.arraycopy(computed.array, 0, bufferArr, resultArr.length, computed.size());
            resultArr = bufferArr;
        }
        return new Array<>(resultArr);
    }

    @NotNull
    @Override
    public Iterator<E> iterator() {
        return Iterables.cast(array);
    }

    @Override
    public <A> A foldLeft(A accumulator, BiFunction<A, E, A> mapper) {
        for(var i = 0; i < size(); i++) {
            accumulator = mapper.apply(accumulator, get(i));
        }
        return accumulator;
    }

    @Override
    public <A> A foldRight(A accumulator, BiFunction<A, E, A> mapper) {
        for (var i = size() - 1; i >= 0; i--) {
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

    @Override
    public <U> Array<U> pure(U value) {
        return of(value);
    }

    @Override
    public <U> Array<U> apply(Applicative1<Array, Function<? super E, ? extends U>> applicative1) {
        return Monad1.applyImpl(this, applicative1);
    }

    @Override
    public Array<E> mempty() {
        return empty();
    }

    @Override
    public Array<E> mappend(Monoid1<Array, ? extends E> other) {
        var otherArr = Monoid1.Type.<Array<E>, Array, E>narrow(other);
        var newArr = new Object[otherArr.size() + size()];
        System.arraycopy(array, 0, newArr, 0, size());
        System.arraycopy(otherArr.array, 0, newArr, size(), otherArr.size());
        return new Array<>(newArr);
    }

    @Override
    public Array<E> mconcat(List<Monoid1<Array, ? extends E>> list) {
        return Monoid1.Type.narrow(Seq.super.mconcat(list));
    }
}
