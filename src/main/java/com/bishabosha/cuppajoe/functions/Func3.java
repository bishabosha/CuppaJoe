/*
 * Copyright (c) 2017. Jamie Thompson <bishbashboshjt@gmail.com>
 */

package com.bishabosha.cuppajoe.functions;

import com.bishabosha.cuppajoe.control.Option;
import com.bishabosha.cuppajoe.control.Try;
import com.bishabosha.cuppajoe.tuples.Apply3;
import com.bishabosha.cuppajoe.tuples.Product3;
import org.jetbrains.annotations.Contract;

public interface Func3<A, B, C, R> {

    @Contract(pure = true)
    static <X,Y,Z,R> Func3<X,Y,Z,R> of(Func3<X, Y, Z, R> reference) {
        return reference;
    }

    @Contract(pure = true)
    static <X,Y,Z,R> Func3<X,Y,Z,R> narrow(Func3<? super X, ? super Y, ? super Z, ? extends R> func) {
        return func::apply;
    }

    @Contract(pure = true)
    static <X, Y, Z, R> Func3<X, Y, Z, Option<R>> lift(Func3<? super X, ? super Y, ? super Z, ? extends R> func) {
        return (x, y, z) -> Try.<R>narrow(Try.of(() -> func.apply(x, y, z))).get();
    }

    @Contract(pure = true)
    default Func1<A, Func1<B, Func1<C, R>>> curried() {
        return x -> y -> z -> apply(x, y, z);
    }

    @Contract(pure = true)
    default Func1<Product3<A, B, C>, R> tupled() {
        return x -> apply(x.$1(), x.$2(), x.$3());
    }

    @Contract(pure = true)
    default Apply3<A, B, C, R> applied() {
        return x -> tupled().apply(x);
    }

    R apply(A a, B b, C c);

    default Func2<B, C, R> apply(A a) {
        return (b, c) -> apply(a, b, c);
    }

    default Func1<C, R> apply(A a, B b) {
        return c -> apply(a, b, c);
    }
}
