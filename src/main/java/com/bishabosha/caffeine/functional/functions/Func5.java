/*
 * Copyright (c) 2017. Jamie Thompson <bishbashboshjt@gmail.com>
 */

package com.bishabosha.caffeine.functional.functions;

import com.bishabosha.caffeine.functional.Option;
import com.bishabosha.caffeine.functional.tuples.Tuple5;

public interface Func5<A, B, C, D, E, R> {
    R apply(A a, B b, C c, D d, E e);

    static <V,W,X,Y,Z,R> Func5<V,W,X,Y,Z,R> of(Func5<V,W,X,Y,Z,R> func) {
        return func;
    }

    default Func5<A, B, C, D, E, Option<R>> lifted() {
        return CheckedFunc5.of(this::apply).lifted();
    }

    default Func1<A, Func1<B, Func1<C, Func1<D, Func1<E, R>>>>> curried() {
        return v -> w -> x -> y -> z -> apply(v, w, x, y, z);
    }

    default Func1<Tuple5<A, B, C, D, E>, R> tupled() {
        return x -> apply(x.$1(), x.$2(), x.$3(), x.$4(), x.$5());
    }
}
