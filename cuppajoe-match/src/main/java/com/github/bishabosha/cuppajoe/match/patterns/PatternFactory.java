/*
 * Copyright (c) 2017. Jamie Thompson <bishbashboshjt@gmail.com>
 */

package com.github.bishabosha.cuppajoe.match.patterns;

import com.github.bishabosha.cuppajoe.control.Option;
import com.github.bishabosha.cuppajoe.tuples.Unapply0;
import com.github.bishabosha.cuppajoe.tuples.Unapply1;
import com.github.bishabosha.cuppajoe.tuples.Unapply2;
import com.github.bishabosha.cuppajoe.tuples.Unapply3;
import com.github.bishabosha.cuppajoe.tuples.Unapply4;
import com.github.bishabosha.cuppajoe.tuples.Unapply5;
import com.github.bishabosha.cuppajoe.tuples.Unapply6;
import com.github.bishabosha.cuppajoe.tuples.Unapply7;
import com.github.bishabosha.cuppajoe.tuples.Unapply8;

import static com.github.bishabosha.cuppajoe.match.patterns.Pattern.FAIL;
import static com.github.bishabosha.cuppajoe.match.patterns.Pattern.PASS;

public class PatternFactory {

    public static <O> Pattern<O> unapply0(Unapply0 target) {
        return x -> target.equals(x) ? PASS : FAIL;
    }

    @SuppressWarnings("unchecked")
    public static <O, A> Pattern<O> unapply1(Class<? extends Unapply1> target, Pattern<A> p1) {
        return x -> Option.of(x)
            .filter(target::isInstance)
            .map(verified -> ((Unapply1<A>) verified).unapply())
            .flatMap(v1 -> p1.test(v1));
    }

    @SuppressWarnings("unchecked")
    public static <O, A, B> Pattern<O> unapply2(Class<? extends Unapply2> target, Pattern<A> p1, Pattern<B> p2) {
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
    public static <O, A, B, C> Pattern<O> unapply3(Class<? extends Unapply3> target, Pattern<A> p1, Pattern<B> p2, Pattern<C> p3) {
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
    public static <O, A, B, C, D> Pattern<O> unapply4(Class<? extends Unapply4> target, Pattern<A> p1, Pattern<B> p2, Pattern<C> p3, Pattern<D> p4) {
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
    public static <O, A, B, C, D, E> Pattern<O> unapply5(Class<? extends Unapply5> target, Pattern<A> p1, Pattern<B> p2, Pattern<C> p3, Pattern<D> p4, Pattern<E> p5) {
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
    public static <O, A, B, C, D, E, F> Pattern<O> unapply6(Class<? extends Unapply6> target, Pattern<A> p1, Pattern<B> p2, Pattern<C> p3, Pattern<D> p4, Pattern<E> p5, Pattern<F> p6) {
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
    public static <O, A, B, C, D, E, F, G> Pattern<O> unapply7(Class<? extends Unapply7> target, Pattern<A> p1, Pattern<B> p2, Pattern<C> p3, Pattern<D> p4, Pattern<E> p5, Pattern<F> p6, Pattern<G> p7) {
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
    public static <O, A, B, C, D, E, F, G, H> Pattern<O> unapply8(Class<? extends Unapply8> target, Pattern<A> p1, Pattern<B> p2, Pattern<C> p3, Pattern<D> p4, Pattern<E> p5, Pattern<F> p6, Pattern<G> p7, Pattern<H> p8) {
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
