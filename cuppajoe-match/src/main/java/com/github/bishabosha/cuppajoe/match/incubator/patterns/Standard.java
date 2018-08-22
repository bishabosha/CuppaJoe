package com.github.bishabosha.cuppajoe.match.incubator.patterns;

import com.github.bishabosha.cuppajoe.collections.immutable.tuples.Tuple2;
import com.github.bishabosha.cuppajoe.control.Option;
import com.github.bishabosha.cuppajoe.control.Option.Some;
import com.github.bishabosha.cuppajoe.higher.unapply.Unapply1;
import com.github.bishabosha.cuppajoe.higher.unapply.Unapply2;
import com.github.bishabosha.cuppajoe.match.incubator.internal.patterns.Bootstrap;
import com.github.bishabosha.cuppajoe.match.incubator.patterns.Pattern.Branch1;
import com.github.bishabosha.cuppajoe.match.incubator.patterns.Pattern.Branch2;
import com.github.bishabosha.cuppajoe.match.incubator.patterns.Pattern.Empty;

import static com.github.bishabosha.cuppajoe.API.None;

public final class Standard {

    private Standard() {}

    public static <T> Pattern<T> id() {
        return Bootstrap.id();
    }

    public static <T> Pattern<T> __() {
        return Bootstrap.__();
    }

    public static <T> Pattern<Option<T>> some(Pattern<T> value) {
        return new UnapplyBranch1<>(Some.class, value);
    }

    public static <T> Pattern<Option<T>> none() {
        return new IdentityEmpty<>(None());
    }

    public static <A, B> Pattern<Tuple2<A, B>> tuple(Pattern<A> a, Pattern<B> b) {
        return new UnapplyBranch2<>(Tuple2.class, a, b);
    }

    private static class UnapplyBranch2<T, A, B> extends Branch2<T, A, B> {
        private final Class<? extends Unapply2> type;

        private UnapplyBranch2(Class<? extends Unapply2> type, Pattern<A> branch1, Pattern<B> branch2) {
            super(branch1, branch2);
            this.type = type;
        }

        @Override
        public boolean canBranch(T value) {
            return type.isInstance(value);
        }

        @SuppressWarnings("unchecked")
        @Override
        public A extract1(T value) {
            return ((Unapply2<A, B>) value).unapply().compose((x, __) -> x);
        }

        @SuppressWarnings("unchecked")
        @Override
        public B extract2(T value) {
            return ((Unapply2<A, B>) value).unapply().compose((__, y) -> y);
        }
    }

    private static class UnapplyBranch1<T, A> extends Branch1<T, A> {
        private final Class<? extends Unapply1> type;

        private UnapplyBranch1(Class<? extends Unapply1> type, Pattern<A> branch1) {
            super(branch1);
            this.type = type;
        }

        @Override
        public boolean canBranch(T value) {
            return type.isInstance(value);
        }

        @SuppressWarnings("unchecked")
        @Override
        public A extract1(T value) {
            return ((Unapply1<A>) value).unapply();
        }
    }

    private static class IdentityEmpty<T> extends Empty<T> {
        private final T toMatch;

        private IdentityEmpty(T value) {
            toMatch = value;
        }

        @Override
        public boolean matches(T value) {
            return toMatch == value;
        }
    }
}
