/*
 * Copyright (c) 2017. Jamie Thompson <bishbashboshjt@gmail.com>
 */

package io.cuppajoe.functions;

import io.cuppajoe.control.Try;
import io.cuppajoe.tuples.Tuple4;
import io.cuppajoe.tuples.Unit;

import java.util.function.Supplier;

@FunctionalInterface
public interface Consume4<A, B, C, D> {

    void apply(A a, B b, C c, D d);

    static <W, X, Y, Z> Consume4<W, X, Y, Z> of(Consume4<W, X, Y, Z> reference) {
        return reference;
    }

    static <W, X, Y, Z> Consume4<W, X, Y, Z> narrow(Consume4<? super W, ? super X, ? super Y, ? super Z> func) {
        return func::apply;
    }

    static <W, X, Y, Z> Func4<W, X, Y, Z, Try<Unit>> lift(Consume4<? super W, ? super X, ? super Y, ? super Z> func) {
        return CheckedConsume4.lift(func::apply);
    }

    default Func1<A, Func1<B, Func1<C, Consume1<D>>>> curried() {
        return w -> x -> y -> z -> apply(w, x, y, z);
    }

    default Consume1<Tuple4<A, B, C, D>> tupled() {
        return x -> apply(x.$1, x.$2, x.$3, x.$4);
    }

    default Consume4<Supplier<A>, Supplier<B>, Supplier<C>, Supplier<D>> lazyInput() {
        return (a, b, c, d) -> apply(a.get(), b.get(), c.get(), d.get());
    }

    default Consume1<D> apply(A a, B b, C c) {
        return d -> apply(a, b, c, d);
    }

    default Consume2<C, D> apply(A a, B b) {
        return (c, d) -> apply(a, b, c, d);
    }

    default Consume3<B, C, D> apply(A a) {
        return (b, c, d) -> apply(a, b, c, d);
    }
}
