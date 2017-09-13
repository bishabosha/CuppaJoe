/*
 * Copyright (c) 2017. Jamie Thompson <bishbashboshjt@gmail.com>
 */

package com.bishabosha.caffeine.sequences;

import com.bishabosha.caffeine.functional.control.Option;
import com.bishabosha.caffeine.pipelines.Pipeline;

import java.util.function.Consumer;
import java.util.function.Predicate;

public interface Sequence<T> extends Iterable<T> {
    Sequence<T> filter(Predicate<? super T> predicate);
    Sequence<T> takeWhile(Predicate<? super T> predicate);
    Sequence<T> dropWhile(Predicate<? super T> predicate);
    Sequence<T> peek(Consumer<? super T> action);
    Sequence<T> removeRearNode();
    Sequence<T> removeFrontNode();
    Sequence<T> clearNodes();
    Sequence<T> terms(int start, int end);
    Sequence<T> terms(int n);
    Option<T> term(int n);
    Pipeline<T> pipeline();
            int size();
}
