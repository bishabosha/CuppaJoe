package io.cuppajoe.match.patterns;

import io.cuppajoe.control.Option;
import io.cuppajoe.match.PatternFactory;
import io.cuppajoe.match.Result;
import io.cuppajoe.tuples.*;

import java.util.Objects;

import static io.cuppajoe.API.None;
import static io.cuppajoe.API.Some;

public final class Standard {

    public static final Option<Result> PASS = Some(Result.empty());

    public static final Option<Result> FAIL = None();

    public static final Pattern<Integer> $LT = x -> x.intValue() < 0 ? PASS : FAIL;
    public static final Pattern<Integer> $GT = x -> x.intValue() > 0 ? PASS : FAIL;
    public static final Pattern<Integer> $EQ = x -> x.intValue() == 0 ? PASS : FAIL;

    public static <O> Pattern<O> $_() {
        return x -> PASS;
    }

    public static <O> Pattern<O> $x() {
        return x -> bind(x);
    }

    public static final Pattern<Unit> $Unit = PatternFactory.unapply0(Unit.INSTANCE);

    public static <A> Pattern<Tuple1<A>> $Tuple1(Pattern<A> $1) {
        return PatternFactory.unapply1(Tuple1.class, $1);
    }

    public static <A, B> Pattern<Tuple2<A, B>> $Tuple2(Pattern<A> $1, Pattern<B> $2) {
        return PatternFactory.unapply2(Tuple2.class, $1, $2);
    }

    public static <A, B, C> Pattern<Tuple3<A, B, C>> $Tuple3(Pattern<A> $1, Pattern<B> $2, Pattern<C> $3) {
        return PatternFactory.unapply3(Tuple3.class, $1, $2, $3);
    }

    public static <A, B, C, D> Pattern<Tuple4<A, B, C, D>> $Tuple4(Pattern<A> $1, Pattern<B> $2, Pattern<C> $3, Pattern<D> $4) {
        return PatternFactory.unapply4(Tuple4.class, $1, $2, $3, $4);
    }

    public static <A, B, C, D, E> Pattern<Tuple5<A, B, C, D, E>> $Tuple5(Pattern<A> $1, Pattern<B> $2, Pattern<C> $3, Pattern<D> $4, Pattern<E> $5) {
        return PatternFactory.unapply5(Tuple5.class, $1, $2, $3, $4, $5);
    }

    public static <A, B, C, D, E, F> Pattern<Tuple6<A, B, C, D, E, F>> $Tuple6(Pattern<A> $1, Pattern<B> $2, Pattern<C> $3, Pattern<D> $4, Pattern<E> $5, Pattern<F> $6) {
        return PatternFactory.unapply6(Tuple6.class, $1, $2, $3, $4, $5, $6);
    }

    public static <A, B, C, D, E, F, G> Pattern<Tuple7<A, B, C, D, E, F, G>> $Tuple7(Pattern<A> $1, Pattern<B> $2, Pattern<C> $3, Pattern<D> $4, Pattern<E> $5, Pattern<F> $6, Pattern<G> $7) {
        return PatternFactory.unapply7(Tuple7.class, $1, $2, $3, $4, $5, $6, $7);
    }

    public static <A, B, C, D, E, F, G, H> Pattern<Tuple8<A, B, C, D, E, F, G, H>> $Tuple8(Pattern<A> $1, Pattern<B> $2, Pattern<C> $3, Pattern<D> $4, Pattern<E> $5, Pattern<F> $6, Pattern<G> $7, Pattern<H> $8) {
        return PatternFactory.unapply8(Tuple8.class, $1, $2, $3, $4, $5, $6, $7, $8);
    }

    public static Option<Result> bind(Object $x) {
        return Option.of(Result.of($x));
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

    public static <O> Pattern<O> $instance(Class<? super O> clazz) {
        return x -> clazz.isInstance(x) ? bind(x) : FAIL;
    }

    public static <O> Pattern<O> $varEq(O toMatch) {
        return x -> Objects.equals(x, toMatch) ? bind(x) : FAIL;
    }

    public static <O> Pattern<O> $eq(O toMatch) {
        return x -> Objects.equals(x, toMatch) ? PASS : FAIL;
    }

    public static <R> Pattern<R> $any(R... values) {
        return x -> {
            for (var val: values) {
                if (Objects.equals(x, val)) {
                    return bind(x);
                }
            }
            return FAIL;
        };
    }
}
