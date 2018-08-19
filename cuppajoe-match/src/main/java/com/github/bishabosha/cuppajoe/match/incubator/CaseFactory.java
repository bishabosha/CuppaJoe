package com.github.bishabosha.cuppajoe.match.incubator;

import com.github.bishabosha.cuppajoe.annotation.NonNull;
import com.github.bishabosha.cuppajoe.control.Option;
import com.github.bishabosha.cuppajoe.higher.functions.Func1;
import com.github.bishabosha.cuppajoe.higher.functions.Func2;
import com.github.bishabosha.cuppajoe.match.Case;
import com.github.bishabosha.cuppajoe.match.incubator.MethodCapture.M1;
import com.github.bishabosha.cuppajoe.match.incubator.MethodCapture.M2;

import java.util.Objects;
import java.util.function.Supplier;

import static com.github.bishabosha.cuppajoe.API.None;

public final class CaseFactory {

    private CaseFactory() {
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
        return i -> matcher.test(i).map(mr -> {
            var m1 = new M1<>(binder);
            mr.accept(m1);
            return m1.invoke();
        });
    }

    public static <I, O, A, B> Case<I, O>
    with(@NonNull Pattern<I> matcher, @NonNull Func2<A, B, O> binder) {
        Objects.requireNonNull(binder, "binder");
        return i -> matcher.test(i).map(mr -> {
            var m2 = new M2<>(binder);
            mr.accept(m2);
            return m2.invoke();
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
