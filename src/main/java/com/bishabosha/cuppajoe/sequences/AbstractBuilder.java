/*
 * Copyright (c) 2017. Jamie Thompson <bishbashboshjt@gmail.com>
 */

package com.bishabosha.cuppajoe.sequences;

import com.bishabosha.cuppajoe.Iterables;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

abstract class AbstractBuilder<E> implements Iterable<E> {
    protected List<E> cache = getEmptyList();
    protected E currentValue = null;
    protected int n;

    @Override
    public Iterator<E> iterator() {
        return new Iterables.Lockable<E>() {
            boolean generateNewTerm = true;
            boolean lastHasNext = false;
            {
                setUpIterator();
            }

            @Override
            public boolean hasNextSupplier() {
                if (generateNewTerm) { // prevent term calculation again until next() is called
                    generateNewTerm = false;
                    lastHasNext = getNextValue() != null;
                }
                return lastHasNext;
            }

            @Override
            public E nextSupplier() {
                generateNewTerm = true;
                return currentValue;
            }
        };
    }

    protected void setUpIterator() {
        n = 0;
    }

    protected E getNextValue() {
        if (incN() < cache.size()) {
            currentValue = cache.get(getN());
        } else {
            currentValue = generateTerm();
            if (null != currentValue) {
                cache.add(currentValue);
            }
        }
        return currentValue;
    }

    protected int incN() {
        return n++;
    }

    protected int getN() {
        return n - 1;
    }

    protected abstract E generateTerm();

    public List<E> getEmptyList() {
        return new ArrayList<>();
    }

    public E buildAndReturn(int term) {
        while (n < term) {
            getNextValue();
        }
        return cache.get(term-1);
    }
}
