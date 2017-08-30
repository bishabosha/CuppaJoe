/*
 * Copyright (c) 2017. Jamie Thompson <bishbashboshjt@gmail.com>
 */

package com.bishabosha.caffeine.functional.tuples;

import com.bishabosha.caffeine.base.AbstractBase;
import com.bishabosha.caffeine.functional.Library;
import com.bishabosha.caffeine.functional.Try;

import java.util.*;
import java.util.function.Supplier;

public class Tuple extends AbstractBase<Object> {

    public final static Tuple EMPTY = new Tuple();

    protected Tuple() {
        size = 0;
    }

    protected SupplierIterable supplierIterable = new SupplierIterable();

    public static Tuple empty() {
        return EMPTY;
    }

    @Override
    public boolean contains(Object o) {
        for (Object elem: this) {
            if (elem.equals(o)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (obj instanceof Tuple) {
            Tuple tup = (Tuple) obj;
            if (size() != tup.size()) {
                return false;
            }
            Iterator<Object> other = tup.iterator();
            for (Object elem: this) {
                if (other.hasNext() && !elem.equals(other.next())) {
                    return false;
                }
            }
            return true;
        }
        return false;
    }

    public List flatten() {
        return Library.flatten(Tuple.class, this);
    }

    @Override
    public Iterator iterator() {
        return supplierIterable.iterator();
    }

    @Override
    public String toString() {
        return "()";
    }

    public Try<Object> get(int index) {
        return Try.of(() -> supplierIterable.innerList.get(index-1).get());
    }

    class SupplierIterable implements Iterable {
        ArrayList<Supplier<?>> innerList = new ArrayList<>();

        public void add(Supplier<?> supplier) {
            innerList.add(supplier);
        }

        @Override
        public Iterator iterator() {
            return new Iterator() {

                Iterator<Supplier<?>> suppliers = innerList.iterator();

                @Override
                public boolean hasNext() {
                    return suppliers.hasNext();
                }

                @Override
                public Object next() {
                    return suppliers.next().get();
                }
            };
        }
    }
}
