package io.cuppajoe.tuples;

import io.cuppajoe.Iterators;
import io.cuppajoe.functions.Func1;
import io.cuppajoe.typeclass.compose.Compose1;
import org.jetbrains.annotations.NotNull;

import java.util.Iterator;
import java.util.function.Function;
import java.util.function.Supplier;

public interface Product1<A> extends Product, Unapply1<A>, Compose1<A> {

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
        return Iterators.ofSuppliers((Supplier<Object>) this::$1);
    }

    @Override
    default <O> O compose(Function<? super A, ? extends O> mapper) {
        return Func1.narrow(mapper).tupled().apply(this::$1);
    }

    default <O> Product1<O> map(Function<? super A, ? extends O> function) {
        return Product.of(function.apply($1()));
    }
}
