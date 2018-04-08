package com.github.bishabosha.cuppajoe.higher.compose;

import com.github.bishabosha.cuppajoe.annotation.NonNull;
import com.github.bishabosha.cuppajoe.functions.Func4;

public interface Compose4<T1, T2, T3, T4> {
    <O> O compose(@NonNull Func4<? super T1, ? super T2, ? super T3, ? super T4, ? extends O> mapper);
}
