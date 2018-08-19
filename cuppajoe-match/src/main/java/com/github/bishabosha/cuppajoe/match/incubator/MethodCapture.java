package com.github.bishabosha.cuppajoe.match.incubator;

import com.github.bishabosha.cuppajoe.higher.functions.Func1;
import com.github.bishabosha.cuppajoe.higher.functions.Func2;

public abstract class MethodCapture<R> {

    private MethodCapture() {
    }

    public interface MethodCaptureVisitor {
        <R> void accept(MethodCapture<R> methodCapture);
    }

    public abstract R invoke();

    public abstract void capture(Object a);

    public abstract boolean uninitialised();

    public static class M1<A, R> extends MethodCapture<R> {
        private A obj;
        private final Func1<A, R> func1;
        private boolean uninitialised = true;

        public M1(Func1<A, R> func1) {
            this.func1 = func1;
        }

        @SuppressWarnings("unchecked")
        @Override
        public void capture(Object a) {
            obj = (A) a;
            uninitialised = false;
        }

        @Override
        public boolean uninitialised() {
            return uninitialised;
        }

        @Override
        public R invoke() {
            if (uninitialised) {
                throw new IllegalStateException();
            }
            return func1.apply(obj);
        }
    }

    private static abstract class MArray<R> extends MethodCapture<R> {
        private final int size;
        final Object[] arr;
        int cursor = 0;

        MArray(int size) {
            this.size = size;
            arr = new Object[size];
        }

        @Override
        public final void capture(Object a) {
            arr[cursor++] = a;
        }

        @Override
        public boolean uninitialised() {
            return cursor < size;
        }
    }

    public static class M2<A, B, R> extends MArray<R> {
        private final Func2<A, B, R> func2;

        public M2(Func2<A, B, R> func2) {
            super(2);
            this.func2 = func2;
        }

        @SuppressWarnings("unchecked")
        @Override
        public R invoke() {
            if (uninitialised()) {
                throw new IllegalStateException();
            }
            return func2.apply((A)arr[0], (B)arr[1]);
        }
    }
}
