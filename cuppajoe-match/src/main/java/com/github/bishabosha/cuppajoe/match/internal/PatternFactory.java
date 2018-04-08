/*
 * Copyright (c) 2017. Jamie Thompson <bishbashboshjt@gmail.com>
 */

package com.github.bishabosha.cuppajoe.match.internal;

import com.github.bishabosha.cuppajoe.annotation.NonNull;
import com.github.bishabosha.cuppajoe.control.Option;
import com.github.bishabosha.cuppajoe.higher.unapply.*;
import com.github.bishabosha.cuppajoe.match.patterns.Pattern;
import com.github.bishabosha.cuppajoe.match.patterns.Result;

import java.util.Objects;

import static com.github.bishabosha.cuppajoe.match.patterns.Pattern.FAIL;
import static com.github.bishabosha.cuppajoe.match.patterns.Pattern.PASS;

public final class PatternFactory {

    private PatternFactory() {
    }

    public static <O> @NonNull Pattern<O> unapply0(@NonNull Unapply0 target) {
        Objects.requireNonNull(target, "target");
        return x -> target.equals(x) ? PASS : FAIL;
    }

    @SuppressWarnings("unchecked")
    public static <O, A> @NonNull Pattern<O> unapply1(@NonNull Class<? extends Unapply1> target, @NonNull Pattern<A> p1) {
        Objects.requireNonNull(target, "target");
        Objects.requireNonNull(p1, "p1");
        return x -> Option.of(x)
            .filter(target::isInstance)
            .flatMap(verified -> (p1.test(((Unapply1<A>) verified).unapply())));
    }

    @SuppressWarnings("unchecked")
    public static <O, A, B> @NonNull Pattern<O> unapply2(@NonNull Class<? extends Unapply2> target, @NonNull Pattern<A> p1, @NonNull Pattern<B> p2) {
        Objects.requireNonNull(target, "target");
        Objects.requireNonNull(p1, "p1");
        Objects.requireNonNull(p2, "p2");
        return x -> Option.of(x)
            .filter(target::isInstance)
            .map(verified -> ((Unapply2<A, B>) verified).unapply())
            .flatMap(values ->
                values.compose((v1, v2) ->
                    p1.test(v1).flatMap(r1 ->
                        p2.test(v2).map(r2 ->
                            Result.compose(r1, r2)))));
    }

    @SuppressWarnings("unchecked")
    public static <O, A, B, C> @NonNull Pattern<O> unapply3(@NonNull Class<? extends Unapply3> target, @NonNull Pattern<A> p1, @NonNull Pattern<B> p2, @NonNull Pattern<C> p3) {
        Objects.requireNonNull(target, "target");
        Objects.requireNonNull(p1, "p1");
        Objects.requireNonNull(p2, "p2");
        Objects.requireNonNull(p3, "p3");
        return x -> Option.of(x)
            .filter(target::isInstance)
            .map(verified -> ((Unapply3<A, B, C>) verified).unapply())
            .flatMap(values ->
                values.compose((v1, v2, v3) ->
                    p1.test(v1).flatMap(r1 ->
                        p2.test(v2).flatMap(r2 ->
                            p3.test(v3).map(r3 ->
                                Result.compose(r1, r2, r3))))));
    }

    @SuppressWarnings("unchecked")
    public static <O, A, B, C, D> @NonNull Pattern<O> unapply4(@NonNull Class<? extends Unapply4> target, @NonNull Pattern<A> p1, @NonNull Pattern<B> p2, @NonNull Pattern<C> p3, @NonNull Pattern<D> p4) {
        Objects.requireNonNull(target, "target");
        Objects.requireNonNull(p1, "p1");
        Objects.requireNonNull(p2, "p2");
        Objects.requireNonNull(p3, "p3");
        Objects.requireNonNull(p4, "p4");
        return x -> Option.of(x)
            .filter(target::isInstance)
            .map(verified -> ((Unapply4<A, B, C, D>) verified).unapply())
            .flatMap(values ->
                values.compose((v1, v2, v3, v4) ->
                    p1.test(v1).flatMap(r1 ->
                        p2.test(v2).flatMap(r2 ->
                            p3.test(v3).flatMap(r3 ->
                                p4.test(v4).map(r4 ->
                                    Result.compose(r1, r2, r3, r4)))))));
    }

    @SuppressWarnings("unchecked")
    public static <O, A, B, C, D, E> @NonNull Pattern<O> unapply5(@NonNull Class<? extends Unapply5> target, @NonNull Pattern<A> p1, @NonNull Pattern<B> p2, @NonNull Pattern<C> p3, @NonNull Pattern<D> p4, @NonNull Pattern<E> p5) {
        Objects.requireNonNull(target, "target");
        Objects.requireNonNull(p1, "p1");
        Objects.requireNonNull(p2, "p2");
        Objects.requireNonNull(p3, "p3");
        Objects.requireNonNull(p4, "p4");
        Objects.requireNonNull(p5, "p5");
        return x -> Option.of(x)
            .filter(target::isInstance)
            .map(verified -> ((Unapply5<A, B, C, D, E>) verified).unapply())
            .flatMap(values ->
                values.compose((v1, v2, v3, v4, v5) ->
                    p1.test(v1).flatMap(r1 ->
                        p2.test(v2).flatMap(r2 ->
                            p3.test(v3).flatMap(r3 ->
                                p4.test(v4).flatMap(r4 ->
                                    p5.test(v5).map(r5 ->
                                        Result.compose(r1, r2, r3, r4, r5))))))));
    }

    @SuppressWarnings("unchecked")
    public static <O, A, B, C, D, E, F> @NonNull Pattern<O> unapply6(@NonNull Class<? extends Unapply6> target, @NonNull Pattern<A> p1, @NonNull Pattern<B> p2, @NonNull Pattern<C> p3, @NonNull Pattern<D> p4, @NonNull Pattern<E> p5, @NonNull Pattern<F> p6) {
        Objects.requireNonNull(target, "target");
        Objects.requireNonNull(p1, "p1");
        Objects.requireNonNull(p2, "p2");
        Objects.requireNonNull(p3, "p3");
        Objects.requireNonNull(p4, "p4");
        Objects.requireNonNull(p5, "p5");
        Objects.requireNonNull(p6, "p6");
        return x -> Option.of(x)
            .filter(target::isInstance)
            .map(verified -> ((Unapply6<A, B, C, D, E, F>) verified).unapply())
            .flatMap(values ->
                values.compose((v1, v2, v3, v4, v5, v6) ->
                    p1.test(v1).flatMap(r1 ->
                        p2.test(v2).flatMap(r2 ->
                            p3.test(v3).flatMap(r3 ->
                                p4.test(v4).flatMap(r4 ->
                                    p5.test(v5).flatMap(r5 ->
                                        p6.test(v6).map(r6 ->
                                            Result.compose(r1, r2, r3, r4, r5, r6)))))))));
    }

    @SuppressWarnings("unchecked")
    public static <O, A, B, C, D, E, F, G> @NonNull Pattern<O> unapply7(@NonNull Class<? extends Unapply7> target, @NonNull Pattern<A> p1, @NonNull Pattern<B> p2, @NonNull Pattern<C> p3, @NonNull Pattern<D> p4, @NonNull Pattern<E> p5, @NonNull Pattern<F> p6, @NonNull Pattern<G> p7) {
        Objects.requireNonNull(target, "target");
        Objects.requireNonNull(p1, "p1");
        Objects.requireNonNull(p2, "p2");
        Objects.requireNonNull(p3, "p3");
        Objects.requireNonNull(p4, "p4");
        Objects.requireNonNull(p5, "p5");
        Objects.requireNonNull(p6, "p6");
        Objects.requireNonNull(p7, "p7");
        return x -> Option.of(x)
            .filter(target::isInstance)
            .map(verified -> ((Unapply7<A, B, C, D, E, F, G>) verified).unapply())
            .flatMap(values ->
                values.compose((v1, v2, v3, v4, v5, v6, v7) ->
                    p1.test(v1).flatMap(r1 ->
                        p2.test(v2).flatMap(r2 ->
                            p3.test(v3).flatMap(r3 ->
                                p4.test(v4).flatMap(r4 ->
                                    p5.test(v5).flatMap(r5 ->
                                        p6.test(v6).flatMap(r6 ->
                                            p7.test(v7).map(r7 ->
                                                Result.compose(r1, r2, r3, r4, r5, r6, r7))))))))));
    }

    @SuppressWarnings("unchecked")
    public static <O, A, B, C, D, E, F, G, H> @NonNull Pattern<O> unapply8(@NonNull Class<? extends Unapply8> target, @NonNull Pattern<A> p1, @NonNull Pattern<B> p2, @NonNull Pattern<C> p3, @NonNull Pattern<D> p4, @NonNull Pattern<E> p5, @NonNull Pattern<F> p6, @NonNull Pattern<G> p7, @NonNull Pattern<H> p8) {
        Objects.requireNonNull(target, "target");
        Objects.requireNonNull(p1, "p1");
        Objects.requireNonNull(p2, "p2");
        Objects.requireNonNull(p3, "p3");
        Objects.requireNonNull(p4, "p4");
        Objects.requireNonNull(p5, "p5");
        Objects.requireNonNull(p6, "p6");
        Objects.requireNonNull(p7, "p7");
        Objects.requireNonNull(p8, "p8");
        return x -> Option.of(x)
            .filter(target::isInstance)
            .map(verified -> ((Unapply8<A, B, C, D, E, F, G, H>) verified).unapply())
            .flatMap(values ->
                values.compose((v1, v2, v3, v4, v5, v6, v7, v8) ->
                    p1.test(v1).flatMap(r1 ->
                        p2.test(v2).flatMap(r2 ->
                            p3.test(v3).flatMap(r3 ->
                                p4.test(v4).flatMap(r4 ->
                                    p5.test(v5).flatMap(r5 ->
                                        p6.test(v6).flatMap(r6 ->
                                            p7.test(v7).flatMap(r7 ->
                                                p8.test(v8).map(r8 ->
                                                    Result.compose(r1, r2, r3, r4, r5, r6, r7, r8)))))))))));
    }
}
