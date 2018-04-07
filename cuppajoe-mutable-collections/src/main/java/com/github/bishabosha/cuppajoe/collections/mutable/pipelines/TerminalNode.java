/*
 * Copyright (c) 2017. Jamie Thompson <bishbashboshjt@gmail.com>
 */

package com.github.bishabosha.cuppajoe.collections.mutable.pipelines;

import com.github.bishabosha.cuppajoe.collections.mutable.lists.LinkedList;
import com.github.bishabosha.cuppajoe.control.Option;
import com.github.bishabosha.cuppajoe.util.Iterators;

import java.util.Iterator;
import java.util.Objects;

class TerminalNode<I, O> extends AbstractNode<O, O> implements Iterable<O> {

    LinkedList<O> buffer = new LinkedList<>(); // need sort to work on my lists
    Iterable<I> sourceIterable;
    AbstractNode<I, ?> head;

    public static <T, R> TerminalNode<T, R> basic(Iterable<T> source,
                                                  AbstractNode<T, ?> head,
                                                  AbstractNode<?, R> tail) {
        return new TerminalNode<>(source, head, tail);
    }

    @Override
    public Iterator<O> iterator() {
        return (head == null) ?
                (Iterator<O>) sourceIterable.iterator() :
                new Iterators.IdempotentIterator<>() {
                    Iterator<I> source = sourceIterable.iterator();
                    boolean sourceHasNext = false;
                    O current;

                    @Override
                    public boolean hasNextSupplier() {
                        while (!willTerminate()) {
                            if (sourceHasNext = source.hasNext()) {
                                head.accept(Option.of(source.next()));
                            }
                            if (!buffer.isEmpty()) {
                                current = buffer.remove();
                                break;
                            } else if (!sourceHasNext) {
                                terminate();
                            }
                        }
                        return !willTerminate();
                    }

                    @Override
                    public O nextSupplier() {
                        return current;
                    }
                };
    }

    private TerminalNode(Iterable<I> source,
                         AbstractNode<I, ?> head,
                         AbstractNode<?, O> tail) {
        Objects.requireNonNull(source, "source");
        this.sourceIterable = source;
        if (tail != null) {
            tail.addDownstream(this);
        }
        this.head = head;
    }

    @Override
    public void accept(Option<O> option) {
        option.peek(buffer::add);
    }
}
