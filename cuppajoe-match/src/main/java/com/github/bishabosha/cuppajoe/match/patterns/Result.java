package com.github.bishabosha.cuppajoe.match.patterns;

public abstract class Result {

    private Result() {
    }

    public static Result empty() {
        return Empty.INSTANCE;
    }

    public static Result of(Object a) {
        return new Leaf(a);
    }

    public static Result compose(Result a, Result b) {
        return new Branch2(a, b);
    }

    public static Result compose(Result a, Result b, Result c) {
        return new Branch3(a, b, c);
    }

    public static Result compose(Result a, Result b, Result c, Result d) {
        return new Branch4(a, b, c, d);
    }

    public static Result compose(Result a, Result b, Result c, Result d, Result e) {
        return new Branch5(a, b, c, d, e);
    }

    public static Result compose(Result a, Result b, Result c, Result d, Result e, Result f) {
        return new Branch6(a, b, c, d, e, f);
    }

    public static Result compose(Result a, Result b, Result c, Result d, Result e, Result f, Result g) {
        return new Branch7(a, b, c, d, e, f, g);
    }

    public static Result compose(Result a, Result b, Result c, Result d, Result e, Result f, Result g, Result h) {
        return new Branch8(a, b, c, d, e, f, g, h);
    }

    public abstract void accept(ResultVisitor resultVisitor);

    public static class Empty extends Result {
        private static final Empty INSTANCE = new Empty();

        @Override
        public void accept(ResultVisitor resultVisitor) {
        }

        @Override
        public String toString() {
            return "*";
        }
    }

    public static class Leaf extends Result {
        private final Object val;

        private Leaf(Object val) {
            this.val = val;
        }

        @Override
        public void accept(ResultVisitor resultVisitor) {
            resultVisitor.onValue(val);
        }

        @Override
        public String toString() {
            return "<" + val + ">";
        }
    }

    public static class Branch2 extends Result {
        private final Result b1;
        private final Result b2;

        public Branch2(Result b1, Result b2) {
            this.b1 = b1;
            this.b2 = b2;
        }

        @Override
        public void accept(ResultVisitor resultVisitor) {
            if (resultVisitor.uninitialised()) {
                b1.accept(resultVisitor);
                if (resultVisitor.uninitialised()) {
                    b2.accept(resultVisitor);
                }
            }
        }

        @Override
        public String toString() {
            return "{" + b1 + ", " + b2 + "}";
        }
    }

    public static class Branch3 extends Result {
        private final Result b1;
        private final Result b2;
        private final Result b3;

        public Branch3(Result b1, Result b2, Result b3) {
            this.b1 = b1;
            this.b2 = b2;
            this.b3 = b3;
        }

        @Override
        public void accept(ResultVisitor resultVisitor) {
            if (resultVisitor.uninitialised()) {
                b1.accept(resultVisitor);
                if (resultVisitor.uninitialised()) {
                    b2.accept(resultVisitor);
                    if (resultVisitor.uninitialised()) {
                        b3.accept(resultVisitor);
                    }
                }
            }
        }

        @Override
        public String toString() {
            return "{" + b1 + ", " + b2 + ", " + b3 + "}";
        }
    }

    public static class Branch4 extends Result {
        private final Result b1;
        private final Result b2;
        private final Result b3;
        private final Result b4;

        public Branch4(Result b1, Result b2, Result b3, Result b4) {
            this.b1 = b1;
            this.b2 = b2;
            this.b3 = b3;
            this.b4 = b4;
        }

        @Override
        public void accept(ResultVisitor resultVisitor) {
            if (resultVisitor.uninitialised()) {
                b1.accept(resultVisitor);
                if (resultVisitor.uninitialised()) {
                    b2.accept(resultVisitor);
                    if (resultVisitor.uninitialised()) {
                        b3.accept(resultVisitor);
                        if (resultVisitor.uninitialised()) {
                            b4.accept(resultVisitor);
                        }
                    }
                }
            }
        }

        @Override
        public String toString() {
            return "{" + b1 + ", " + b2 + ", " + b3 + ", " + b4 + "}";
        }
    }

    public static class Branch5 extends Result {
        private final Result b1;
        private final Result b2;
        private final Result b3;
        private final Result b4;
        private final Result b5;

        public Branch5(Result b1, Result b2, Result b3, Result b4, Result b5) {
            this.b1 = b1;
            this.b2 = b2;
            this.b3 = b3;
            this.b4 = b4;
            this.b5 = b5;
        }

        @Override
        public void accept(ResultVisitor resultVisitor) {
            if (resultVisitor.uninitialised()) {
                b1.accept(resultVisitor);
                if (resultVisitor.uninitialised()) {
                    b2.accept(resultVisitor);
                    if (resultVisitor.uninitialised()) {
                        b3.accept(resultVisitor);
                        if (resultVisitor.uninitialised()) {
                            b4.accept(resultVisitor);
                            if (resultVisitor.uninitialised()) {
                                b5.accept(resultVisitor);
                            }
                        }
                    }
                }
            }
        }

        @Override
        public String toString() {
            return "{" + b1 + ", " + b2 + ", " + b3 + ", " + b4 + ", " + b5 + "}";
        }
    }

    public static class Branch6 extends Result {
        private final Result b1;
        private final Result b2;
        private final Result b3;
        private final Result b4;
        private final Result b5;
        private final Result b6;

        public Branch6(Result b1, Result b2, Result b3, Result b4, Result b5, Result b6) {
            this.b1 = b1;
            this.b2 = b2;
            this.b3 = b3;
            this.b4 = b4;
            this.b5 = b5;
            this.b6 = b6;
        }

        @Override
        public void accept(ResultVisitor resultVisitor) {
            if (resultVisitor.uninitialised()) {
                b1.accept(resultVisitor);
                if (resultVisitor.uninitialised()) {
                    b2.accept(resultVisitor);
                    if (resultVisitor.uninitialised()) {
                        b3.accept(resultVisitor);
                        if (resultVisitor.uninitialised()) {
                            b4.accept(resultVisitor);
                            if (resultVisitor.uninitialised()) {
                                b5.accept(resultVisitor);
                                if (resultVisitor.uninitialised()) {
                                    b6.accept(resultVisitor);
                                }
                            }
                        }
                    }
                }
            }
        }

        @Override
        public String toString() {
            return "{" + b1 + ", " + b2 + ", " + b3 + ", " + b4 + ", " + b5 + ", " + b6 + "}";
        }
    }

    public static class Branch7 extends Result {
        private final Result b1;
        private final Result b2;
        private final Result b3;
        private final Result b4;
        private final Result b5;
        private final Result b6;
        private final Result b7;

        public Branch7(Result b1, Result b2, Result b3, Result b4, Result b5, Result b6, Result b7) {
            this.b1 = b1;
            this.b2 = b2;
            this.b3 = b3;
            this.b4 = b4;
            this.b5 = b5;
            this.b6 = b6;
            this.b7 = b7;
        }

        @Override
        public void accept(ResultVisitor resultVisitor) {
            if (resultVisitor.uninitialised()) {
                b1.accept(resultVisitor);
                if (resultVisitor.uninitialised()) {
                    b2.accept(resultVisitor);
                    if (resultVisitor.uninitialised()) {
                        b3.accept(resultVisitor);
                        if (resultVisitor.uninitialised()) {
                            b4.accept(resultVisitor);
                            if (resultVisitor.uninitialised()) {
                                b5.accept(resultVisitor);
                                if (resultVisitor.uninitialised()) {
                                    b6.accept(resultVisitor);
                                    if (resultVisitor.uninitialised()) {
                                        b7.accept(resultVisitor);
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        @Override
        public String toString() {
            return "{" + b1 + ", " + b2 + ", " + b3 + ", " + b4 + ", " + b5 + ", " + b6 + ", " + b7 + "}";
        }
    }

    public static class Branch8 extends Result {
        private final Result b1;
        private final Result b2;
        private final Result b3;
        private final Result b4;
        private final Result b5;
        private final Result b6;
        private final Result b7;
        private final Result b8;

        public Branch8(Result b1, Result b2, Result b3, Result b4, Result b5, Result b6, Result b7, Result b8) {
            this.b1 = b1;
            this.b2 = b2;
            this.b3 = b3;
            this.b4 = b4;
            this.b5 = b5;
            this.b6 = b6;
            this.b7 = b7;
            this.b8 = b8;
        }

        @Override
        public void accept(ResultVisitor resultVisitor) {
            if (resultVisitor.uninitialised()) {
                b1.accept(resultVisitor);
                if (resultVisitor.uninitialised()) {
                    b2.accept(resultVisitor);
                    if (resultVisitor.uninitialised()) {
                        b3.accept(resultVisitor);
                        if (resultVisitor.uninitialised()) {
                            b4.accept(resultVisitor);
                            if (resultVisitor.uninitialised()) {
                                b5.accept(resultVisitor);
                                if (resultVisitor.uninitialised()) {
                                    b6.accept(resultVisitor);
                                    if (resultVisitor.uninitialised()) {
                                        b7.accept(resultVisitor);
                                        if (resultVisitor.uninitialised()) {
                                            b8.accept(resultVisitor);
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        @Override
        public String toString() {
            return "{" + b1 + ", " + b2 + ", " + b3 + ", " + b4 + ", " + b5 + ", " + b6 + ", " + b7 + ", " + b8 + "}";
        }
    }
}
