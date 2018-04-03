package io.cuppajoe.typeclass.peek;

import java.util.function.Consumer;

public interface Peek2<T1, T2> {
    void peek(Consumer<? super T1> c1, Consumer<? super T2> c2);
}
