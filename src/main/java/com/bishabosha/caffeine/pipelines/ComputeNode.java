/*
 * Copyright (c) 2017. Jamie Thompson <bishbashboshjt@gmail.com>
 */

package com.bishabosha.caffeine.pipelines;

import com.bishabosha.caffeine.functional.API;
import com.bishabosha.caffeine.functional.control.Option;
import com.bishabosha.caffeine.functional.functions.Func1;

import java.util.Iterator;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

import static com.bishabosha.caffeine.functional.API.Option;

class ComputeNode<I, O> extends AbstractNode<I, O> {

    private BiConsumer<ComputeNode, Option<I>> compute;

    public static <T, R> ComputeNode<T, R> map(Func1<T, R> mapper) {
        return new ComputeNode<>(
            (cn, op) -> cn.send(op.map(mapper)));
    }

    public static <T, R> ComputeNode<T, R> flatMap(Func1<? super T, ? extends AbstractPipeline> mapper){
        return new ComputeNode<>(
            (cn, op) -> cn.sendFlattened(
                op.flatMap(x -> Option(mapper.apply(x)))));
    }

    public static <T> ComputeNode<T, T> peek(Consumer<? super T> action) {
        return new ComputeNode<>(
            (cn, op) -> cn.sendAfterAction(op, action));
    }

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
        if (value.isSome()) {
            action.accept(value.get());
            send(value);
        }
    }

    private void sendFlattened(Option<? extends AbstractPipeline> option) {
        option.ifSome(p -> {
            Iterator<O> iterator = p.iterator();
            while (!downstreamWillTerminate() && iterator.hasNext()) {
                send(Option(iterator.next()));
            }
        });
    }

    public void sendOrTerminate(Option<O> t, boolean terminateIfNotPresent) {
        if (t.isSome()) {
            send(t);
        } else if (terminateIfNotPresent) {
            terminate();
        }
    }

    private ComputeNode(BiConsumer<ComputeNode, Option<I>> compute) {
        this.compute = compute;
    }
}
