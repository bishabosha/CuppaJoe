package com.github.bishabosha.cuppajoe.higher.peek;

import com.github.bishabosha.cuppajoe.annotation.NonNull;

import java.util.function.Consumer;

public interface Peek1<T1> {
    void peek(@NonNull Consumer<? super T1> consumer);
}
