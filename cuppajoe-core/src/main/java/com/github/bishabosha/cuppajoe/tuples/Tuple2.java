package com.github.bishabosha.cuppajoe.tuples;

import com.github.bishabosha.cuppajoe.annotation.NonNull;
import com.github.bishabosha.cuppajoe.control.Option;
import com.github.bishabosha.cuppajoe.typeclass.applicative.Applicative2;
import com.github.bishabosha.cuppajoe.typeclass.compose.Compose2;
import com.github.bishabosha.cuppajoe.typeclass.monad.Monad2;
import com.github.bishabosha.cuppajoe.typeclass.peek.Peek2;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;

public final class Tuple2<A, B> implements Tuple, Unapply2<A, B>, Monad2<Tuple2, A, B>, Compose2<A, B>, Peek2<A, B> {

    public final A $1;
    public final B $2;

    Tuple2(A $1, B $2) {
        this.$1 = $1;
        this.$2 = $2;
    }

    public Tuple2<A, B> unapply() {
        return this;
    }

    @Override
    public int arity() {
        return 2;
    }

    @Override
    public Object get(int index) {
        switch (index) {
            case 1:
                return $1;
            case 2:
                return $2;
            default:
                throw new IndexOutOfBoundsException();
        }
    }

    @Override
    public <U1, U2> Tuple2<U1, U2> pure(U1 a, U2 b) {
        return Tuple.of(a, b);
    }

    @Override
    public <U1, U2> Tuple2<U1, U2> map(@NonNull Function<? super A, ? extends U1> m1, @NonNull Function<? super B, ? extends U2> m2) {
        Objects.requireNonNull(m1, "m1");
        Objects.requireNonNull(m2, "m2");
        return Tuple.of(m1.apply($1), m2.apply($2));
    }

    @Override
    public <U1, U2> Tuple2<U1, U2> flatMap(@NonNull BiFunction<? super A, ? super B, Monad2<Tuple2, ? extends U1, ? extends U2>> mapper) {
        Objects.requireNonNull(mapper, "mapper");
        return (Tuple2<U1, U2>) mapper.apply($1, $2);
    }

    @Override
    public <O> O compose(@NonNull BiFunction<? super A, ? super B, ? extends O> mapper) {
        Objects.requireNonNull(mapper, "mapper");
        return mapper.apply($1, $2);
    }

    @Override
    public void peek(@NonNull Consumer<? super A> c1, @NonNull Consumer<? super B> c2) {
        Objects.requireNonNull(c1, "c1");
        Objects.requireNonNull(c2, "c2");
        c1.accept($1);
        c2.accept($2);
    }

    @Override
    public <U1, U2> Tuple2<U1, U2> apply(@NonNull Applicative2<Tuple2, Function<? super A, ? extends U1>, Function<? super B, ? extends U2>> applicative) {
        Objects.requireNonNull(applicative, "applicative");
        return narrowA(applicative).flatMap((f1, f2) -> Tuple.of(f1.apply($1), f2.apply($2)));
    }

    static <A, B> Tuple2<A, B> narrowA(Applicative2<Tuple2, A, B> applicative2) {
        return Monad2.Type.narrowA(applicative2);
    }

    @Override
    public int hashCode() {
        return Objects.hash($1, $2);
    }

    @Override
    public boolean equals(Object obj) {
        return obj == this || Option.of(obj)
                .cast(Tuple2.class)
                .map(o -> Objects.equals($1, o.$1) && Objects.equals($2, o.$2))
                .orElse(false);
    }

    public String toString() {
        return "(" + $1 + ", " + $2 + ")";
    }
}
