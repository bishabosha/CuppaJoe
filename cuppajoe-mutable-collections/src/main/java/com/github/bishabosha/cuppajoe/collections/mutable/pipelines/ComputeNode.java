/*
 * Copyright (c) 2017. Jamie Thompson <bishbashboshjt@gmail.com>
 */

package com.github.bishabosha.cuppajoe.collections.mutable.pipelines;

import com.github.bishabosha.cuppajoe.control.Option;
import com.github.bishabosha.cuppajoe.functions.Func1;

import java.util.Iterator;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Predicate;

class ComputeNode<I, O> extends AbstractNode<I, O> {

    private BiConsumer<ComputeNode, Option<I>> compute;

    @SuppressWarnings("unchecked")
    public static <T, R> ComputeNode<T, R> map(Func1<T, R> mapper) {
        return new ComputeNode<>(
                (cn, op) -> cn.send(op.map(mapper)));
    }

    @SuppressWarnings("unchecked")
    public static <T, R> ComputeNode<T, R> flatMap(Func1<? super T, ? extends AbstractPipeline> mapper) {
        return new ComputeNode<>(
                (cn, op) -> cn.sendFlattened(
                        op.flatMap(x -> Option.of(mapper.apply(x)))));
    }

    @SuppressWarnings("unchecked")
    public static <T> ComputeNode<T, T> peek(Consumer<? super T> action) {
        return new ComputeNode<>(
                (cn, op) -> cn.sendAfterAction(op, action));
    }

    @SuppressWarnings("unchecked")
    public static <T> ComputeNode<T, T> filter(Predicate<T> passCondition, boolean terminateOnFail) {
        return new ComputeNode<>(
                (cn, op) -> cn.sendOrTerminate(op.filter(passCondition), terminateOnFail));
    }

    @Override
    public void accept(Option<I> i) {
        if (!willTerminate()) {
            compute.accept(this, i);
        }
    }

    private void sendAfterAction(Option<O> value, Consumer<? super O> action) {
        if (!value.isEmpty()) {
            action.accept(value.get());
            send(value);
        }
    }

    @SuppressWarnings("unchecked")
    private void sendFlattened(Option<? extends AbstractPipeline> option) {
        option.peek(p -> {
            Iterator<O> iterator = p.iterator();
            while (!downstreamWillTerminate() && iterator.hasNext()) {
                send(Option.of(iterator.next()));
            }
        });
    }

    public void sendOrTerminate(Option<O> t, boolean terminateIfNotPresent) {
        if (!t.isEmpty()) {
            send(t);
        } else if (terminateIfNotPresent) {
            terminate();
        }
    }

    private ComputeNode(BiConsumer<ComputeNode, Option<I>> compute) {
        this.compute = compute;
    }
}
