package com.bishabosha.cuppajoe.tuples;

import com.bishabosha.cuppajoe.Iterables;
import com.bishabosha.cuppajoe.typeclass.applicative.BiApplicative;
import com.bishabosha.cuppajoe.typeclass.monad.Dyad;
import org.jetbrains.annotations.NotNull;

import java.util.Iterator;
import java.util.function.BiFunction;
import java.util.function.Function;

public interface Product2<A, B> extends Product, Unapply2<A, B>, Dyad<Product2, A, B> {

    A $1();
    B $2();

    default Product2<A, B> unapply() {
        return this;
    }

    @Override
    default int arity() {
        return 2;
    }

    @Override
    default Object $(int index) {
        switch (index) {
            case 1: return $1();
            case 2: return $2();
            default: throw new IndexOutOfBoundsException();
        }
    }

    @Override
    default  <U1, U2> Product2<U1, U2> map(Function<? super A, ? extends U1> m1, Function<? super B, ? extends U2> m2) {
        return Tuple2.of(m1.apply($1()), m2.apply($2()));
    }

    @Override
    default <U1, U2> Product2<U1, U2> flatMap(BiFunction<? super A, ? super B, Dyad<Product2, ? extends U1, ? extends U2>> mapper) {
        return (Product2<U1, U2>) mapper.apply($1(), $2());
    }

    @Override
    default <U> U compose(BiFunction<? super A, ? super B, ? extends U> mapper) {
        return mapper.apply($1(), $2());
    }

    @Override
    default <U1, U2> Product2<U1, U2> apply(BiApplicative<Product2, Function<? super A, ? extends U1>, Function<? super B, ? extends U2>> applicative) {
        Product2<Function<? super A, ? extends U1>, Function<? super B, ? extends U2>> product2 = narrowA2(applicative);
        return Tuple2.of(product2.$1().apply($1()), product2.$2().apply($2()));
    }

    static <A, B> Product2<A, B> narrowA2(BiApplicative<Product2, A, B> applicative2) {
        return (Product2<A, B>) applicative2;
    }

    static <A, B> Product2<A, B> narrow(Product2<? extends A, ? extends B> product2) {
        return (Product2<A, B>) product2;
    }

    @NotNull
    @Override
    default Iterator<Object> iterator() {
        return Iterables.ofSuppliers(this::$1, this::$2).iterator();
    }
}
