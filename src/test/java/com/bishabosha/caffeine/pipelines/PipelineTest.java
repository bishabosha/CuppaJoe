/*
 * Copyright (c) 2017. Jamie Thompson <bishbashboshjt@gmail.com>
 */

package com.bishabosha.caffeine.pipelines;

import com.bishabosha.caffeine.base.Iterables;
import com.bishabosha.caffeine.base.MapEntry;
import com.bishabosha.caffeine.functional.Case;
import com.bishabosha.caffeine.functional.Option;
import com.bishabosha.caffeine.functional.Functions;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.*;
import java.util.stream.*;

import static com.bishabosha.caffeine.functional.Case.with;
import static com.bishabosha.caffeine.functional.Pattern.$any;

public class PipelineTest {

    @Test
    void testOf() {
        Assertions.assertIterableEquals(
            Arrays.asList(1, 2, 3, 4, 5),
            Pipeline.of(1, 2, 3, 4, 5)
        );
    }

    @Test
    void testFilter() {
        Assertions.assertIterableEquals(
            Iterables.of(1,2,3,4,5),
            Pipeline.of(1,2,3,4,5,6,7,8,9,10)
                .filter(x -> x <= 5)
        );
    }

    @Test
    void testMap() {
        Assertions.assertIterableEquals(
            Iterables.of("1", "2", "3", "4", "5"),
            Pipeline.of(1,2,3,4,5)
                .map(x -> x.toString())
        );
    }

    @Test
    void testFlatMap() {
        Assertions.assertIterableEquals(
            Iterables.of(1, 2, 3, 2, 4, 6, 3, 6, 9),
            Pipeline.of(1,2,3)
                .flatMap(x -> Pipeline.of(x*1, x*2, x*3))
        );
    }

    @Test
    void testDistinct() {
        Assertions.assertIterableEquals(
            Iterables.of(1,2,3,4,5),
            Pipeline.of(1, 2, 2, 3, 3, 3, 4, 4, 4, 4, 5, 5, 5, 5, 5)
                .distinct()
                .collect(Collectors.toList())
        );
    }

    @Test
    void testSorted() {
        Assertions.assertIterableEquals(
            Iterables.of(1,2,3,4,5),
            Pipeline.of(1, 5, 2, 4, 3)
                .sorted()
        );
        Assertions.assertIterableEquals(
            Iterables.of(5,4,3,2,1),
            Pipeline.of(1, 5, 2, 4, 3)
                .sorted(Comparator.reverseOrder())
        );
    }

    @Test
    void testTakeWhile() {
        Assertions.assertIterableEquals(
            Iterables.of("a", "b", "c"),
            Pipeline.of("a", "b", "c", "", "", "e", "f")
                .takeWhile(x -> x.length() > 0)
        );
    }

    @Test
    void testDropWhile() {
        Assertions.assertIterableEquals(
            Iterables.of(5, 6, 7, 8, 9, 10),
            Pipeline.of(1, 2, 3, 4, 5, 6, 7, 8, 9, 10)
                .dropWhile(x -> x < 5)
        );
    }

    @Test
    void testPeek() {
        List<Integer> list = new ArrayList<>();
        Pipeline.of(1, 2, 3, 4, 5, 6, 7, 8, 9, 10)
            .peek(list::add)
            .forEach(x -> {return;});
        Assertions.assertEquals(Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10), list);
    }

    @Test
    void testReduceIdentity() {
        int sum = Pipeline.of(1, 2, 3, 4, 5)
            .reduce(0, Integer::sum);
        Assertions.assertEquals(15, sum);
    }

    @Test
    void testReduce() {
        Assertions.assertEquals(
            Option.of(15),
            Pipeline.of(1, 2, 3, 4, 5)
                .reduce(Integer::sum)
        );
    }

    @Test
    void testCollect() {
        Assertions.assertIterableEquals(
            Iterables.of(1, 2, 3, 4, 5),
            Pipeline.of(1, 2, 3, 4, 5)
                .collect(() -> new ArrayList<>(), ArrayList::add)
        );
    }

    @Test
    void testCollectCollectors() {
        Assertions.assertIterableEquals(
            Iterables.of(1, 2, 3, 4, 5),
            Pipeline.of(1, 2, 3, 4, 5)
                .collect(Collectors.toList())
        );
    }

    @Test
    void testMax() {
        Assertions.assertEquals(
            Option.of(1),
            Pipeline.of(1, 2, 3, 4, 5, 6, 7, 8, 9, 10)
                .max(Comparator.reverseOrder())
        );
    }

    @Test
    void testAllMatch() {
        boolean allEven = Pipeline.of(2, 4, 6, 8, 10)
            .allMatch(x -> x % 2 == 0);
        Assertions.assertTrue(allEven);
    }

    @Test
    void testAnyMatch() {
        boolean anyDivBy3 = Pipeline.of(2, 4, 8, 16)
            .anyMatch(x -> x > 8);
        Assertions.assertTrue(anyDivBy3);
    }

    @Test
    void testNoneMatch() {
        boolean noneDivBy11 = Pipeline.of(2, 4, 6, 8, 10)
            .noneMatch(x -> x % 11 == 0);
        Assertions.assertTrue(noneDivBy11);
    }

    @Test
    void findFirst() {
        Assertions.assertEquals(
            Option.of(29),
            Pipeline.of(34, 67, 29, 31, 15, 17)
                .filter(x -> x < 30)
                .findFirst()
        );
    }

    @Test
    void testToArray() {
        Assertions.assertArrayEquals(
            new String[]{"hi", "yo"},
            Pipeline.of("hi", "yo", "hello", "greetings")
                .filter(x -> x.length() <= 2)
                .toArray(size -> new String[size])
        );
    }

    @Test
    void testLimit() {
        Assertions.assertIterableEquals(
            Iterables.of(1, 2, 3, 4, 5),
            Pipeline.of(1, 2, 3, 4, 5, 6, 7, 8, 9, 10)
                .limit(5)
                .collect(Collectors.toList())
        );
    }

    @Test
    void testSkip() {
        Assertions.assertIterableEquals(
            Iterables.of(6, 7, 8, 9, 10),
            Pipeline.of(1, 2, 3, 4, 5, 6, 7, 8, 9, 10)
                .skip(5)
                .collect(Collectors.toList())
        );
    }

    @Test
    void testIterate() {
        Assertions.assertIterableEquals(
            Iterables.of(1, 2, 3, 4, 5),
            Pipeline.iterate(1, x -> x+1)
                .limit(5)
                .collect(Collectors.toList())
        );
    }

    @Test
    void testZipWith() {
        Assertions.assertEquals(
            "[1=Hi, 2=Hello]",
            Pipeline.of(1, 2, 3, 4)
            .zipWith(Arrays.asList("Hi", "Hello"), MapEntry::new)
            .toString()
        );
    }

    @Test
    void testCycle() {
        Assertions.assertIterableEquals(
            Iterables.of("Hi", "Hello", "Hi", "Hello", "Hi", "Hello"),
            Pipeline.cycle("Hi", "Hello").limit(6).collect(Collectors.toList())
        );
    }

    @Test
    void testCartesianProduct() {
        Assertions.assertIterableEquals(
            Iterables.of(1, 2, 2, 4, 3, 6),
            Pipeline.of(1, 2, 3).product(Arrays.asList(1, 2), Functions::multiply)
        );
    }

    @Test
    void testTerminatingIterate() {
        Assertions.assertIterableEquals(
            Iterables.of(1, 2, 3, 4, 5),
            Pipeline.iterate(1, x -> x > 5, x -> x+1)
        );
    }

    @Test
    void testOptional() {
        Assertions.assertIterableEquals(
            Iterables.of(1,3),
            Pipeline.of(Optional.of(1), Optional.empty(), Optional.of(3))
                .flatMap(x -> Pipeline.fromOptional(x))
        );
    }

    @Test
    void testMatching() {
        Assertions.assertIterableEquals(
            Iterables.of("One", "Two", "Three", "One", "Two", "Three", "One", "Two"),
            Pipeline.of(
                "1.0", null, BigDecimal.valueOf(2), 4, BigInteger.valueOf(3), Optional.of(400),
                1, 2.0, "Batman", 3L, "1", "Fall through", null, BigInteger.valueOf(2)
            )
            .match(Case.combine(
                with($any(1, 1L, 1.0, "1", "1.0", BigInteger.ONE, BigDecimal.ONE), () -> "One"),
                with($any(2, 2L, 2.0, "2", "2.0", BigInteger.valueOf(2), BigDecimal.valueOf(2)), () -> "Two"),
                with($any(3, 3L, 3.0, "3", "3.0", BigInteger.valueOf(3), BigDecimal.valueOf(3)), () -> "Three")
            ))
        );
    }

    @Test
    void testConcat() {
        Assertions.assertIterableEquals(
            Iterables.of(1, 2, 3, 4, 5),
            Pipeline.concat(
                Pipeline.of(1, 2),
                Pipeline.of(3),
                Pipeline.empty(),
                Pipeline.of(4, 5)
            )
        );
    }
}