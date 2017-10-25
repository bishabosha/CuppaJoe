/*
 * Copyright (c) 2017. Jamie Thompson <bishbashboshjt@gmail.com>
 */

package com.bishabosha.cuppajoe.functions;

import com.bishabosha.cuppajoe.control.Option;
import com.bishabosha.cuppajoe.control.Try;
import com.bishabosha.cuppajoe.tuples.Apply7;
import com.bishabosha.cuppajoe.tuples.Product7;
import org.jetbrains.annotations.Contract;

public interface Func7<A, B, C, D, E, F, G, R> {

    @Contract(pure = true)
    static <T,U,V,W,X,Y,Z,R> Func7<T,U,V,W,X,Y,Z,R> of(Func7<T, U, V, W, X, Y, Z, R> reference) {
        return reference;
    }

    @Contract(pure = true)
    static <T,U,V,W,X,Y,Z,R> Func7<T,U,V,W,X,Y,Z,R> narrow(Func7<? super T, ? super U, ? super V, ? super W, ? super X, ? super Y, ? super Z, ? extends R> func) {
        return func::apply;
    }

    @Contract(pure = true)
    static <T, U, V, W, X, Y, Z, R> Func7<T, U, V, W, X, Y, Z, Option<R>> lift(Func7<? super T, ? super U, ? super V, ? super W, ? super X, ? super Y, ? super Z, ? extends R> func) {
        return (t, u, v, w, x, y, z) -> Try.<R>narrow(Try.of(() -> func.apply(t, u, v, w, x, y, z))).get();
    }

    @Contract(pure = true)
    default Func1<A, Func1<B, Func1<C, Func1<D, Func1<E, Func1<F, Func1<G, R>>>>>>> curried() {
        return t -> u -> v -> w -> x -> y -> z -> apply(t, u, v, w, x, y, z);
    }

    @Contract(pure = true)
    default Apply7<A, B, C, D, E, F, G, R> applied() {
        return x -> apply(x.$1(), x.$2(), x.$3(), x.$4(), x.$5(), x.$6(), x.$7());
    }

    R apply(A a, B b, C c, D d, E e, F f, G g);

    default Func6<B, C, D, E, F, G, R> apply(A a) {
        return (b, c, d, e, f, g) -> apply(a, b, c, d, e, f, g);
    }

    default Func5<C, D, E, F, G, R> apply(A a, B b) {
        return (c, d, e, f, g) -> apply(a, b, c, d, e, f, g);
    }

    default Func4<D, E, F, G, R> apply(A a, B b, C c) {
        return (d, e, f, g) -> apply(a, b, c, d, e, f, g);
    }

    default Func3<E, F, G, R> apply(A a, B b, C c, D d) {
        return (e, f, g) -> apply(a, b, c, d, e, f, g);
    }

    default Func2<F, G, R> apply(A a, B b, C c, D d, E e) {
        return (f, g) -> apply(a, b, c, d, e, f, g);
    }

    default Func1<G, R> apply(A a, B b, C c, D d, E e, F f) {
        return g -> apply(a, b, c, d, e, f, g);
    }
}
