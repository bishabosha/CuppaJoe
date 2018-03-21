package com.bishabosha.cuppajoe.typeclass.peek;

import java.util.function.Consumer;

public interface Peek1<T1> {
    void peek(Consumer<? super T1> consumer);
}
