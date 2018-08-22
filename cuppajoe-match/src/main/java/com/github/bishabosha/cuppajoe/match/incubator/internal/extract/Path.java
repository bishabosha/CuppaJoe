package com.github.bishabosha.cuppajoe.match.incubator.internal.extract;

@FunctionalInterface
public interface Path<I, O> {
    O get(I source);

    static <I> Path<I, I> identity() {
        return x -> x;
    }

    @SuppressWarnings("unchecked")
    static <I, O> CompletedPath<I, O> identityCompletedPath() {
        return x -> (O) x;
    }

    static <I, O> CompletedPath<I, O> complete(Path<I, O> path) {
        return new DeferredCompletedPath<>(path);
    }

    default boolean isComplete() {
        return false;
    }

    default <U> Path<I, U> then(Path<O, U> next) {
        return i -> next.get(get(i));
    }

    interface CompletedPath<I, O> extends Path<I, O> {
        @Override
        default boolean isComplete() {
            return true;
        }
    }

    final class DeferredCompletedPath<I, O> implements CompletedPath<I, O> {
        private final Path<I, O> path;

        private DeferredCompletedPath(Path<I, O> path) {
            this.path = path;
        }

        @Override
        public O get(I source) {
            return path.get(source);
        }
    }
}
