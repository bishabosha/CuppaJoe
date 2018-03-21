package io.cuppajoe.patterns;

import io.cuppajoe.Foldable;
import io.cuppajoe.collections.immutable.List;
import io.cuppajoe.control.Option;
import io.cuppajoe.Iterables;
import io.cuppajoe.Iterables.Lockable;
import io.cuppajoe.collections.immutable.Array;
import io.cuppajoe.collections.immutable.List;
import io.cuppajoe.functions.*;
import org.jetbrains.annotations.NotNull;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Objects;

import static io.cuppajoe.API.*;

public interface Result<E> extends Iterable<E> {

    static <E> Result<E> compose(Result<E>... results) {
        return results == null ? new Leaf<>(null) : new Node<>(Array.of(results));
    }

    static <E> Func2<Result<E>, Result<E>, Result<E>> compose2() {
        return Array.<Result<E>>of2().andThen(Node::new);
    }

    static <E> Func3<Result<E>, Result<E>, Result<E>, Result<E>> compose3() {
        return Array.<Result<E>>of3().andThen(Node::new);
    }

    static <E> Func4<Result<E>, Result<E>, Result<E>, Result<E>, Result<E>> compose4() {
        return Array.<Result<E>>of4().andThen(Node::new);
    }

    static <E> Func5<Result<E>, Result<E>, Result<E>, Result<E>, Result<E>, Result<E>> compose5() {
        return Array.<Result<E>>of5().andThen(Node::new);
    }

    static <E> Func6<Result<E>, Result<E>, Result<E>, Result<E>, Result<E>, Result<E>, Result<E>> compose6() {
        return Array.<Result<E>>of6().andThen(Node::new);
    }

    static <E> Func7<Result<E>, Result<E>, Result<E>, Result<E>, Result<E>, Result<E>, Result<E>, Result<E>> compose7() {
        return Array.<Result<E>>of7().andThen(Node::new);
    }

    static <E> Func8<Result<E>, Result<E>, Result<E>, Result<E>, Result<E>, Result<E>, Result<E>, Result<E>, Result<E>> compose8() {
        return Array.<Result<E>>of8().andThen(Node::new);
    }

    @SafeVarargs
    static <E> Result<E> of(E... values) {
        return values == null
            ? new Leaf<>(null)
            : values.length == 0
                ? empty()
                : new Node<>(Array.of(values).map(Leaf::new));
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

    Array<Result<E>> branches();

    default Values values() {
        return new Values(iterator());
    }

    default int size() {
        return Foldable.foldOver(this, 0, (acc, x) -> acc = acc + 1);
    }

    class Node<E> implements Result<E> {

        private static final Result<?> EMPTY = new Node<>(Array.empty());

        Array<Result<E>> branches;

        private Node(Array<Result<E>> branches) {
            this.branches = branches;
        }

        @Override
        public boolean isEmpty() {
            return branches.isEmpty();
        }

        public boolean isLeaf() {
            return false;
        }

        @Override
        public Option<E> get() {
            return Nothing();
        }

        @Override
        public Array<Result<E>> branches() {
            return branches;
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
        public Array<Result<E>> branches() {
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
