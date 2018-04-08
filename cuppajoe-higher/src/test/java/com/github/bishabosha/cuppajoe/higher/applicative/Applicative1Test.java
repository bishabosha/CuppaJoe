package com.github.bishabosha.cuppajoe.higher.applicative;

import com.github.bishabosha.cuppajoe.annotation.NonNull;
import com.github.bishabosha.cuppajoe.higher.functions.*;
import com.github.bishabosha.cuppajoe.higher.functor.Functor1;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;

public class Applicative1Test {

    @Test
    public void test_liftAFunc1() {
        Function<BasicApplicative1<Integer>, BasicApplicative1<String>> funcLift = Applicative1.liftAFunc1(x -> String.valueOf(x));
        Assertions.assertEquals(BasicApplicative1.of("1"), funcLift.apply(BasicApplicative1.of(1)));
    }

    @Test
    public void test_liftAFunc2() {
        BiFunction<BasicApplicative1<Integer>, BasicApplicative1<Integer>, BasicApplicative1<String>> funcLift = Applicative1.liftAFunc2((x, y) -> String.valueOf(x + y));
        Assertions.assertEquals(BasicApplicative1.of("2"), funcLift.apply(BasicApplicative1.of(1), BasicApplicative1.of(1)));
    }

    @Test
    public void test_liftAFunc3() {
        Func3<BasicApplicative1<Integer>, BasicApplicative1<Integer>, BasicApplicative1<Integer>, BasicApplicative1<String>> funcLift = Applicative1.liftAFunc3((x, y, z) -> String.valueOf(x + y + z));
        Assertions.assertEquals(BasicApplicative1.of("3"), funcLift.apply(BasicApplicative1.of(1), BasicApplicative1.of(1), BasicApplicative1.of(1)));
    }

    @Test
    public void test_liftAFunc4() {
        Func4<BasicApplicative1<Integer>, BasicApplicative1<Integer>, BasicApplicative1<Integer>, BasicApplicative1<Integer>, BasicApplicative1<String>> funcLift = Applicative1.liftAFunc4((w, x, y, z) -> String.valueOf(w + x + y + z));
        Assertions.assertEquals(BasicApplicative1.of("4"), funcLift.apply(BasicApplicative1.of(1), BasicApplicative1.of(1), BasicApplicative1.of(1), BasicApplicative1.of(1)));
    }

    @Test
    public void test_liftAFunc5() {
        Func5<BasicApplicative1<Integer>, BasicApplicative1<Integer>, BasicApplicative1<Integer>, BasicApplicative1<Integer>, BasicApplicative1<Integer>, BasicApplicative1<String>> funcLift = Applicative1.liftAFunc5((v, w, x, y, z) -> String.valueOf(v + w + x + y + z));
        Assertions.assertEquals(BasicApplicative1.of("5"), funcLift.apply(BasicApplicative1.of(1), BasicApplicative1.of(1), BasicApplicative1.of(1), BasicApplicative1.of(1), BasicApplicative1.of(1)));
    }

    @Test
    public void test_liftAFunc6() {
        Func6<BasicApplicative1<Integer>, BasicApplicative1<Integer>, BasicApplicative1<Integer>, BasicApplicative1<Integer>, BasicApplicative1<Integer>, BasicApplicative1<Integer>, BasicApplicative1<String>> funcLift = Applicative1.liftAFunc6((u, v, w, x, y, z) -> String.valueOf(u + v + w + x + y + z));
        Assertions.assertEquals(BasicApplicative1.of("6"), funcLift.apply(BasicApplicative1.of(1), BasicApplicative1.of(1), BasicApplicative1.of(1), BasicApplicative1.of(1), BasicApplicative1.of(1), BasicApplicative1.of(1)));
    }

    @Test
    public void test_liftAFunc7() {
        Func7<BasicApplicative1<Integer>, BasicApplicative1<Integer>, BasicApplicative1<Integer>, BasicApplicative1<Integer>, BasicApplicative1<Integer>, BasicApplicative1<Integer>, BasicApplicative1<Integer>, BasicApplicative1<String>> funcLift = Applicative1.liftAFunc7((t, u, v, w, x, y, z) -> String.valueOf(t + u + v + w + x + y + z));
        Assertions.assertEquals(BasicApplicative1.of("7"), funcLift.apply(BasicApplicative1.of(1), BasicApplicative1.of(1), BasicApplicative1.of(1), BasicApplicative1.of(1), BasicApplicative1.of(1), BasicApplicative1.of(1), BasicApplicative1.of(1)));
    }

    @Test
    public void test_liftAFunc8() {
        Func8<BasicApplicative1<Integer>, BasicApplicative1<Integer>, BasicApplicative1<Integer>, BasicApplicative1<Integer>, BasicApplicative1<Integer>, BasicApplicative1<Integer>, BasicApplicative1<Integer>, BasicApplicative1<Integer>, BasicApplicative1<String>> funcLift = Applicative1.liftAFunc8((s, t, u, v, w, x, y, z) -> String.valueOf(s + t + u + v + w + x + y + z));
        Assertions.assertEquals(BasicApplicative1.of("8"), funcLift.apply(BasicApplicative1.of(1), BasicApplicative1.of(1), BasicApplicative1.of(1), BasicApplicative1.of(1), BasicApplicative1.of(1), BasicApplicative1.of(1), BasicApplicative1.of(1), BasicApplicative1.of(1)));
    }

    @Test
    public void test_applyBasicApplicative1() {
        BasicApplicative1<Function<? super Integer, ? extends String>> func = BasicApplicative1.of(String::valueOf);
        Assertions.assertEquals(BasicApplicative1.of("1"), BasicApplicative1.of(1).apply(func));
    }

    private static class BasicApplicative1<T> implements Applicative1<BasicApplicative1, T> {

        private final T value;

        private BasicApplicative1(T value) {
            this.value = value;
        }

        public static <U> BasicApplicative1<U> of(U value) {
            return new BasicApplicative1<>(value);
        }

        @Override
        public <U> Applicative1<BasicApplicative1, U> pure(U value) {
            return new BasicApplicative1<>(value);
        }

        @Override
        public <U> Applicative1<BasicApplicative1, U> apply(@NonNull Applicative1<BasicApplicative1, Function<? super T, ? extends U>> applicative1) {
            var app = (BasicApplicative1<Function<? super T, ? extends U>>) applicative1;
            return pure(app.value.apply(value));
        }

        @Override
        public <U> Functor1<BasicApplicative1, U> map(@NonNull Function<? super T, ? extends U> mapper) {
            return pure(mapper.apply(value));
        }

        @Override
        public boolean equals(Object obj) {
            return obj instanceof BasicApplicative1 && Objects.equals(value, ((BasicApplicative1) obj).value);
        }
    }
}