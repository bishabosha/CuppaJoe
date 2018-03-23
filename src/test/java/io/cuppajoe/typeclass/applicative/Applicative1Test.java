package io.cuppajoe.typeclass.applicative;

import io.cuppajoe.collections.immutable.Array;
import io.cuppajoe.collections.immutable.List;
import io.cuppajoe.control.Option;
import io.cuppajoe.functions.*;
import org.junit.Test;

import java.util.function.BiFunction;
import java.util.function.Function;

import static io.cuppajoe.API.*;
import static org.junit.Assert.assertEquals;

public class Applicative1Test {

    @Test
    public void liftAFunc1() {
        Function<Option<Integer>, Option<String>> funcLift = Applicative1.liftAFunc1(x -> String.valueOf(x));
        assertEquals(Some("1"), funcLift.apply(Some(1)));
    }

    @Test
    public void liftAFunc2() {
        BiFunction<Option<Integer>, Option<Integer>, Option<String>> funcLift = Applicative1.liftAFunc2((x, y) -> String.valueOf(x + y));
        assertEquals(Some("2"), funcLift.apply(Some(1), Some(1)));
    }

    @Test
    public void liftAFunc3() {
        Func3<Option<Integer>, Option<Integer>, Option<Integer>, Option<String>> funcLift = Applicative1.liftAFunc3((x, y, z) -> String.valueOf(x + y + z));
        assertEquals(Some("3"), funcLift.apply(Some(1), Some(1), Some(1)));
    }

    @Test
    public void liftAFunc4() {
        Func4<Option<Integer>, Option<Integer>, Option<Integer>, Option<Integer>, Option<String>> funcLift = Applicative1.liftAFunc4((w, x, y, z) -> String.valueOf(w + x + y + z));
        assertEquals(Some("4"), funcLift.apply(Some(1), Some(1), Some(1), Some(1)));
    }

    @Test
    public void liftAFunc5() {
        Func5<Option<Integer>, Option<Integer>, Option<Integer>, Option<Integer>, Option<Integer>, Option<String>> funcLift = Applicative1.liftAFunc5((v, w, x, y, z) -> String.valueOf(v + w + x + y + z));
        assertEquals(Some("5"), funcLift.apply(Some(1), Some(1), Some(1), Some(1), Some(1)));
    }

    @Test
    public void liftAFunc6() {
        Func6<Option<Integer>, Option<Integer>, Option<Integer>, Option<Integer>, Option<Integer>, Option<Integer>, Option<String>> funcLift = Applicative1.liftAFunc6((u, v, w, x, y, z) -> String.valueOf(u + v + w + x + y + z));
        assertEquals(Some("6"), funcLift.apply(Some(1), Some(1), Some(1), Some(1), Some(1), Some(1)));
    }

    @Test
    public void liftAFunc7() {
        Func7<Option<Integer>, Option<Integer>, Option<Integer>, Option<Integer>, Option<Integer>, Option<Integer>, Option<Integer>, Option<String>> funcLift = Applicative1.liftAFunc7((t, u, v, w, x, y, z) -> String.valueOf(t + u + v + w + x + y + z));
        assertEquals(Some("7"), funcLift.apply(Some(1), Some(1), Some(1), Some(1), Some(1), Some(1), Some(1)));
    }

    @Test
    public void liftAFunc8() {
        Func8<Option<Integer>, Option<Integer>, Option<Integer>, Option<Integer>, Option<Integer>, Option<Integer>, Option<Integer>, Option<Integer>, Option<String>> funcLift = Applicative1.liftAFunc8((s, t, u, v, w, x, y, z) -> String.valueOf(s + t + u + v + w + x + y + z));
        assertEquals(Some("8"), funcLift.apply(Some(1), Some(1), Some(1), Some(1), Some(1), Some(1), Some(1), Some(1)));
    }

    @Test
    public void applyList() {
        List<Function<? super Integer, ? extends String>> funcs = List(
            x -> String.valueOf(x),
            x -> String.valueOf(x * x),
            x -> String.valueOf(x * x * x)
        );
        List<Function<? super Integer, ? extends String>> funcsNo = List();
        assertEquals(List("1", "2", "3", "1", "4", "9", "1", "8", "27"), List(1,2,3).apply(funcs));
        assertEquals(List(), List(1,2,3).apply(funcsNo));
    }

    @Test
    public void applyOption() {
        Option<Function<? super Integer, ? extends String>> func = Some(x -> String.valueOf(x));
        Option<Function<? super Integer, ? extends String>> funcNo = Nothing();

        assertEquals(Some("1"), Some(1).apply(func));
        assertEquals(Nothing(), Some(1).apply(funcNo));
    }

    @Test
    public void applyArray() {
        Array<Function<? super Integer, ? extends String>> funcs = Array.of(
            x -> String.valueOf(x),
            x -> String.valueOf(x * x),
            x -> String.valueOf(x * x * x)
        );
        Array<Function<? super Integer, ? extends String>> funcsNo = Array.empty();
        assertEquals(Array.of("1", "2", "3", "1", "4", "9", "1", "8", "27"), Array.of(1,2,3).apply(funcs));
        assertEquals(Array.empty(), Array.of(1,2,3).apply(funcsNo));
    }
}