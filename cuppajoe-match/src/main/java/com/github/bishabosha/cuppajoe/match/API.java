package com.github.bishabosha.cuppajoe.match;

import com.github.bishabosha.cuppajoe.annotation.NonNull;
import com.github.bishabosha.cuppajoe.control.Option;
import com.github.bishabosha.cuppajoe.functions.*;
import com.github.bishabosha.cuppajoe.match.internal.CaseFactory;
import com.github.bishabosha.cuppajoe.match.internal.GuardFactory;
import com.github.bishabosha.cuppajoe.match.internal.PatternFactory;
import com.github.bishabosha.cuppajoe.match.patterns.Pattern;
import com.github.bishabosha.cuppajoe.tuples.*;

import java.util.function.BooleanSupplier;
import java.util.function.Supplier;

public final class API {

    private API () {
    }

    public static <I>
    Matcher<I>
    Match(I toMatch) {
        return Matcher.create(toMatch);
    }

    @SafeVarargs
    public static <O>
    O
    IfUnsafe(@NonNull Guard<O>... guards) {
        return Guards(guards).get();
    }

    @SafeVarargs
    public static <O>
    Option<O>
    If(@NonNull Guard<O>... guards) {
        return Guards(guards).match();
    }

    @SafeVarargs
    public static <O>
    Guard<O>
    Guards(@NonNull Guard<O>... guards) {
        return GuardFactory.combine(guards);
    }

    public static <O>
    Guard<O>
    When(@NonNull BooleanSupplier test, @NonNull Func0<O> valueSupplier) {
        return GuardFactory.when(test, valueSupplier);
    }

    public static <O>
    Guard<O>
    Edge(@NonNull Supplier<O> valueSupplier) {
        return GuardFactory.edge(valueSupplier);
    }

    @SafeVarargs
    public static <I, O>
    Case<I, O>
    Cases(@NonNull Case<I, O>... cases) {
        return CaseFactory.combine(cases);
    }

    public static <I, O>
    Case<I, O>
    With(@NonNull Pattern<I> matcher, @NonNull Supplier<O> binder) {
        return CaseFactory.with(matcher, binder);
    }

    public static <I, O, A>
    Case<I, O>
    With(@NonNull Pattern<I> matcher, @NonNull Func1<A, O> binder) {
        return CaseFactory.with(matcher, binder);
    }

    public static <I, O, A, B>
    Case<I, O>
    With(@NonNull Pattern<I> matcher, @NonNull Func2<A, B, O> binder) {
        return CaseFactory.with(matcher, binder);
    }

    public static <I, O, A, B, C>
    Case<I, O>
    With(@NonNull Pattern<I> matcher, @NonNull Func3<A, B, C, O> binder) {
        return CaseFactory.with(matcher, binder);
    }

    public static <I, O, A, B, C, D>
    Case<I, O>
    With(@NonNull Pattern<I> matcher, @NonNull Func4<A, B, C, D, O> binder) {
        return CaseFactory.with(matcher, binder);
    }

    public static <I, O, A, B, C, D, E>
    Case<I, O>
    With(@NonNull Pattern<I> matcher, @NonNull Func5<A, B, C, D, E, O> binder) {
        return CaseFactory.with(matcher, binder);
    }

    public static <I, O, A, B, C, D, E, F>
    Case<I, O>
    With(@NonNull Pattern<I> matcher, @NonNull Func6<A, B, C, D, E, F, O> binder) {
        return CaseFactory.with(matcher, binder);
    }

    public static <I, O, A, B, C, D, E, F, G>
    Case<I, O>
    With(@NonNull Pattern<I> matcher, @NonNull Func7<A, B, C, D, E, F, G, O> binder) {
        return CaseFactory.with(matcher, binder);
    }

    public static <I, O, A, B, C, D, E, F, G, H>
    Case<I, O>
    With(@NonNull Pattern<I> matcher, @NonNull Func8<A, B, C, D, E, F, G, H, O> binder) {
        return CaseFactory.with(matcher, binder);
    }

    public static <O>
    Pattern<O>
    PatternFor(@NonNull Unapply0 target) {
        return PatternFactory.unapply0(target);
    }

    public static <O, A>
    Pattern<O>
    PatternFor(@NonNull Class<? extends Unapply1> target, @NonNull Pattern<A> p1) {
        return PatternFactory.unapply1(target, p1);
    }

    public static <O, A, B>
    Pattern<O>
    PatternFor(@NonNull Class<? extends Unapply2> target, @NonNull Pattern<A> p1, @NonNull Pattern<B> p2) {
        return PatternFactory.unapply2(target, p1, p2);
    }

    public static <O, A, B, C>
    Pattern<O>
    PatternFor(@NonNull Class<? extends Unapply3> target, @NonNull Pattern<A> p1, @NonNull Pattern<B> p2, @NonNull Pattern<C> p3) {
        return PatternFactory.unapply3(target, p1, p2, p3);
    }

    public static <O, A, B, C, D>
    Pattern<O>
    PatternFor(@NonNull Class<? extends Unapply4> target, @NonNull Pattern<A> p1, @NonNull Pattern<B> p2, @NonNull Pattern<C> p3, @NonNull Pattern<D> p4) {
        return PatternFactory.unapply4(target, p1, p2, p3, p4);
    }

    public static <O, A, B, C, D, E>
    Pattern<O>
    PatternFor(@NonNull Class<? extends Unapply5> target, @NonNull Pattern<A> p1, @NonNull Pattern<B> p2, @NonNull Pattern<C> p3, @NonNull Pattern<D> p4, @NonNull Pattern<E> p5) {
        return PatternFactory.unapply5(target, p1, p2, p3, p4, p5);
    }

    public static <O, A, B, C, D, E, F>
    Pattern<O>
    PatternFor(@NonNull Class<? extends Unapply6> target, @NonNull Pattern<A> p1, @NonNull Pattern<B> p2, @NonNull Pattern<C> p3, @NonNull Pattern<D> p4, @NonNull Pattern<E> p5, @NonNull Pattern<F> p6) {
        return PatternFactory.unapply6(target, p1, p2, p3, p4, p5, p6);
    }

    public static <O, A, B, C, D, E, F, G>
    Pattern<O>
    PatternFor(@NonNull Class<? extends Unapply7> target, @NonNull Pattern<A> p1, @NonNull Pattern<B> p2, @NonNull Pattern<C> p3, @NonNull Pattern<D> p4, @NonNull Pattern<E> p5, @NonNull Pattern<F> p6, @NonNull Pattern<G> p7) {
        return PatternFactory.unapply7(target, p1, p2, p3, p4, p5, p6, p7);
    }

    public static <O, A, B, C, D, E, F, G, H>
    Pattern<O>
    PatternFor(@NonNull Class<? extends Unapply8> target, @NonNull Pattern<A> p1, @NonNull Pattern<B> p2, @NonNull Pattern<C> p3, @NonNull Pattern<D> p4, @NonNull Pattern<E> p5, @NonNull Pattern<F> p6, @NonNull Pattern<G> p7, @NonNull Pattern<H> p8) {
        return PatternFactory.unapply8(target, p1, p2, p3, p4, p5, p6, p7, p8);
    }
}
