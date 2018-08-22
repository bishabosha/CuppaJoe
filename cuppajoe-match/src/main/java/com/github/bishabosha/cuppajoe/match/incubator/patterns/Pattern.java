package com.github.bishabosha.cuppajoe.match.incubator.patterns;

public abstract class Pattern<T> {

    public interface PatternVisitor {
        <O> void onEmpty(Empty<O> empty);
        <O> void onValue(Value<O> value);
        <O, Z> void onBranch1(Branch1<O, Z> branch);
        <O, Y, Z> void onBranch2(Branch2<O, Y, Z> branch);
    }

    private Pattern() {
    }

    public abstract void accept(PatternVisitor visitor);

    public static abstract class Leaf<T> extends Pattern<T> {
        public abstract boolean matches(T value);
    }

    public static abstract class Empty<T> extends Leaf<T> {
        @Override
        public void accept(PatternVisitor visitor) {
            visitor.onEmpty(this);
        }
    }

    public static abstract class Value<T> extends Leaf<T> {
        @Override
        public void accept(PatternVisitor visitor) {
            visitor.onValue(this);
        }
    }

    public static abstract class Branch<T> extends Pattern<T> {
        public abstract boolean canBranch(T value);
    }

    public static abstract class Branch1<T, A> extends Branch<T> {
        private final Pattern<A> branch1;

        protected Branch1(Pattern<A> branch1) {
            this.branch1 = branch1;
        }

        public Pattern<A> branch1() {
            return branch1;
        }

        public abstract A extract1(T value);

        @Override
        public void accept(PatternVisitor visitor) {
            visitor.onBranch1(this);
        }
    }

    public static abstract class Branch2<T, A, B> extends Branch1<T, A> {
        private final Pattern<B> branch2;

        protected Branch2(Pattern<A> branch1, Pattern<B> branch2) {
            super(branch1);
            this.branch2 = branch2;
        }

        public Pattern<B> branch2() {
            return branch2;
        }

        public abstract B extract2(T value);

        @Override
        public void accept(PatternVisitor visitor) {
            visitor.onBranch2(this);
        }
    }
}
