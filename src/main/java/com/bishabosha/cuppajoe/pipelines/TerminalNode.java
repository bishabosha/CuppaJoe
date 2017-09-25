/*
 * Copyright (c) 2017. Jamie Thompson <bishbashboshjt@gmail.com>
 */

package com.bishabosha.cuppajoe.pipelines;

import java.util.*;

import com.bishabosha.cuppajoe.Iterables;
import com.bishabosha.cuppajoe.control.Option;
import com.bishabosha.cuppajoe.collections.mutable.lists.LinkedList;

import static com.bishabosha.cuppajoe.API.Option;

class TerminalNode<I, O> extends AbstractNode<O, O> implements Iterable<O>{

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
                new Iterables.Lockable<>() {
                    Iterator<I> source = sourceIterable.iterator();
                    boolean sourceHasNext = false;
                    O current;

                    @Override
                    public boolean hasNextSupplier() {
                        while (!willTerminate()) {
                            if (sourceHasNext = source.hasNext()) {
                                head.accept(Option(source.next()));
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
        Objects.requireNonNull(source);
        this.sourceIterable = source;
        if (tail != null) {
            tail.addDownstream(this);
        }
        this.head = head;
    }

    @Override
    public void accept(Option<O> option) {
        option.ifSome(buffer::add);
    }
}
