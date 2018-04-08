package com.github.bishabosha.cuppajoe.higher.monoid;

import com.github.bishabosha.cuppajoe.annotation.NonNull;
import com.github.bishabosha.cuppajoe.higher.foldable.Foldable;

import java.util.Objects;

public interface Monoid1<INSTANCE extends Monoid1, T> {

    Monoid1<INSTANCE, T> mempty();

    Monoid1<INSTANCE, T> mappend(@NonNull Monoid1<INSTANCE, ? extends T> other);

    default Monoid1<INSTANCE, T> mconcat(@NonNull Foldable<Monoid1<INSTANCE, ? extends T>> list) {
        // start from the base of the input stack and fast append each element on top of the accumulated monoid
        Objects.requireNonNull(list, "list");
        return list.foldRight(mempty(), (mx, m) -> Type.<Monoid1<INSTANCE, T>, INSTANCE, T>narrow(m).mappend(mx));
    }

    interface Type {
        @SuppressWarnings("unchecked")
        static <OUT extends Monoid1<INSTANCE, U>, INSTANCE extends Monoid1, U> OUT narrow(Monoid1<INSTANCE, ? extends U> higher) {
            return (OUT) higher;
        }

        @SuppressWarnings("unchecked")
        static <OUT extends Monoid1<INSTANCE, U>, INSTANCE extends Monoid1, U> OUT castParam(Monoid1<INSTANCE, ?> higher) {
            return (OUT) higher;
        }
    }
}
