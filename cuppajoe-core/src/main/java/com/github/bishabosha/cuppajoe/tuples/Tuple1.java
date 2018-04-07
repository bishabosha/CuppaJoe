package com.github.bishabosha.cuppajoe.tuples;

import com.github.bishabosha.cuppajoe.annotation.NonNull;
import com.github.bishabosha.cuppajoe.functions.Func1;
import com.github.bishabosha.cuppajoe.typeclass.compose.Compose1;

import java.util.Objects;
import java.util.function.Function;

public final class Tuple1<A> implements Tuple, Unapply1<A>, Compose1<A> {

    public final A $1;

    Tuple1(A $1) {
        this.$1 = $1;
    }

    public A unapply() {
        return $1;
    }

    @Override
    public int arity() {
        return 1;
    }

    @Override
    public Object get(int index) {
        if (index == 1) {
            return $1;
        } else {
            throw new IndexOutOfBoundsException();
        }
    }

    @Override
    public <O> O compose(@NonNull Function<? super A, ? extends O> mapper) {
        Objects.requireNonNull(mapper, "mapper");
        return Func1.<A, O>narrow(mapper).tupled().apply(this);
    }

    public <O> Tuple1<O> map(@NonNull Function<? super A, ? extends O> mapper) {
        Objects.requireNonNull(mapper, "mapper");
        return Tuple.of(mapper.apply($1));
    }

    public <AA> Tuple1<AA> flatMap(@NonNull Func1<A, Tuple1<AA>> mapper) {
        Objects.requireNonNull(mapper, "mapper");
        return mapper.apply($1);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode($1);
    }

    @Override
    public boolean equals(Object obj) {
        return obj == this || obj instanceof Tuple1 && Objects.equals($1, ((Tuple1) obj).$1);
    }

    public String toString() {
        return "(" + $1 + ")";
    }
}
