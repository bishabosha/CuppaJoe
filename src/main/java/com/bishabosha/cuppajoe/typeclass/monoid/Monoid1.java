package com.bishabosha.cuppajoe.typeclass.monoid;

import com.bishabosha.cuppajoe.collections.immutable.List;

public interface Monoid1<INSTANCE extends Monoid1, T> {

    Monoid1<INSTANCE, T> identity();
    Monoid1<INSTANCE, T> mappend(Monoid1<INSTANCE, ? extends T> other);

    default Monoid1<INSTANCE, T> mconcat(List<Monoid1<INSTANCE, ? extends T>> list) {
        // start from the base of the input stack and fast append each element on top of the accumulated monoid
        return list.foldRight(identity(), (mx, m) -> Type.<Monoid1<INSTANCE, T>, INSTANCE, T>narrow(m).mappend(mx));
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
