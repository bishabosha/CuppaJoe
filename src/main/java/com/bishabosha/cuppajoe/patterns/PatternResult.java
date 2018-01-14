package com.bishabosha.cuppajoe.patterns;

import com.bishabosha.cuppajoe.Foldable;
import com.bishabosha.cuppajoe.Iterables;
import com.bishabosha.cuppajoe.collections.immutable.Array;
import com.bishabosha.cuppajoe.collections.immutable.List;
import com.bishabosha.cuppajoe.control.Option;
import com.bishabosha.cuppajoe.functions.*;
import com.bishabosha.cuppajoe.tuples.Product2;
import org.jetbrains.annotations.NotNull;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Objects;

import static com.bishabosha.cuppajoe.API.*;

public interface PatternResult<E> extends Iterable<E> {

    static <E> PatternResult<E> compose(PatternResult<E>... results) {
        return results == null ? new Leaf<>(null) : new Node<>(Array.of(results));
    }

    static <E> Func2<PatternResult<E>, PatternResult<E>, PatternResult<E>> compose2() {
        return (a, b) -> new Node<>(Array.of(a, b));
    }

    static <E> Func3<PatternResult<E>, PatternResult<E>, PatternResult<E>, PatternResult<E>> compose3() {
        return (a, b, c) -> new Node<>(Array.of(a, b, c));
    }

    static <E> Func4<PatternResult<E>, PatternResult<E>, PatternResult<E>, PatternResult<E>, PatternResult<E>> compose4() {
        return (a, b, c, d) -> new Node<>(Array.of(a, b, c, d));
    }

    static <E> Func5<PatternResult<E>, PatternResult<E>, PatternResult<E>, PatternResult<E>, PatternResult<E>, PatternResult<E>> compose5() {
        return (a, b, c, d, e) -> new Node<>(Array.of(a, b, c, d, e));
    }

    static <E> Func6<PatternResult<E>, PatternResult<E>, PatternResult<E>, PatternResult<E>, PatternResult<E>, PatternResult<E>, PatternResult<E>> compose6() {
        return (a, b, c, d, e, f) -> new Node<>(Array.of(a, b, c, d, e, f));
    }

    static <E> Func7<PatternResult<E>, PatternResult<E>, PatternResult<E>, PatternResult<E>, PatternResult<E>, PatternResult<E>, PatternResult<E>, PatternResult<E>> compose7() {
        return (a, b, c, d, e, f, g) -> new Node<>(Array.of(a, b, c, d, e, f, g));
    }

    static <E> Func8<PatternResult<E>, PatternResult<E>, PatternResult<E>, PatternResult<E>, PatternResult<E>, PatternResult<E>, PatternResult<E>, PatternResult<E>, PatternResult<E>> compose8() {
        return (a, b, c, d, e, f, g, h) -> new Node<>(Array.of(a, b, c, d, e, f, g, h));
    }

    @SafeVarargs
    static <E> PatternResult<E> of(E... values) {
        return values == null
            ? new Leaf<>(null)
            : values.length == 0
                ? empty()
                : new Node<>(Array.of(values).map(Leaf::new));
    }

    static <E> PatternResult<E> of(E value) {
        return new Leaf<>(value);
    }

    @SuppressWarnings("unchecked")
    static <E> PatternResult<E> empty() {
        return (PatternResult<E>) Node.EMPTY;
    }

    boolean isEmpty();

    boolean isLeaf();

    Option<E> get();

    Array<PatternResult<E>> branches();

    default Values values() {
        return new Values(iterator());
    }

    default int size() {
        return Foldable.foldOver(this, 0, (acc, x) -> acc = acc + 1);
    }

    class Node<E> implements PatternResult<E> {

        private static final PatternResult<?> EMPTY = new Node<>(Array.empty());

        Array<PatternResult<E>> branches;

        private Node(Array<PatternResult<E>> branches) {
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
        public Array<PatternResult<E>> branches() {
            return branches;
        }

        @NotNull
        @Override
        public Iterator<E> iterator() {
            return new Iterables.Lockable<>() {
                private List<Iterator<PatternResult<E>>> stack = isEmpty() ? List() : List(Node.this.branches().iterator());
                private Option<E> toReturn;

                @Override
                public boolean hasNextSupplier() {
                    final Product2<Option<E>, List<Iterator<PatternResult<E>>>> nextItem;
                    nextItem = stack.nextItem((it, xs) -> {
                        if (it.hasNext()) {
                            PatternResult<E> tree = it.next();
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
            return obj == this || obj instanceof PatternResult && Iterables.equals(this, obj);
        }

        @Override
        public String toString() {
            return Iterables.toString('[', ']', iterator());
        }
    }

    class Leaf<E> implements PatternResult<E> {

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
        public Array<PatternResult<E>> branches() {
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
            return obj == this || Option(obj)
                                    .cast(PatternResult.class)
                                    .map(x -> {
                                        Iterator other = x.iterator();
                                        if (other.hasNext() && Objects.equals(other.next(), value) && !other.hasNext()) {
                                            return true;
                                        }
                                        return false;
                                    })
                                    .orElse(false);
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
                throw new ClassCastException("Pattern Result elem("+count+") is not of the type requested.");
            }
        }
    }
}
