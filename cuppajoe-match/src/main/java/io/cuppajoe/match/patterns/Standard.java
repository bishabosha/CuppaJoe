package io.cuppajoe.match.patterns;

import io.cuppajoe.control.Lazy;
import io.cuppajoe.control.Option;
import io.cuppajoe.control.Option.Some;
import io.cuppajoe.control.Try;
import io.cuppajoe.control.Try.Failure;
import io.cuppajoe.control.Try.Success;
import io.cuppajoe.tuples.*;

import java.util.Objects;

import static io.cuppajoe.match.patterns.Pattern.*;

public final class Standard {

    public static <O> Pattern<O> __() {
        return x -> PASS;
    }

    public static <O> Pattern<O> $() {
        return Pattern::bind;
    }

    public static <O> Pattern<O> $(O toMatch) {
        return x -> Objects.equals(x, toMatch) ? bind(x) : FAIL;
    }

    @SafeVarargs
    public static <R> Pattern<R> $any(R... values) {
        return x -> {
            for (var val : values) {
                if (Objects.equals(x, val)) {
                    return bind(x);
                }
            }
            return FAIL;
        };
    }

    public static <O> Pattern<O> $instance(Class<? super O> clazz) {
        return x -> clazz.isInstance(x) ? bind(x) : FAIL;
    }

    public static Pattern<String> $RegEx(String regex) {
        return $RegEx(java.util.regex.Pattern.compile(regex));
    }

    public static Pattern<String> $RegEx(java.util.regex.Pattern pattern) {
        return x -> {
            var matcher = pattern.matcher(x);
            return matcher.matches() ? bind(x) : FAIL;
        };
    }

    public static final Pattern<Integer> LT = x -> x < 0 ? PASS : FAIL;
    public static final Pattern<Integer> GT = x -> x > 0 ? PASS : FAIL;
    public static final Pattern<Integer> EQ = x -> x == 0 ? PASS : FAIL;

    public static <O> Pattern<Try<O>> Success_(Pattern<O> value) {
        return PatternFactory.unapply1(Success.class, value);
    }

    public static <O> Pattern<Try<O>> Failure_(Pattern<Exception> error) {
        return PatternFactory.unapply1(Failure.class, error);
    }

    public static <O> Pattern<Lazy<O>> Lazy_(Pattern<O> value) {
        return PatternFactory.unapply1(Lazy.class, value);
    }

    public static <O> Pattern<Option<O>> Some_(Pattern<O> value) {
        return PatternFactory.unapply1(Some.class, value);
    }

    public static <O> Pattern<Option<O>> None_() {
        return PatternFactory.unapply0(Option.None.INSTANCE);
    }

    public static final Pattern<Unit> Unit_ = PatternFactory.unapply0(Unit.INSTANCE);

    public static <A> Pattern<Tuple1<A>> Tuple1_(Pattern<A> $1) {
        return PatternFactory.unapply1(Tuple1.class, $1);
    }

    public static <A, B> Pattern<Tuple2<A, B>> Tuple2$(Pattern<A> $1, Pattern<B> $2) {
        return PatternFactory.unapply2(Tuple2.class, $1, $2);
    }

    public static <A, B, C> Pattern<Tuple3<A, B, C>> Tuple3_(Pattern<A> $1, Pattern<B> $2, Pattern<C> $3) {
        return PatternFactory.unapply3(Tuple3.class, $1, $2, $3);
    }

    public static <A, B, C, D> Pattern<Tuple4<A, B, C, D>> Tuple4_(Pattern<A> $1, Pattern<B> $2, Pattern<C> $3, Pattern<D> $4) {
        return PatternFactory.unapply4(Tuple4.class, $1, $2, $3, $4);
    }

    public static <A, B, C, D, E> Pattern<Tuple5<A, B, C, D, E>> Tuple5_(Pattern<A> $1, Pattern<B> $2, Pattern<C> $3, Pattern<D> $4, Pattern<E> $5) {
        return PatternFactory.unapply5(Tuple5.class, $1, $2, $3, $4, $5);
    }

    public static <A, B, C, D, E, F> Pattern<Tuple6<A, B, C, D, E, F>> Tuple6_(Pattern<A> $1, Pattern<B> $2, Pattern<C> $3, Pattern<D> $4, Pattern<E> $5, Pattern<F> $6) {
        return PatternFactory.unapply6(Tuple6.class, $1, $2, $3, $4, $5, $6);
    }

    public static <A, B, C, D, E, F, G> Pattern<Tuple7<A, B, C, D, E, F, G>> Tuple7_(Pattern<A> $1, Pattern<B> $2, Pattern<C> $3, Pattern<D> $4, Pattern<E> $5, Pattern<F> $6, Pattern<G> $7) {
        return PatternFactory.unapply7(Tuple7.class, $1, $2, $3, $4, $5, $6, $7);
    }

    public static <A, B, C, D, E, F, G, H> Pattern<Tuple8<A, B, C, D, E, F, G, H>> Tuple8_(Pattern<A> $1, Pattern<B> $2, Pattern<C> $3, Pattern<D> $4, Pattern<E> $5, Pattern<F> $6, Pattern<G> $7, Pattern<H> $8) {
        return PatternFactory.unapply8(Tuple8.class, $1, $2, $3, $4, $5, $6, $7, $8);
    }
}
