package com.github.bishabosha.cuppajoe.higher.applicative;

import com.github.bishabosha.cuppajoe.collections.immutable.Array;
import com.github.bishabosha.cuppajoe.control.Option;
import com.github.bishabosha.cuppajoe.functions.*;
import com.github.bishabosha.cuppajoe.higher.functions.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.function.BiFunction;
import java.util.function.Function;

import static com.github.bishabosha.cuppajoe.API.None;
import static com.github.bishabosha.cuppajoe.API.Some;
import static com.github.bishabosha.cuppajoe.collections.immutable.API.List;

public class Applicative1Test {

    @Test
    public void test_liftAFunc1() {
        Function<Option<Integer>, Option<String>> funcLift = Applicative1.liftAFunc1(x -> String.valueOf(x));
        Assertions.assertEquals(API.Some("1"), funcLift.apply(API.Some(1)));
    }

    @Test
    public void test_liftAFunc2() {
        BiFunction<Option<Integer>, Option<Integer>, Option<String>> funcLift = Applicative1.liftAFunc2((x, y) -> String.valueOf(x + y));
        Assertions.assertEquals(API.Some("2"), funcLift.apply(API.Some(1), API.Some(1)));
    }

    @Test
    public void test_liftAFunc3() {
        Func3<Option<Integer>, Option<Integer>, Option<Integer>, Option<String>> funcLift = Applicative1.liftAFunc3((x, y, z) -> String.valueOf(x + y + z));
        Assertions.assertEquals(API.Some("3"), funcLift.apply(API.Some(1), API.Some(1), API.Some(1)));
    }

    @Test
    public void test_liftAFunc4() {
        Func4<Option<Integer>, Option<Integer>, Option<Integer>, Option<Integer>, Option<String>> funcLift = Applicative1.liftAFunc4((w, x, y, z) -> String.valueOf(w + x + y + z));
        Assertions.assertEquals(API.Some("4"), funcLift.apply(API.Some(1), API.Some(1), API.Some(1), API.Some(1)));
    }

    @Test
    public void test_liftAFunc5() {
        Func5<Option<Integer>, Option<Integer>, Option<Integer>, Option<Integer>, Option<Integer>, Option<String>> funcLift = Applicative1.liftAFunc5((v, w, x, y, z) -> String.valueOf(v + w + x + y + z));
        Assertions.assertEquals(API.Some("5"), funcLift.apply(API.Some(1), API.Some(1), API.Some(1), API.Some(1), API.Some(1)));
    }

    @Test
    public void test_liftAFunc6() {
        Func6<Option<Integer>, Option<Integer>, Option<Integer>, Option<Integer>, Option<Integer>, Option<Integer>, Option<String>> funcLift = Applicative1.liftAFunc6((u, v, w, x, y, z) -> String.valueOf(u + v + w + x + y + z));
        Assertions.assertEquals(API.Some("6"), funcLift.apply(API.Some(1), API.Some(1), API.Some(1), API.Some(1), API.Some(1), API.Some(1)));
    }

    @Test
    public void test_liftAFunc7() {
        Func7<Option<Integer>, Option<Integer>, Option<Integer>, Option<Integer>, Option<Integer>, Option<Integer>, Option<Integer>, Option<String>> funcLift = Applicative1.liftAFunc7((t, u, v, w, x, y, z) -> String.valueOf(t + u + v + w + x + y + z));
        Assertions.assertEquals(API.Some("7"), funcLift.apply(API.Some(1), API.Some(1), API.Some(1), API.Some(1), API.Some(1), API.Some(1), API.Some(1)));
    }

    @Test
    public void test_liftAFunc8() {
        Func8<Option<Integer>, Option<Integer>, Option<Integer>, Option<Integer>, Option<Integer>, Option<Integer>, Option<Integer>, Option<Integer>, Option<String>> funcLift = Applicative1.liftAFunc8((s, t, u, v, w, x, y, z) -> String.valueOf(s + t + u + v + w + x + y + z));
        Assertions.assertEquals(API.Some("8"), funcLift.apply(API.Some(1), API.Some(1), API.Some(1), API.Some(1), API.Some(1), API.Some(1), API.Some(1), API.Some(1)));
    }

    @Test
    public void test_applyList() {
        List<Function<? super Integer, ? extends String>> funcs = API.List(
                String::valueOf,
                x -> String.valueOf(x * x),
                x -> String.valueOf(x * x * x)
        );
        List<Function<? super Integer, ? extends String>> funcsNo = API.List();
        Assertions.assertEquals(API.List("1", "2", "3", "1", "4", "9", "1", "8", "27"), API.List(1, 2, 3).apply(funcs));
        Assertions.assertEquals(API.List(), API.List(1, 2, 3).apply(funcsNo));
    }

    @Test
    public void test_applyOption() {
        Option<Function<? super Integer, ? extends String>> func = API.Some(String::valueOf);
        Option<Function<? super Integer, ? extends String>> funcNo = API.None();

        Assertions.assertEquals(API.Some("1"), API.Some(1).apply(func));
        Assertions.assertEquals(API.None(), API.Some(1).apply(funcNo));
    }

    @Test
    public void test_applyArray() {
        Array<Function<? super Integer, ? extends String>> funcs = Array.of(
                String::valueOf,
                x -> String.valueOf(x * x),
                x -> String.valueOf(x * x * x)
        );
        Array<Function<? super Integer, ? extends String>> funcsNo = Array.empty();
        Assertions.assertEquals(Array.of("1", "2", "3", "1", "4", "9", "1", "8", "27"), Array.of(1, 2, 3).apply(funcs));
        Assertions.assertEquals(Array.empty(), Array.of(1, 2, 3).apply(funcsNo));
    }
}