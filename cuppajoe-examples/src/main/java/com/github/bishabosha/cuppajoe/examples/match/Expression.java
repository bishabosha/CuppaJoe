package com.github.bishabosha.cuppajoe.examples.match;

import com.github.bishabosha.cuppajoe.match.patterns.Pattern;
import com.github.bishabosha.cuppajoe.match.patterns.Pattern.Branch;

import static com.github.bishabosha.cuppajoe.collections.immutable.API.Tuple;
import static com.github.bishabosha.cuppajoe.match.patterns.Pattern.branch1;
import static com.github.bishabosha.cuppajoe.match.patterns.Pattern.branchN;

public abstract class Expression {
    private Expression() {
    }

    public static Branch<Expression> val(Pattern<Integer> value) {
        return branch1(Val.class, value, x -> x.value);
    }

    public static Branch<Expression> add(Pattern<Expression> left, Pattern<Expression> right) {
        return branchN(
            Add.class,
            Tuple(left, x -> x.left),
            Tuple(right, x -> x.right)
        );
    }

    public static Branch<Expression> mul(Pattern<Expression> left, Pattern<Expression> right) {
        return branchN(
            Mul.class,
            Tuple(left, x -> x.left),
            Tuple(right, x -> x.right)
        );
    }

    public static Branch<Expression> div(Pattern<Expression> left, Pattern<Expression> right) {
        return branchN(
            Div.class,
            Tuple(left, x -> x.left),
            Tuple(right, x -> x.right)
        );
    }

    public static Branch<Expression> neg(Pattern<Expression> expression) {
        return branch1(Neg.class, expression, x -> x.expression);
    }

    public static Val Val(int value) {
        return new Val(value);
    }

    public static Add Add(Expression left, Expression right) {
        return new Add(left, right);
    }

    public static Mul Mul(Expression left, Expression right) {
        return new Mul(left, right);
    }

    public static Div Div(Expression left, Expression right) {
        return new Div(left, right);
    }

    public static Neg Neg(Expression expression) {
        return new Neg(expression);
    }

    public static final class Val extends Expression {
        private final int value;

        public Val(int value) {
            this.value = value;
        }
    }

    public static final class Add extends Expression {
        private final Expression left;
        private final Expression right;

        public Add(Expression left, Expression right) {
            this.left = left;
            this.right = right;
        }
    }

    public static final class Mul extends Expression {
        private final Expression left;
        private final Expression right;

        public Mul(Expression left, Expression right) {
            this.left = left;
            this.right = right;
        }
    }

    public static final class Div extends Expression {
        private final Expression left;
        private final Expression right;

        public Div(Expression left, Expression right) {
            this.left = left;
            this.right = right;
        }
    }

    public static final class Neg extends Expression {
        private final Expression expression;

        public Neg(Expression expression) {
            this.expression = expression;
        }
    }
}
