package io.cuppajoe.match;

import io.cuppajoe.Iterators;
import io.cuppajoe.Iterators.IdempotentIterator;
import io.cuppajoe.collections.immutable.List;
import io.cuppajoe.control.Either;
import io.cuppajoe.tuples.Tuple;
import io.cuppajoe.tuples.Tuple2;
import io.cuppajoe.tuples.Unit;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.Iterator;
import java.util.NoSuchElementException;

import static io.cuppajoe.API.*;

public interface Result {

    static Result compose(Result a, Result b) {
        return new Node(Tuple.of(a, b));
    }

    static Result compose(Result a, Result b, Result c) {
        return new Node(Tuple.of(a, b, c));
    }

    static Result compose(Result a, Result b, Result c, Result d) {
        return new Node(Tuple.of(a, b, c, d));
    }

    static Result compose(Result a, Result b, Result c, Result d, Result e) {
        return new Node(Tuple.of(a, b, c, d, e));
    }

    static Result compose(Result a, Result b, Result c, Result d, Result e, Result f) {
        return new Node(Tuple.of(a, b, c, d, e, f));
    }

    static Result compose(Result a, Result b, Result c, Result d, Result e, Result f, Result g) {
        return new Node(Tuple.of(a, b, c, d, e, f, g));
    }

    static Result compose(Result a, Result b, Result c, Result d, Result e, Result f, Result g, Result h) {
        return new Node(Tuple.of(a, b, c, d, e, f, g, h));
    }

    static Result of(Object value) {
        return new Leaf(value);
    }

    @SuppressWarnings("unchecked")
    static Result empty() {
        return Node.EMPTY;
    }

    boolean isEmpty();

    boolean isLeaf();

    Object get();

    Iterator<Result> branches();

    Values values();

    default int size() {
        var size = 0;
        var values = values();
        while (values.hasNext()) {
            size++;
            values.nextVal();
        }
        return size;
    }

    class Node implements Result {

        private static final Result EMPTY = new Node(Tuple());

        Tuple branches;

        private Node(Tuple branches) {
            this.branches = branches;
        }

        @Override
        public boolean isEmpty() {
            return branches.arity() == 0;
        }

        public boolean isLeaf() {
            return false;
        }

        @Override
        public Object get() {
            throw new NoSuchElementException();
        }

        @Override
        public Iterator<Result> branches() {
            return branches.iterator();
        }

        @Override
        @NotNull
        public Values values() {
            return new Values() {

                private List<Iterator<Result>> stack = isEmpty() ? List() : List(Node.this.branches());
                private Object toReturn;

                @Override
                public boolean hasNextSupplier() {
                    return stack.nextItem(this::stackAlgorithm)
                        .map(this::foundItem)
                        .containsValue();
                }

                private Either<List<Iterator<Result>>, Tuple2<Object, List<Iterator<Result>>>> stackAlgorithm(Iterator<Result> it, List<Iterator<Result>> xs) {
                    if (it.hasNext()) {
                        var result = it.next();
                        if (it.hasNext()) {
                            xs = xs.push(it);
                        }
                        if (result.isLeaf()) {
                            return Right(Tuple(result.get(), xs));
                        } else {
                            return Left(result.isEmpty() ? xs : xs.push(result.branches()));
                        }
                    }
                    return Left(List());
                }

                private Unit foundItem(Tuple2<Object, List<Iterator<Result>>> tuple) {
                    return tuple.compose((elem, xs) -> {
                        stack = xs;
                        toReturn = elem;
                        return Unit.INSTANCE;
                    });
                }

                public Object nextSupplier() {
                    return toReturn;
                }
            };
        }

        @Override
        public int hashCode() {
            return Iterators.hash(values());
        }

        @Override
        public boolean equals(Object obj) {
            return obj == this || obj instanceof Result && Iterators.equals(values(), ((Result) obj).values());
        }
    }

    class Leaf implements Result {

        private Object value;

        private Leaf(Object value) {
            this.value = value;
        }

        @Override
        public boolean isEmpty() {
            return false;
        }

        @Override
        public boolean isLeaf() {
            return true;
        }

        @Override
        public Object get() {
            return value;
        }

        @Override
        public Iterator<Result> branches() {
            return Collections.emptyIterator();
        }

        @Override
        public Values values() {
            return new SingleValues(value);
        }

        @Override
        public int hashCode() {
            return Iterators.hash(values());
        }

        @Override
        public boolean equals(Object obj) {
            return obj == this || obj instanceof Result && Iterators.equals(values(), ((Result) obj).values());
        }
    }

    class SingleValues extends Values {

        private final Object value;
        private boolean unboxed = false;

        private SingleValues(Object value) {
            this.value = value;
        }

        @Override
        public boolean hasNextSupplier() {
            return !unboxed;
        }

        @Override
        public Object nextSupplier() {
            unboxed = true;
            return value;
        }
    }

    abstract class Values extends IdempotentIterator {
        private int count = -1;

        @SuppressWarnings("unchecked")
        public <O> O nextVal() {
            count++;
            try {
                return (O) next();
            } catch (ClassCastException cce) {
                throw new ClassCastException("Result elem " + count + " is not of the type requested.");
            }
        }
    }
}
