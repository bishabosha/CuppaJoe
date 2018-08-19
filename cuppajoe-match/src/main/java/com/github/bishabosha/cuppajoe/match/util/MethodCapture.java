package com.github.bishabosha.cuppajoe.match.util;

import com.github.bishabosha.cuppajoe.higher.functions.*;
import com.github.bishabosha.cuppajoe.match.patterns.ResultVisitor;

public abstract class MethodCapture<R> extends ResultVisitor {

    private MethodCapture() {
    }

    public abstract R invoke();

    public static final class M1<A, R> extends MethodCapture<R> {
        private A obj;
        private final Func1<A, R> func;
        private boolean uninitialised = true;

        public M1(Func1<A, R> func) {
            this.func = func;
        }

        @SuppressWarnings("unchecked")
        @Override
        public final void onValue(Object a) {
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
            return func.apply(obj);
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
        public final void onValue(Object a) {
            arr[cursor++] = a;
        }

        @Override
        public final boolean uninitialised() {
            return cursor < size;
        }
    }

    public static final class M2<A, B, R> extends MArray<R> {
        private final Func2<A, B, R> func;

        public M2(Func2<A, B, R> func) {
            super(2);
            this.func = func;
        }

        @SuppressWarnings("unchecked")
        @Override
        public R invoke() {
            if (uninitialised()) {
                throw new IllegalStateException();
            }
            return func.apply((A)arr[0], (B)arr[1]);
        }
    }

    public static final class M3<A, B, C, R> extends MArray<R> {
        private final Func3<A, B, C, R> func;

        public M3(Func3<A, B, C, R> func) {
            super(3);
            this.func = func;
        }

        @SuppressWarnings("unchecked")
        @Override
        public R invoke() {
            if (uninitialised()) {
                throw new IllegalStateException();
            }
            return func.apply((A)arr[0], (B)arr[1], (C)arr[2]);
        }
    }

    public static final class M4<A, B, C, D, R> extends MArray<R> {
        private final Func4<A, B, C, D, R> func;

        public M4(Func4<A, B, C, D, R> func) {
            super(4);
            this.func = func;
        }

        @SuppressWarnings("unchecked")
        @Override
        public R invoke() {
            if (uninitialised()) {
                throw new IllegalStateException();
            }
            return func.apply((A)arr[0], (B)arr[1], (C)arr[2], (D)arr[3]);
        }
    }

    public static final class M5<A, B, C, D, E, R> extends MArray<R> {
        private final Func5<A, B, C, D, E, R> func;

        public M5(Func5<A, B, C, D, E, R> func) {
            super(5);
            this.func = func;
        }

        @SuppressWarnings("unchecked")
        @Override
        public R invoke() {
            if (uninitialised()) {
                throw new IllegalStateException();
            }
            return func.apply((A)arr[0], (B)arr[1], (C)arr[2], (D)arr[3], (E)arr[4]);
        }
    }

    public static final class M6<A, B, C, D, E, F, R> extends MArray<R> {
        private final Func6<A, B, C, D, E, F, R> func;

        public M6(Func6<A, B, C, D, E, F, R> func) {
            super(6);
            this.func = func;
        }

        @SuppressWarnings("unchecked")
        @Override
        public R invoke() {
            if (uninitialised()) {
                throw new IllegalStateException();
            }
            return func.apply((A)arr[0], (B)arr[1], (C)arr[2], (D)arr[3], (E)arr[4], (F)arr[5]);
        }
    }

    public static final class M7<A, B, C, D, E, F, G, R> extends MArray<R> {
        private final Func7<A, B, C, D, E, F, G, R> func;

        public M7(Func7<A, B, C, D, E, F, G, R> func) {
            super(7);
            this.func = func;
        }

        @SuppressWarnings("unchecked")
        @Override
        public R invoke() {
            if (uninitialised()) {
                throw new IllegalStateException();
            }
            return func.apply((A)arr[0], (B)arr[1], (C)arr[2], (D)arr[3], (E)arr[4], (F)arr[5], (G)arr[6]);
        }
    }

    public static final class M8<A, B, C, D, E, F, G, H, R> extends MArray<R> {
        private final Func8<A, B, C, D, E, F, G, H, R> func;

        public M8(Func8<A, B, C, D, E, F, G, H, R> func) {
            super(8);
            this.func = func;
        }

        @SuppressWarnings("unchecked")
        @Override
        public R invoke() {
            if (uninitialised()) {
                throw new IllegalStateException();
            }
            return func.apply((A)arr[0], (B)arr[1], (C)arr[2], (D)arr[3], (E)arr[4], (F)arr[5], (G)arr[6], (H)arr[7]);
        }
    }
}
