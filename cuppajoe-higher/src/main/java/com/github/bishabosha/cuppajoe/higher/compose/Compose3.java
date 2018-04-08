package com.github.bishabosha.cuppajoe.higher.compose;

import com.github.bishabosha.cuppajoe.annotation.NonNull;
import com.github.bishabosha.cuppajoe.functions.Func3;

public interface Compose3<T1, T2, T3> {
    <O> O compose(@NonNull Func3<? super T1, ? super T2, ? super T3, ? extends O> mapper);
}
