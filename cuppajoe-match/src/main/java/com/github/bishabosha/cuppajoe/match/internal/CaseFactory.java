package com.github.bishabosha.cuppajoe.match.internal;

import com.github.bishabosha.cuppajoe.annotation.NonNull;
import com.github.bishabosha.cuppajoe.control.Option;
import com.github.bishabosha.cuppajoe.higher.functions.*;
import com.github.bishabosha.cuppajoe.match.Case;
import com.github.bishabosha.cuppajoe.match.util.MethodCapture;
import com.github.bishabosha.cuppajoe.match.util.MethodCapture.*;
import com.github.bishabosha.cuppajoe.match.patterns.Pattern;

import java.util.Objects;
import java.util.function.Function;
import java.util.function.Supplier;

import static com.github.bishabosha.cuppajoe.API.None;

public final class CaseFactory {

    private CaseFactory() {
    }

    public static <I, O> Case<I, O>
    with(@NonNull Pattern<I> matcher, @NonNull O value) {
        Objects.requireNonNull(value, "value");
        return i -> matcher.test(i).map(ignored -> value);
    }

    public static <I, O> Case<I, O>
    with(@NonNull Pattern<I> matcher, @NonNull Supplier<O> binder) {
        Objects.requireNonNull(binder, "binder");
        return i -> matcher.test(i).map(ignored ->
            binder.get()
        );
    }

    public static <I, O, A> Case<I, O>
    with(@NonNull Pattern<I> matcher, @NonNull Func1<A, O> binder) {
        Objects.requireNonNull(binder, "binder");
        return base(matcher, binder, M1::new);
    }

    public static <I, O, A, B> Case<I, O>
    with(@NonNull Pattern<I> matcher, @NonNull Func2<A, B, O> binder) {
        Objects.requireNonNull(binder, "binder");
        return base(matcher, binder, M2::new);
    }

    public static <I, O, A, B, C> Case<I, O>
    with(@NonNull Pattern<I> matcher, @NonNull Func3<A, B, C, O> binder) {
        Objects.requireNonNull(binder, "binder");
        return base(matcher, binder, M3::new);
    }

    public static <I, O, A, B, C, D> Case<I, O>
    with(@NonNull Pattern<I> matcher, @NonNull Func4<A, B, C, D, O> binder) {
        Objects.requireNonNull(binder, "binder");
        return base(matcher, binder, M4::new);
    }

    public static <I, O, A, B, C, D, E> Case<I, O>
    with(@NonNull Pattern<I> matcher, @NonNull Func5<A, B, C, D, E, O> binder) {
        Objects.requireNonNull(binder, "binder");
        return base(matcher, binder, M5::new);
    }

    public static <I, O, A, B, C, D, E, F> Case<I, O>
    with(@NonNull Pattern<I> matcher, @NonNull Func6<A, B, C, D, E, F, O> binder) {
        Objects.requireNonNull(binder, "binder");
        return base(matcher, binder, M6::new);
    }

    public static <I, O, A, B, C, D, E, F, G> Case<I, O>
    with(@NonNull Pattern<I> matcher, @NonNull Func7<A, B, C, D, E, F, G, O> binder) {
        Objects.requireNonNull(binder, "binder");
        return base(matcher, binder, M7::new);
    }

    public static <I, O, A, B, C, D, E, F, G, H> Case<I, O>
    with(@NonNull Pattern<I> matcher, @NonNull Func8<A, B, C, D, E, F, G, H, O> binder) {
        Objects.requireNonNull(binder, "binder");
        return base(matcher, binder, M8::new);
    }

    private static <I, O, F> Case<I, O> base(@NonNull Pattern<I> matcher, F func, Function<F, ? extends MethodCapture<O>> mapper) {
        Objects.requireNonNull(matcher, "matcher");
        return i ->
            matcher.test(i)
                   .map(result -> {
                        var mc = mapper.apply(func);
                        result.accept(mc);
                        return mc.invoke();
                   });
    }

    @SafeVarargs
    public static <I, O> Case<I, O> combine(@NonNull Case<I, O>... cases) {
        Objects.requireNonNull(cases, "cases");
        return i -> {
            Option<O> result = None();
            for (var c : cases) {
                result = c.match(i);
                if (!result.isEmpty()) {
                    break;
                }
            }
            return result;
        };
    }
}
