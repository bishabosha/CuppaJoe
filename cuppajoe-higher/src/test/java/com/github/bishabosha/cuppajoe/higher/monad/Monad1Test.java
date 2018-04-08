package com.github.bishabosha.cuppajoe.higher.monad;

import com.github.bishabosha.cuppajoe.annotation.NonNull;
import com.github.bishabosha.cuppajoe.higher.applicative.Applicative1;
import com.github.bishabosha.cuppajoe.higher.functions.*;
import com.github.bishabosha.cuppajoe.higher.functor.Functor1;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.UnaryOperator;

public class Monad1Test {

    @Test
    public void test_liftMOp1() {
        UnaryOperator<BasicMonad1<Integer>> funcLift = Monad1.liftMOp1(x -> x + 1);
        Assertions.assertEquals(BasicMonad1.of(2), funcLift.apply(BasicMonad1.of(1)));
    }

    @Test
    public void test_liftMOp2() {
        BinaryOperator<BasicMonad1<Integer>> funcLift = Monad1.liftMOp2((x, y) -> x + y);
        Assertions.assertEquals(BasicMonad1.of(3), funcLift.apply(BasicMonad1.of(1), BasicMonad1.of(2)));
    }

    @Test
    public void test_liftMFunc1() {
        Function<BasicMonad1<Integer>, BasicMonad1<String>> funcLift = Monad1.liftMFunc1(x -> String.valueOf(x));
        Assertions.assertEquals(BasicMonad1.of("1"), funcLift.apply(BasicMonad1.of(1)));
    }

    @Test
    public void test_liftMFunc2() {
        BiFunction<BasicMonad1<Integer>, BasicMonad1<Integer>, BasicMonad1<String>> funcLift = Monad1.liftMFunc2((x, y) -> String.valueOf(x + y));
        Assertions.assertEquals(BasicMonad1.of("2"), funcLift.apply(BasicMonad1.of(1), BasicMonad1.of(1)));
    }

    @Test
    public void test_liftMFunc3() {
        Func3<BasicMonad1<Integer>, BasicMonad1<Integer>, BasicMonad1<Integer>, BasicMonad1<String>> funcLift = Monad1.liftMFunc3((x, y, z) -> String.valueOf(x + y + z));
        Assertions.assertEquals(BasicMonad1.of("3"), funcLift.apply(BasicMonad1.of(1), BasicMonad1.of(1), BasicMonad1.of(1)));
    }

    @Test
    public void test_liftMFunc4() {
        Func4<BasicMonad1<Integer>, BasicMonad1<Integer>, BasicMonad1<Integer>, BasicMonad1<Integer>, BasicMonad1<String>> funcLift = Monad1.liftMFunc4((w, x, y, z) -> String.valueOf(w + x + y + z));
        Assertions.assertEquals(BasicMonad1.of("4"), funcLift.apply(BasicMonad1.of(1), BasicMonad1.of(1), BasicMonad1.of(1), BasicMonad1.of(1)));
    }

    @Test
    public void test_liftMFunc5() {
        Func5<BasicMonad1<Integer>, BasicMonad1<Integer>, BasicMonad1<Integer>, BasicMonad1<Integer>, BasicMonad1<Integer>, BasicMonad1<String>> funcLift = Monad1.liftMFunc5((v, w, x, y, z) -> String.valueOf(v + w + x + y + z));
        Assertions.assertEquals(BasicMonad1.of("5"), funcLift.apply(BasicMonad1.of(1), BasicMonad1.of(1), BasicMonad1.of(1), BasicMonad1.of(1), BasicMonad1.of(1)));
    }

    @Test
    public void test_liftMFunc6() {
        Func6<BasicMonad1<Integer>, BasicMonad1<Integer>, BasicMonad1<Integer>, BasicMonad1<Integer>, BasicMonad1<Integer>, BasicMonad1<Integer>, BasicMonad1<String>> funcLift = Monad1.liftMFunc6((u, v, w, x, y, z) -> String.valueOf(u + v + w + x + y + z));
        Assertions.assertEquals(BasicMonad1.of("6"), funcLift.apply(BasicMonad1.of(1), BasicMonad1.of(1), BasicMonad1.of(1), BasicMonad1.of(1), BasicMonad1.of(1), BasicMonad1.of(1)));
    }

    @Test
    public void test_liftMFunc7() {
        Func7<BasicMonad1<Integer>, BasicMonad1<Integer>, BasicMonad1<Integer>, BasicMonad1<Integer>, BasicMonad1<Integer>, BasicMonad1<Integer>, BasicMonad1<Integer>, BasicMonad1<String>> funcLift = Monad1.liftMFunc7((t, u, v, w, x, y, z) -> String.valueOf(t + u + v + w + x + y + z));
        Assertions.assertEquals(BasicMonad1.of("7"), funcLift.apply(BasicMonad1.of(1), BasicMonad1.of(1), BasicMonad1.of(1), BasicMonad1.of(1), BasicMonad1.of(1), BasicMonad1.of(1), BasicMonad1.of(1)));
    }

    @Test
    public void test_liftMFunc8() {
        Func8<BasicMonad1<Integer>, BasicMonad1<Integer>, BasicMonad1<Integer>, BasicMonad1<Integer>, BasicMonad1<Integer>, BasicMonad1<Integer>, BasicMonad1<Integer>, BasicMonad1<Integer>, BasicMonad1<String>> funcLift = Monad1.liftMFunc8((s, t, u, v, w, x, y, z) -> String.valueOf(s + t + u + v + w + x + y + z));
        Assertions.assertEquals(BasicMonad1.of("8"), funcLift.apply(BasicMonad1.of(1), BasicMonad1.of(1), BasicMonad1.of(1), BasicMonad1.of(1), BasicMonad1.of(1), BasicMonad1.of(1), BasicMonad1.of(1), BasicMonad1.of(1)));
    }

    private static class BasicMonad1<T> implements Monad1<BasicMonad1, T> {

        private final T value;

        private BasicMonad1(T value) {
            this.value = value;
        }

        public static <U> BasicMonad1<U> of(U value) {
            return new BasicMonad1<>(value);
        }

        @Override
        public <U> Monad1<BasicMonad1, U> flatMap(@NonNull Function<? super T, Monad1<BasicMonad1, ? extends U>> mapper) {
            return (Monad1<BasicMonad1, U>) mapper.apply(value);
        }

        @Override
        public <U> Applicative1<BasicMonad1, U> pure(U value) {
            return new BasicMonad1<>(value);
        }

        @Override
        public <U> Applicative1<BasicMonad1, U> apply(@NonNull Applicative1<BasicMonad1, Function<? super T, ? extends U>> applicative1) {
            var app = (BasicMonad1<Function<? super T, ? extends U>>) applicative1;
            return pure(app.value.apply(value));
        }

        @Override
        public <U> Functor1<BasicMonad1, U> map(@NonNull Function<? super T, ? extends U> mapper) {
            return pure(mapper.apply(value));
        }

        @Override
        public boolean equals(Object obj) {
            return obj instanceof BasicMonad1 && Objects.equals(value, ((BasicMonad1) obj).value);
        }
    }
}