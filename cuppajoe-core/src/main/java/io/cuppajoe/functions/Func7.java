/*
 * Copyright (c) 2017. Jamie Thompson <bishbashboshjt@gmail.com>
 */

package io.cuppajoe.functions;

import io.cuppajoe.annotation.NonNull;
import io.cuppajoe.control.Try;
import io.cuppajoe.tuples.Tuple7;

import java.util.Objects;
import java.util.function.Function;
import java.util.function.Supplier;

@FunctionalInterface
public interface Func7<A, B, C, D, E, F, G, R> {

    static <T, U, V, W, X, Y, Z, R> Func7<T, U, V, W, X, Y, Z, R> of(@NonNull Func7<T, U, V, W, X, Y, Z, R> reference) {
        return Objects.requireNonNull(reference);
    }

    static <T, U, V, W, X, Y, Z, R> Func7<T, U, V, W, X, Y, Z, R> narrow(@NonNull Func7<? super T, ? super U, ? super V, ? super W, ? super X, ? super Y, ? super Z, ? extends R> func) {
        Objects.requireNonNull(func);
        return func::apply;
    }

    static <T, U, V, W, X, Y, Z, R> Func7<T, U, V, W, X, Y, Z, Try<R>> lift(@NonNull Func7<? super T, ? super U, ? super V, ? super W, ? super X, ? super Y, ? super Z, ? extends R> func) {
        Objects.requireNonNull(func);
        return CheckedFunc7.lift(func::apply);
    }

    default Func1<A, Func1<B, Func1<C, Func1<D, Func1<E, Func1<F, Func1<G, R>>>>>>> curried() {
        return t -> u -> v -> w -> x -> y -> z -> apply(t, u, v, w, x, y, z);
    }

    default Func1<Tuple7<A, B, C, D, E, F, G>, R> tupled() {
        return x -> apply(x.$1, x.$2, x.$3, x.$4, x.$5, x.$6, x.$7);
    }

    default <U> Func7<A, B, C, D, E, F, G, U> andThen(@NonNull Function<? super R, ? extends U> next) {
        Objects.requireNonNull(next);
        return (s, t, u, v, w, x, y) -> next.apply(apply(s, t, u, v, w, x, y));
    }

    default Func7<Supplier<A>, Supplier<B>, Supplier<C>, Supplier<D>, Supplier<E>, Supplier<F>, Supplier<G>, R> lazyInput() {
        return (a, b, c, d, e, f, g) -> apply(a.get(), b.get(), c.get(), d.get(), e.get(), f.get(), g.get());
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
