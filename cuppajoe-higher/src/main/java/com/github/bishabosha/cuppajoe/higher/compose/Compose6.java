package com.github.bishabosha.cuppajoe.higher.compose;

import com.github.bishabosha.cuppajoe.annotation.NonNull;
import com.github.bishabosha.cuppajoe.higher.functions.Func6;

public interface Compose6<T1, T2, T3, T4, T5, T6> {
    <O> O compose(@NonNull Func6<? super T1, ? super T2, ? super T3, ? super T4, ? super T5, ? super T6, ? extends O> mapper);
}
