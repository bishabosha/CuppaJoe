/*
 * Copyright (c) 2017. Jamie Thompson <bishbashboshjt@gmail.com>
 */

package com.github.bishabosha.cuppajoe.match.incubator;

import com.github.bishabosha.cuppajoe.annotation.NonNull;
import com.github.bishabosha.cuppajoe.control.Option;
import com.github.bishabosha.cuppajoe.higher.unapply.Unapply0;
import com.github.bishabosha.cuppajoe.higher.unapply.Unapply1;
import com.github.bishabosha.cuppajoe.higher.unapply.Unapply2;

import java.util.Objects;

import static com.github.bishabosha.cuppajoe.match.incubator.Pattern.FAIL;
import static com.github.bishabosha.cuppajoe.match.incubator.Pattern.PASS;

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
                            MatchResult.compose(r1, r2)))));
    }
}
