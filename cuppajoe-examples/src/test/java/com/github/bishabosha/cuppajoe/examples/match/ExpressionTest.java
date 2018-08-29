package com.github.bishabosha.cuppajoe.examples.match;

import com.github.bishabosha.cuppajoe.collections.immutable.tuples.Tuple;
import com.github.bishabosha.cuppajoe.collections.immutable.tuples.Tuple2;
import com.github.bishabosha.cuppajoe.match.cases.Case;
import org.junit.jupiter.api.Test;

import static com.github.bishabosha.cuppajoe.examples.match.Expression.*;
import static com.github.bishabosha.cuppajoe.match.API.*;
import static com.github.bishabosha.cuppajoe.match.patterns.Collections.tuple;
import static com.github.bishabosha.cuppajoe.match.patterns.Standard.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class ExpressionTest {

    private static int eval(Expression expression) {
        return evalCases.get(expression);
    }

    private static Expression fibonacci(Expression expression) {
        return fibonacciCases.get(expression);
    }

    private static Expression reduce(Expression expression) {
        return Val(eval(expression));
    }

    private static String toString(Expression expression) {
        return toStringCases.get(expression);
    }

    private static boolean lower(Class left, Class right) {
        return lowerCases.get(Tuple.of(left, right));
    }

    private static final Case<Expression, Integer> evalCases = MatchUnsafe(m -> m
        .with(val(id()))
            .then((Integer x) -> x)
        .with(add(id(), id()))
            .then((Expression left, Expression right) -> eval(left) + eval(right))
        .with(mul(id(), id()))
            .then((Expression left, Expression right) -> eval(left) * eval(right))
        .with(div(id(), id()))
            .then((Expression left, Expression right) -> eval(left) / eval(right))
        .with(neg(id()))
            .then((Expression exp) -> -eval(exp))
    );

    private static final Case<Expression, Expression> fibonacciCases = Match(m -> m
        .with(val(eq(0, 1)))
            .then(Expression::Val)
        .def((Expression exp) -> Add(fibonacci(reduce(Add(Neg(Val(2)), exp))), fibonacci(reduce(Add(Neg(Val(1)), exp)))))
    );

    private static final Case<Expression, String> toStringCases = MatchUnsafe(m -> m
        .with(val(id()))
            .then((Integer x) -> Integer.toString(x))
        .with(add(id(), id()))
            .then((Expression left, Expression right) -> toString(left) + " + " + toString(right))
        .with(mul(id(), id()))
            .then((Expression left, Expression right) -> bracketExp(Mul.class, " * ", left, right))
        .with(div(id(), id()))
            .then((Expression left, Expression right) -> bracketExp(Div.class, " / ", left, right))
        .with(neg(id()))
            .then((Expression exp) -> lower(Neg.class, exp.getClass()) ? "-" + bracketExp(exp) : "-" + toString(exp))
    );

    private static final Case<Tuple2<Class, Class>, Boolean> lowerCases = Match(m -> m
        .with(tuple(is(Add.class), any(is(Mul.class), is(Div.class))))
            .then(true)
        .with(tuple(is(Neg.class), any(is(Add.class), is(Mul.class), is(Div.class))))
            .then(true)
        .def(false)
    );

    private static String bracketExp(Class op, String opStr, Expression left, Expression right) {
        return bracketExp(op, left) + opStr + bracketExp(op, right);
    }

    private static String bracketExp(Class op, Expression exp) {
        return (lower(exp.getClass(), op) ? bracketExp(exp) : toString(exp));
    }

    private static String bracketExp(Expression exp) {
        return "(" + toString(exp) + ")";
    }

    @Test
    public void test_Eval() {
        assertEquals(1, eval(Val(1)));
        assertEquals(2, eval(Add(Val(1), Val(1))));
        assertEquals(6, eval(Mul(Val(2), Val(3))));
        assertEquals(2, eval(Div(Val(10), Val(5))));
        assertEquals(5, eval(Div(Add(Val(10), Val(5)), Val(3))));
        assertEquals(10, eval(Neg(Neg(Val(10)))));
    }

    @Test
    public void test_toString() {
        assertEquals("1", toString(Val(1)));
        assertEquals("1 + 1", toString(Add(Val(1), Val(1))));
        assertEquals("2 * 3", toString(Mul(Val(2), Val(3))));
        assertEquals("10 / 5", toString(Div(Val(10), Val(5))));
        assertEquals("(10 + 5) / 3", toString(Div(Add(Val(10), Val(5)), Val(3))));
        assertEquals("--10", toString(Neg(Neg(Val(10)))));
        assertEquals("-(1 + -2)", toString(Neg(Add(Val(1), Neg(Val(2))))));
    }

    @Test
    public void test_fibonacci() {
        assertEquals(1, eval(fibonacci(Val(1))));
        assertEquals(1, eval(fibonacci(Val(2))));
        assertEquals(2, eval(fibonacci(Val(3))));
        assertEquals(3, eval(fibonacci(Val(4))));
        assertEquals(5, eval(fibonacci(Val(5))));
        assertEquals(5, eval(fibonacci(Add(Val(2), Val(3)))));
        assertEquals(832040, eval(fibonacci(Val(30))));
    }
}