/*
 * Copyright (c) 2017. Jamie Thompson <bishbashboshjt@gmail.com>
 */

package com.bishabosha.cuppajoe.pipelines;

import com.bishabosha.cuppajoe.collections.mutable.base.AbstractBase;
import com.bishabosha.cuppajoe.collections.mutable.lists.LinkedList;

import java.util.*;
import java.util.function.Function;
import java.util.function.IntFunction;
import java.util.function.Predicate;

class AbstractPipeline<T> extends AbstractBase<T> {
    boolean isClosed;
    Queue<Runnable> closeHandlers;
    private Iterable source;
    private AbstractNode head;
    private AbstractNode tail;

    public AbstractPipeline(Iterable source) {
        this();
        initFrom(source);
    }

    public AbstractPipeline(AbstractPipeline pipeline) {
        initFrom(pipeline);
    }

    private AbstractPipeline() {
        closeHandlers = new LinkedList<>();
        isClosed = false;
    }

    public AbstractPipeline(Iterable source, AbstractPipeline parent) {
        this.source = source;
        closeHandlers = parent.closeHandlers;
        isClosed = parent.isClosed;
    }

    private void initFrom(AbstractPipeline pipeline) {
        source = pipeline.source;
        head = pipeline.head;
        tail = null == pipeline.tail ? null : pipeline.tail.removeTerminal();
        closeHandlers = pipeline.closeHandlers;
        isClosed = pipeline.isClosed;
        size = pipeline.size;
        if (head != null) {
            head.begin();
        }
    }

    private void initFrom(Iterable iterable) {
        source = iterable;
    }

    /**
     * @return the number with nodes in this pipeline
     */
    @Override
    public int size() {
        return super.size();
    }

    protected AbstractPipeline addFunction(AbstractNode functionNode) {
        if (head == null) {
            tail = head = functionNode;
        } else if (!head.hasDownstream()) {
            tail = head.addDownstream(functionNode);
        } else {
            tail = tail.addDownstream(functionNode);
        }
        size++;
        return this;
    }

    protected AbstractPipeline pushFunction(AbstractNode functionNode) {
        if (head == null) {
            tail = head = functionNode;
        } else {
            functionNode.addDownstream(head);
            head = functionNode;
        }
        size++;
        return this;
    }

    public AbstractPipeline removeHead() {
        if (tail == head) {
            head = tail = null;
        } else {
            head = AbstractNode.removeHead(head);
        }
        size--;
        return this;
    }

    public AbstractPipeline removeTail() {
        if (tail == head) {
            head = tail = null;
        } else {
            tail = AbstractNode.removeTail(head);
        }
        size--;
        return this;
    }

    @Override
    public Iterator<T> iterator() {
        checkIfClosed();
        return TerminalNode.basic(source, head, tail).iterator();
    }

    @Override
    public Spliterator<T> spliterator() {
        var list = new LinkedList<T>();
        iterator().forEachRemaining(list::add);
        return list.spliterator();
    }

    public AbstractPipeline onClose(Runnable closeHandler) {
        closeHandlers.add(closeHandler);
        return this;
    }

    public void close() {
        checkIfClosed();
        isClosed = true;
        closeHandlers.forEach(x -> x.run());
    }

    private boolean checkIfClosed() {
        if (isClosed) {
            throw new RuntimeException("This pipeline is closed");
        }
        return true;
    }

    protected AbstractNode<T, T> getLimit(long maxSize) {
        return ComputeNode.filter(new Predicate<T>() {
            long count = 0;
            @Override
            public boolean test(T o) {
                return count++ < maxSize;
            }
        }, true);
    }

    protected AbstractNode<T, T> getSkip(long n) {
        return ComputeNode.filter(new Predicate<T>() {
            long count = 0;
            @Override
            public boolean test(T o) {
                return count++ >= n;
            }
        }, false);
    }

    protected boolean matcher(boolean testMatch,
                              boolean ifMatch,
                              boolean noneMatch,
                              Predicate<? super T> predicate) {
        for (var elem: this) {
            var test = predicate.test(elem);
            if ((test && testMatch) || (!test && !testMatch)) {
                return ifMatch;
            }
        }
        return noneMatch;
    }

    public Object[] toArray() {
        return toArrayFunc(xs -> xs.toArray());
    }

    public <A> A[] toArray(IntFunction<A[]> generator) {
        return (A[]) toArrayFunc(xs -> xs.toArray(generator.apply(xs.size())));
    }

    private Object[] toArrayFunc(Function<List<T>, Object[]> function) {
        var list = new ArrayList<T>();
        forEach(list::add);
        return function.apply(list);
    }

    @Override
    public boolean contains(Object o) {
        Objects.requireNonNull(o);
        Iterator<T> it = iterator();
        while (it.hasNext()) {
            if (it.next().equals(o)) {
                return true;
            }
        }
        return false;
    }
}
