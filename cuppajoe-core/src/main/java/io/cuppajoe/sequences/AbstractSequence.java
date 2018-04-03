/*
 * Copyright (c) 2017. Jamie Thompson <bishbashboshjt@gmail.com>
 */

package io.cuppajoe.sequences;

import io.cuppajoe.Iterators;
import io.cuppajoe.collections.mutable.base.AbstractArrayHelper;
import io.cuppajoe.control.Option;
import io.cuppajoe.pipelines.Pipeline;

import java.util.Iterator;
import java.util.function.Consumer;
import java.util.function.Predicate;

import static io.cuppajoe.API.None;

public abstract class AbstractSequence<T> extends AbstractArrayHelper<T> implements Sequence<T> {
    int initialTerm = 0;
    int finalTerm = Integer.MAX_VALUE;
    AbstractBuilder<T> builder;
    Pipeline<T> pipeline;

    @Override
    public Sequence<T> terms(int from, int to) {
        if (checkTerms(from) && checkTerms(to) && to < from) {
            throw new RuntimeException("to must be larger than from");
        }
        initialTerm = from;
        finalTerm = to + 1;
        return this;
    }

    @Override
    public Sequence<T> terms(int n) {
        checkTerms(n);
        if (n < initialTerm) {
            initialTerm = 0;
        }
        finalTerm = n;
        return this;
    }

    @Override
    public Option<T> term(int n) {
        var num = removeOffset(n);
        var it = iterator();
        var i = 0;
        while (i < num && it.hasNext()) {
            var val = it.next();
            if ((++i) == num) {
                return Option.of(val);
            }
        }
        return None();
    }

    @Override
    public Sequence<T> filter(Predicate<? super T> predicate) {
        getPipeline().filter(predicate);
        return this;
    }

    @Override
    public Sequence<T> takeWhile(Predicate<? super T> predicate) {
        getPipeline().takeWhile(predicate);
        return this;
    }

    @Override
    public Sequence<T> dropWhile(Predicate<? super T> predicate) {
        getPipeline().dropWhile(predicate);
        return this;
    }

    @Override
    public Sequence<T> peek(Consumer<? super T> action) {
        getPipeline().peek(action);
        return this;
    }

    @Override
    public Sequence<T> removeRearNode() {
        getPipeline().removeTail();
        return this;
    }

    @Override
    public Sequence<T> removeFrontNode() {
        getPipeline().removeHead();
        return this;
    }

    @Override
    public Sequence<T> clearNodes() {
        pipeline = createPipeline();
        return this;
    }

    protected Pipeline<T> getPipeline() {
        if (null == pipeline) {
            clearNodes();
        }
        return pipeline;
    }

    protected Pipeline<T> createPipeline() {
        return Pipeline.of(builder);
    }

    protected Pipeline<T> copyPipeline() {
        return Pipeline.copy(getPipeline());
    }

    @Override
    public Pipeline<T> pipeline() {
        var stream = copyPipeline();
        if (finalTerm > initialTerm) {
            stream.pushLimit(finalTerm - initialTerm);
        }
        if (initialTerm > 0) {
            stream.pushSkip(initialTerm - 1);
        }
        return stream;
    }

    @Override
    public boolean equals(Object obj) {
        return obj == this || obj instanceof Sequence && Iterators.equals(this.iterator(), ((Sequence) obj).iterator());
    }

    @Override
    public Iterator<T> iterator() {
        return pipeline().iterator();
    }

    @Override
    public int size() {
        return builder.n;
    }

    @Override
    public boolean contains(Object o) {
        return pipeline().contains(o);
    }

    private boolean checkTerms(int n) {
        if (n <= 0) {
            throw new IndexOutOfBoundsException("Terms must be in the natural numbers");
        }
        return true;
    }

    protected int removeOffset(int term) {
        if (term < initialTerm || (finalTerm < Integer.MAX_VALUE && term > (finalTerm - 1))) {
            throw new IllegalArgumentException("Term must be in range");
        }
        return initialTerm == 0 ? term : term - initialTerm + 1;
    }
}
