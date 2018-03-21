/*
 * Copyright (c) 2017. Jamie Thompson <bishbashboshjt@gmail.com>
 */

package io.cuppajoe.pipelines;

import io.cuppajoe.control.Option;

import java.util.Objects;
import java.util.function.Consumer;

abstract class AbstractNode<I, O> implements Consumer<Option<I>> {
    private boolean willTerminate;
    private AbstractNode<O, ?> downstream;

    public <R> AbstractNode<O, R> addDownstream(AbstractNode<O, R> node) {
        downstream = node;
        return node;
    }

    public boolean hasDownstream() {
        return downstream != null;
    }

    public boolean willTerminate() {
        return willTerminate;
    }

    public boolean downstreamWillTerminate() {
        return hasDownstream() && downstream.willTerminate();
    }

    public AbstractNode removeTerminal() {
        if (downstream instanceof TerminalNode) {
            downstream = null;
        }
        return this;
    }

    public static AbstractNode removeHead(AbstractNode initial) {
        var downstream = initial.downstream;
        if (downstream == null) {
            return null;
        }
        initial.downstream = null;
        return downstream;
    }

    public static AbstractNode removeTail(AbstractNode initial) {
        AbstractNode parent = null;
        var current = initial;
        while (current.downstream != null) {
            parent = current;
            current = current.downstream;
        }
        parent.downstream = null;
        return parent;
    }

    public void terminate() {
        willTerminate = true;
        if (hasDownstream()) {
            downstream.terminate();
        }
    }

    public void begin() {
        willTerminate = false;
        if (hasDownstream()) {
            downstream.begin();
        }
    }

    protected void send(Option<O> option) {
        Objects.requireNonNull(downstream);
        downstream.accept(option);
    }
}
