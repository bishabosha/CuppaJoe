package io.cuppajoe.patterns;

import io.cuppajoe.Foldable;
import io.cuppajoe.Iterables;
import io.cuppajoe.Iterables.Lockable;
import io.cuppajoe.collections.immutable.Array;
import io.cuppajoe.collections.immutable.List;
import io.cuppajoe.control.Option;
import io.cuppajoe.tuples.Product;
import org.jetbrains.annotations.NotNull;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Objects;

import static io.cuppajoe.API.*;

public interface Result<E> extends Iterable<E> {

    static <E> Result<E> compose(Result<E> a, Result<E> b) {
        return new Node<>(Tuple(a, b));
    }

    static <E> Result<E> compose(Result<E> a, Result<E> b, Result<E> c) {
        return new Node<>(Tuple(a, b, c));
    }

    static <E> Result<E> compose(Result<E> a, Result<E> b, Result<E> c, Result<E> d) {
        return new Node<>(Tuple(a, b, c, d));
    }

    static <E> Result<E> compose(Result<E> a, Result<E> b, Result<E> c, Result<E> d, Result<E> e) {
        return new Node<>(Tuple(a, b, c, d, e));
    }

    static <E> Result<E> compose(Result<E> a, Result<E> b, Result<E> c, Result<E> d, Result<E> e, Result<E> f) {
        return new Node<>(Tuple(a, b, c, d, e, f));
    }

    static <E> Result<E> compose(Result<E> a, Result<E> b, Result<E> c, Result<E> d, Result<E> e, Result<E> f, Result<E> g) {
        return new Node<>(Tuple(a, b, c, d, e, f, g));
    }

    static <E> Result<E> compose(Result<E> a, Result<E> b, Result<E> c, Result<E> d, Result<E> e, Result<E> f, Result<E> g, Result<E> h) {
        return new Node<>(Tuple(a, b, c, d, e, f, g, h));
    }

    static <E> Result<E> of(E value) {
        return new Leaf<>(value);
    }

    @SuppressWarnings("unchecked")
    static <E> Result<E> empty() {
        return (Result<E>) Node.EMPTY;
    }

    boolean isEmpty();

    boolean isLeaf();

    Option<E> get();

    Iterable<Result<E>> branches();

    default Values values() {
        return new Values(iterator());
    }

    default int size() {
        return Foldable.foldOver(this, 0, (acc, x) -> acc = acc + 1);
    }

    class Node<E> implements Result<E> {

        private static final Result<?> EMPTY = new Node<>(Tuple());

        Product branches;

        private Node(Product branches) {
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
        public Option<E> get() {
            return Nothing();
        }

        @Override
        public Iterable<Result<E>> branches() {
            return () -> Iterables.tupleIterator(branches);
        }

        @NotNull
        @Override
        public Iterator<E> iterator() {
            return new Lockable<>() {
                private List<Iterator<Result<E>>> stack = isEmpty() ? List() : List(Node.this.branches().iterator());
                private Option<E> toReturn;

                @Override
                public boolean hasNextSupplier() {
                    var nextItem = stack.nextItem((it, xs) -> {
                        if (it.hasNext()) {
                            var tree = it.next();
                            if (it.hasNext()) {
                                xs = xs.push(it);
                            }
                            if (tree.isLeaf()) {
                                return Some(Tuple(tree.get(), xs));
                            } else {
                                return Some(Tuple(Nothing(), tree.isEmpty() ? xs : xs.push(tree.branches().iterator())));
                            }
                        }
                        return Nothing();
                    });
                    toReturn = nextItem.$1();
                    stack = nextItem.$2();
                    return !toReturn.isEmpty();
                }

                @Override
                public E nextSupplier() {
                    return toReturn.get();
                }
            };
        }

        @Override
        public int hashCode() {
            return Iterables.hash(this);
        }

        @Override
        public boolean equals(Object obj) {
            return obj == this || obj instanceof Result && Iterables.equals(this, obj);
        }

        @Override
        public String toString() {
            return Iterables.toString('[', ']', iterator());
        }
    }

    class Leaf<E> implements Result<E> {

        private E value;

        private Leaf(E value) {
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
        public Option<E> get() {
            return Some(value);
        }

        @Override
        public Iterable<Result<E>> branches() {
            return Array.empty();
        }

        @NotNull
        @Override
        public Iterator<E> iterator() {
            return Iterables.singleton(() -> value);
        }

        @Override
        public String toString() {
            return "[" + value + "]" ;
        }

        @Override
        public int hashCode() {
            return Objects.hashCode(value);
        }

        @Override
        public boolean equals(Object obj) {
            if (obj == this) {
                return true;
            }
            if (obj instanceof Result) {
                var otherVals = ((Result) obj).iterator();
                return otherVals.hasNext()
                    && Objects.equals(otherVals.next(), value)
                    && !otherVals.hasNext();
            }
            return false;
        }
    }

    class Values {
        private final Iterator<?> iterator;
        private int count = -1;

        private Values(Iterator<?> iterator) {
            this.iterator = iterator;
        }

        @SuppressWarnings("unchecked")
        public <O> O next() {
            try {
                if (iterator.hasNext()) {
                    count = count + 1;
                    return (O) iterator.next();
                } else {
                    throw new NoSuchElementException("Not enough values contained in Pattern Result.");
                }
            } catch (ClassCastException cce) {
                throw new ClassCastException("Pattern Result elem(" + count + ") is not of the type requested.");
            }
        }
    }
}
