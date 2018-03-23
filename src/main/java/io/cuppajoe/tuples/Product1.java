package io.cuppajoe.tuples;

import io.cuppajoe.Iterables;
import io.cuppajoe.functions.Func1;
import org.jetbrains.annotations.NotNull;

import java.util.Iterator;
import java.util.function.Function;
import java.util.function.Supplier;

public interface Product1<A> extends Product, Unapply1<A> {

    A $1();

    default Product1<A> unapply() {
        return this;
    }

    @Override
    default int arity() {
        return 1;
    }

    @Override
    default Object $(int index) {
        switch (index) {
            case 1: return $1();
            default: throw new IndexOutOfBoundsException();
        }
    }

    @NotNull
    @Override
    default Iterator<Object> iterator() {
        return Iterables.ofSuppliers((Supplier<Object>) this::$1).iterator();
    }

    default <O> O compose(Func1<A, O> mapper) {
        return mapper.apply($1());
    }

    default <O> Product1<O> map(Function<? super A, ? extends O> function) {
        return Tuple1.of(function.apply($1()));
    }
}
