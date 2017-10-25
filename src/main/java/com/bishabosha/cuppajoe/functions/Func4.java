/*
 * Copyright (c) 2017. Jamie Thompson <bishbashboshjt@gmail.com>
 */
package com.bishabosha.cuppajoe.functions;

import com.bishabosha.cuppajoe.control.Option;
import com.bishabosha.cuppajoe.control.Try;
import com.bishabosha.cuppajoe.tuples.Apply4;
import com.bishabosha.cuppajoe.tuples.Product4;
import org.jetbrains.annotations.Contract;

public interface Func4<A, B, C, D, R> {

    @Contract(pure = true)
    static <W,X,Y,Z,R> Func4<W,X,Y,Z,R> of(Func4<W, X, Y, Z, R> reference) {
        return reference;
    }

    @Contract(pure = true)
    static <W,X,Y,Z,R> Func4<W,X,Y,Z,R> narrow(Func4<? super W, ? super X, ? super Y, ? super Z, ? extends R> func) {
        return func::apply;
    }

    @Contract(pure = true)
    static <W, X, Y, Z, R> Func4<W, X, Y, Z, Option<R>> lift(Func4<? super W, ? super X, ? super Y, ? super Z, ? extends R> func) {
        return (w, x, y, z) -> Try.<R>narrow(Try.of(() -> func.apply(w, x, y, z))).get();
    }

    @Contract(pure = true)
    default Func1<A, Func1<B, Func1<C, Func1<D, R>>>> curried() {
        return w -> x -> y -> z -> apply(w, x, y, z);
    }

    @Contract(pure = true)
    default Apply4<A, B, C, D, R> applied() {
        return x -> apply(x.$1(), x.$2(), x.$3(), x.$4());
    }

    R apply(A a, B b, C c, D d);

    default Func3<B, C, D, R> apply(A a) {
        return (b, c, d) -> apply(a, b, c, d);
    }

    default Func2<C, D, R> apply(A a, B b) {
        return (c, d) -> apply(a, b, c, d);
    }

    default Func1<D, R> apply(A a, B b, C c) {
        return d -> apply(a, b, c, d);
    }
}
