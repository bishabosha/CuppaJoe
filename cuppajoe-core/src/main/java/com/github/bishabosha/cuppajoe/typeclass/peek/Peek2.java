package com.github.bishabosha.cuppajoe.typeclass.peek;

import com.github.bishabosha.cuppajoe.annotation.NonNull;

import java.util.function.Consumer;

public interface Peek2<T1, T2> {
    void peek(@NonNull Consumer<? super T1> c1, @NonNull Consumer<? super T2> c2);
}
