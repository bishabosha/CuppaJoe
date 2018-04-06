package io.cuppajoe.typeclass.peek;

import io.cuppajoe.annotation.NonNull;

import java.util.function.Consumer;

public interface Peek1<T1> {
    void peek(@NonNull Consumer<? super T1> consumer);
}
