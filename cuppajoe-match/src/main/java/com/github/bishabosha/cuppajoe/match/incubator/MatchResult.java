package com.github.bishabosha.cuppajoe.match.incubator;

//import com.github.bishabosha.cuppajoe.match.incubator.MethodCapture.MethodCaptureVisitor;

import com.github.bishabosha.cuppajoe.match.incubator.MethodCapture.MethodCaptureVisitor;

public abstract class MatchResult implements MethodCaptureVisitor {

    private MatchResult() {
    }

    public static MatchResult empty() {
        return Empty.INSTANCE;
    }

    public static MatchResult of(Object a) {
        return new Leaf(a);
    }

//    public static MatchResult compose(MatchResult... results) {
//        return new Branch(results);
//    }

    public static MatchResult compose(MatchResult a, MatchResult b) {
        return new Branch2(a, b);
    }

    public static class Empty extends MatchResult {
        private static final Empty INSTANCE = new Empty();

        @Override
        public <R> void accept(MethodCapture<R> methodCapture) {
        }

        @Override
        public String toString() {
            return "*";
        }
    }

    public static class Leaf extends MatchResult {
        private final Object val;

        private Leaf(Object val) {
            this.val = val;
        }

        @Override
        public <R> void accept(MethodCapture<R> methodCapture) {
            methodCapture.capture(val);
        }

        @Override
        public String toString() {
            return "<" + val + ">";
        }
    }

    public static class Branch2 extends MatchResult {
        private final MatchResult b1;
        private final MatchResult b2;

        public Branch2(MatchResult b1, MatchResult b2) {
            this.b1 = b1;
            this.b2 = b2;
        }

        @Override
        public <R> void accept(MethodCapture<R> methodCapture) {
            if (methodCapture.uninitialised()) {
                b1.accept(methodCapture);
                if (methodCapture.uninitialised()) {
                    b2.accept(methodCapture);
                }
            }
        }

        @Override
        public String toString() {
            return "{" + b1 + ", " + b2 + "}";
        }
    }
}
