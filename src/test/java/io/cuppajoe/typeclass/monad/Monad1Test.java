package io.cuppajoe.typeclass.monad;

import io.cuppajoe.control.Option;
import io.cuppajoe.functions.*;
import org.junit.Test;

import java.util.function.BiFunction;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.UnaryOperator;

import static io.cuppajoe.API.Some;
import static org.junit.Assert.*;

public class Monad1Test {

    @Test
    public void liftMOp1() {
        UnaryOperator<Option<Integer>> funcLift = Monad1.liftMOp1(x -> x + 1);
        assertEquals(Some(2), funcLift.apply(Some(1)));
    }

    @Test
    public void liftMOp2() {
        BinaryOperator<Option<Integer>> funcLift = Monad1.liftMOp2((x, y) -> x + y);
        assertEquals(Some(3), funcLift.apply(Some(1), Some(2)));
    }

    @Test
    public void liftMFunc1() {
        Function<Option<Integer>, Option<String>> funcLift = Monad1.liftMFunc1(x -> String.valueOf(x));
        assertEquals(Some("1"), funcLift.apply(Some(1)));
    }

    @Test
    public void liftMFunc2() {
        BiFunction<Option<Integer>, Option<Integer>, Option<String>> funcLift = Monad1.liftMFunc2((x, y) -> String.valueOf(x + y));
        assertEquals(Some("2"), funcLift.apply(Some(1), Some(1)));
    }

    @Test
    public void liftMFunc3() {
        Func3<Option<Integer>, Option<Integer>, Option<Integer>, Option<String>> funcLift = Monad1.liftMFunc3((x, y, z) -> String.valueOf(x + y + z));
        assertEquals(Some("3"), funcLift.apply(Some(1), Some(1), Some(1)));
    }

    @Test
    public void liftMFunc4() {
        Func4<Option<Integer>, Option<Integer>, Option<Integer>, Option<Integer>, Option<String>> funcLift = Monad1.liftMFunc4((w, x, y, z) -> String.valueOf(w + x + y + z));
        assertEquals(Some("4"), funcLift.apply(Some(1), Some(1), Some(1), Some(1)));
    }

    @Test
    public void liftMFunc5() {
        Func5<Option<Integer>, Option<Integer>, Option<Integer>, Option<Integer>, Option<Integer>, Option<String>> funcLift = Monad1.liftMFunc5((v, w, x, y, z) -> String.valueOf(v + w + x + y + z));
        assertEquals(Some("5"), funcLift.apply(Some(1), Some(1), Some(1), Some(1), Some(1)));
    }

    @Test
    public void liftMFunc6() {
        Func6<Option<Integer>, Option<Integer>, Option<Integer>, Option<Integer>, Option<Integer>, Option<Integer>, Option<String>> funcLift = Monad1.liftMFunc6((u, v, w, x, y, z) -> String.valueOf(u + v + w + x + y + z));
        assertEquals(Some("6"), funcLift.apply(Some(1), Some(1), Some(1), Some(1), Some(1), Some(1)));
    }

    @Test
    public void liftMFunc7() {
        Func7<Option<Integer>, Option<Integer>, Option<Integer>, Option<Integer>, Option<Integer>, Option<Integer>, Option<Integer>, Option<String>> funcLift = Monad1.liftMFunc7((t, u, v, w, x, y, z) -> String.valueOf(t + u + v + w + x + y + z));
        assertEquals(Some("7"), funcLift.apply(Some(1), Some(1), Some(1), Some(1), Some(1), Some(1), Some(1)));
    }

    @Test
    public void liftMFunc8() {
        Func8<Option<Integer>, Option<Integer>, Option<Integer>, Option<Integer>, Option<Integer>, Option<Integer>, Option<Integer>, Option<Integer>, Option<String>> funcLift = Monad1.liftMFunc8((s, t, u, v, w, x, y, z) -> String.valueOf(s + t + u + v + w + x + y + z));
        assertEquals(Some("8"), funcLift.apply(Some(1), Some(1), Some(1), Some(1), Some(1), Some(1), Some(1), Some(1)));
    }

}