package com.github.bishabosha.cuppajoe.typeclass.monad;

import com.github.bishabosha.cuppajoe.control.Option;
import com.github.bishabosha.cuppajoe.functions.Func3;
import com.github.bishabosha.cuppajoe.functions.Func4;
import com.github.bishabosha.cuppajoe.functions.Func5;
import com.github.bishabosha.cuppajoe.functions.Func6;
import com.github.bishabosha.cuppajoe.functions.Func7;
import com.github.bishabosha.cuppajoe.functions.Func8;
import org.junit.jupiter.api.Test;

import java.util.function.BiFunction;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.UnaryOperator;

import static com.github.bishabosha.cuppajoe.API.Some;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class Monad1Test {

    @Test
    public void test_liftMOp1() {
        UnaryOperator<Option<Integer>> funcLift = Monad1.liftMOp1(x -> x + 1);
        assertEquals(Some(2), funcLift.apply(Some(1)));
    }

    @Test
    public void test_liftMOp2() {
        BinaryOperator<Option<Integer>> funcLift = Monad1.liftMOp2((x, y) -> x + y);
        assertEquals(Some(3), funcLift.apply(Some(1), Some(2)));
    }

    @Test
    public void test_liftMFunc1() {
        Function<Option<Integer>, Option<String>> funcLift = Monad1.liftMFunc1(x -> String.valueOf(x));
        assertEquals(Some("1"), funcLift.apply(Some(1)));
    }

    @Test
    public void test_liftMFunc2() {
        BiFunction<Option<Integer>, Option<Integer>, Option<String>> funcLift = Monad1.liftMFunc2((x, y) -> String.valueOf(x + y));
        assertEquals(Some("2"), funcLift.apply(Some(1), Some(1)));
    }

    @Test
    public void test_liftMFunc3() {
        Func3<Option<Integer>, Option<Integer>, Option<Integer>, Option<String>> funcLift = Monad1.liftMFunc3((x, y, z) -> String.valueOf(x + y + z));
        assertEquals(Some("3"), funcLift.apply(Some(1), Some(1), Some(1)));
    }

    @Test
    public void test_liftMFunc4() {
        Func4<Option<Integer>, Option<Integer>, Option<Integer>, Option<Integer>, Option<String>> funcLift = Monad1.liftMFunc4((w, x, y, z) -> String.valueOf(w + x + y + z));
        assertEquals(Some("4"), funcLift.apply(Some(1), Some(1), Some(1), Some(1)));
    }

    @Test
    public void test_liftMFunc5() {
        Func5<Option<Integer>, Option<Integer>, Option<Integer>, Option<Integer>, Option<Integer>, Option<String>> funcLift = Monad1.liftMFunc5((v, w, x, y, z) -> String.valueOf(v + w + x + y + z));
        assertEquals(Some("5"), funcLift.apply(Some(1), Some(1), Some(1), Some(1), Some(1)));
    }

    @Test
    public void test_liftMFunc6() {
        Func6<Option<Integer>, Option<Integer>, Option<Integer>, Option<Integer>, Option<Integer>, Option<Integer>, Option<String>> funcLift = Monad1.liftMFunc6((u, v, w, x, y, z) -> String.valueOf(u + v + w + x + y + z));
        assertEquals(Some("6"), funcLift.apply(Some(1), Some(1), Some(1), Some(1), Some(1), Some(1)));
    }

    @Test
    public void test_liftMFunc7() {
        Func7<Option<Integer>, Option<Integer>, Option<Integer>, Option<Integer>, Option<Integer>, Option<Integer>, Option<Integer>, Option<String>> funcLift = Monad1.liftMFunc7((t, u, v, w, x, y, z) -> String.valueOf(t + u + v + w + x + y + z));
        assertEquals(Some("7"), funcLift.apply(Some(1), Some(1), Some(1), Some(1), Some(1), Some(1), Some(1)));
    }

    @Test
    public void test_liftMFunc8() {
        Func8<Option<Integer>, Option<Integer>, Option<Integer>, Option<Integer>, Option<Integer>, Option<Integer>, Option<Integer>, Option<Integer>, Option<String>> funcLift = Monad1.liftMFunc8((s, t, u, v, w, x, y, z) -> String.valueOf(s + t + u + v + w + x + y + z));
        assertEquals(Some("8"), funcLift.apply(Some(1), Some(1), Some(1), Some(1), Some(1), Some(1), Some(1), Some(1)));
    }

}