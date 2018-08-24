package com.github.bishabosha.cuppajoe.match.incubator;

@FunctionalInterface
public interface Path<I, O> {
    O get(I source);

    @SuppressWarnings("unchecked")
    static <I> Path<I, I> identity() {
        return x -> x;
    }

    default <U> Path<I, U> then(Path<O, U> next) {
        return i -> next.get(get(i));
    }
}
