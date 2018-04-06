/*
 * Copyright (c) 2017. Jamie Thompson <bishbashboshjt@gmail.com>
 */

package io.cuppajoe.functions;

import io.cuppajoe.annotation.NonNull;
import io.cuppajoe.control.Try;
import io.cuppajoe.tuples.Tuple7;
import io.cuppajoe.tuples.Unit;

import java.util.Objects;
import java.util.function.Supplier;

@FunctionalInterface
public interface Consume7<A, B, C, D, E, F, G> {

    void apply(A a, B b, C c, D d, E e, F f, G g);

    static <T, U, V, W, X, Y, Z> Consume7<T, U, V, W, X, Y, Z> of(@NonNull Consume7<T, U, V, W, X, Y, Z> reference) {
        return Objects.requireNonNull(reference);
    }

    static <T, U, V, W, X, Y, Z> Consume7<T, U, V, W, X, Y, Z> narrow(@NonNull Consume7<? super T, ? super U, ? super V, ? super W, ? super X, ? super Y, ? super Z> func) {
        Objects.requireNonNull(func);
        return func::apply;
    }

    static <T, U, V, W, X, Y, Z> Func7<T, U, V, W, X, Y, Z, Try<Unit>> lift(@NonNull Consume7<? super T, ? super U, ? super V, ? super W, ? super X, ? super Y, ? super Z> func) {
        Objects.requireNonNull(func);
        return CheckedConsume7.lift(func::apply);
    }

    default Func1<A, Func1<B, Func1<C, Func1<D, Func1<E, Func1<F, Consume1<G>>>>>>> curried() {
        return t -> u -> v -> w -> x -> y -> z -> apply(t, u, v, w, x, y, z);
    }

    default Consume1<Tuple7<A, B, C, D, E, F, G>> tupled() {
        return x -> apply(x.$1, x.$2, x.$3, x.$4, x.$5, x.$6, x.$7);
    }

    default Consume7<Supplier<A>, Supplier<B>, Supplier<C>, Supplier<D>, Supplier<E>, Supplier<F>, Supplier<G>> lazyInput() {
        return (a, b, c, d, e, f, g) -> apply(a.get(), b.get(), c.get(), d.get(), e.get(), f.get(), g.get());
    }

    default Consume1<G> apply(A a, B b, C c, D d, E e, F f) {
        return g -> apply(a, b, c, d, e, f, g);
    }

    default Consume2<F, G> apply(A a, B b, C c, D d, E e) {
        return (f, g) -> apply(a, b, c, d, e, f, g);
    }

    default Consume3<E, F, G> apply(A a, B b, C c, D d) {
        return (e, f, g) -> apply(a, b, c, d, e, f, g);
    }

    default Consume4<D, E, F, G> apply(A a, B b, C c) {
        return (d, e, f, g) -> apply(a, b, c, d, e, f, g);
    }

    default Consume5<C, D, E, F, G> apply(A a, B b) {
        return (c, d, e, f, g) -> apply(a, b, c, d, e, f, g);
    }

    default Consume6<B, C, D, E, F, G> apply(A a) {
        return (b, c, d, e, f, g) -> apply(a, b, c, d, e, f, g);
    }
}
