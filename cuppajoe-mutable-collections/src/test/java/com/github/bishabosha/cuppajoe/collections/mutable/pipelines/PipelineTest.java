/*
 * Copyright (c) 2017. Jamie Thompson <bishbashboshjt@gmail.com>
 */

package com.github.bishabosha.cuppajoe.collections.mutable.pipelines;

import com.github.bishabosha.cuppajoe.collections.mutable.internal.MapEntry;
import com.github.bishabosha.cuppajoe.control.Option;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.*;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

public class PipelineTest {

    @Test
    public void test_Of() {
        assertIterableEquals(
            List.of(1, 2, 3, 4, 5),
            Pipeline.ofAll(1, 2, 3, 4, 5)
        );
    }

    @Test
    public void test_Filter() {
        assertIterableEquals(
            List.of(1, 2, 3, 4, 5),
            Pipeline.ofAll(1, 2, 3, 4, 5, 6, 7, 8, 9, 10)
                    .filter(x -> x <= 5)
        );
    }

    @Test
    public void test_Map() {
        assertIterableEquals(
            List.of("1", "2", "3", "4", "5"),
            Pipeline.ofAll(1, 2, 3, 4, 5)
                    .map(Object::toString)
        );
    }

    @Test
    public void test_FlatMap() {
        assertIterableEquals(
            List.of(1, 2, 3, 2, 4, 6, 3, 6, 9),
            Pipeline.ofAll(1, 2, 3)
                    .flatMap(x -> Pipeline.ofAll(x, x * 2, x * 3))
        );
    }

    @Test
    public void test_Distinct() {
        assertIterableEquals(
            List.of(1, 2, 3, 4, 5),
            Pipeline.ofAll(1, 2, 2, 3, 3, 3, 4, 4, 4, 4, 5, 5, 5, 5, 5)
                    .distinct()
                    .collect(Collectors.toList())
        );
    }

    @Test
    public void test_Sorted() {
        assertIterableEquals(
            List.of(1, 2, 3, 4, 5),
            Pipeline.ofAll(1, 5, 2, 4, 3)
                    .sorted()
        );
        assertIterableEquals(
            List.of(5, 4, 3, 2, 1),
            Pipeline.ofAll(1, 5, 2, 4, 3)
                    .sorted(Comparator.reverseOrder())
        );
    }

    @Test
    public void test_TakeWhile() {
        assertIterableEquals(
            List.of("a", "b", "c"),
            Pipeline.ofAll("a", "b", "c", "", "", "e", "f")
                    .takeWhile(x -> x.length() > 0)
        );
    }

    @Test
    public void test_DropWhile() {
        assertIterableEquals(
            List.of(5, 6, 7, 8, 9, 10),
            Pipeline.ofAll(1, 2, 3, 4, 5, 6, 7, 8, 9, 10)
                    .dropWhile(x -> x < 5)
        );
    }

    @Test
    public void test_Peek() {
        List<Integer> list = new ArrayList<>();
        Pipeline.ofAll(1, 2, 3, 4, 5, 6, 7, 8, 9, 10)
                .peek(list::add)
                .forEach(x -> {});
        assertEquals(List.of(1, 2, 3, 4, 5, 6, 7, 8, 9, 10), list);
    }

    @Test
    public void test_ReduceIdentity() {
        int sum = Pipeline.ofAll(1, 2, 3, 4, 5).reduce(0, Integer::sum);
        assertEquals(15, sum);
    }

    @Test
    public void test_Reduce() {
        assertEquals(
            Option.of(15),
            Pipeline.ofAll(1, 2, 3, 4, 5)
                    .reduce(Integer::sum)
        );
    }

    @Test
    public void test_Collect() {
        assertIterableEquals(
            List.of(1, 2, 3, 4, 5),
            Pipeline.ofAll(1, 2, 3, 4, 5)
                    .collect(ArrayList::new, ArrayList::add)
        );
    }

    @Test
    public void test_CollectCollectors() {
        assertIterableEquals(
            List.of(1, 2, 3, 4, 5),
            Pipeline.ofAll(1, 2, 3, 4, 5)
                    .collect(Collectors.toList())
        );
    }

    @Test
    public void test_Max() {
        assertEquals(
            Option.of(1),
            Pipeline.ofAll(1, 2, 3, 4, 5, 6, 7, 8, 9, 10)
                    .max(Comparator.reverseOrder())
        );
    }

    @Test
    public void test_AllMatch() {
        var allEven = Pipeline.ofAll(2, 4, 6, 8, 10)
                .allMatch(x -> x % 2 == 0);
        Assertions.assertTrue(allEven);
    }

    @Test
    public void test_AnyMatch() {
        var anyDivBy3 = Pipeline.ofAll(2, 4, 8, 16)
                .anyMatch(x -> x > 8);
        Assertions.assertTrue(anyDivBy3);
    }

    @Test
    public void test_NoneMatch() {
        var noneDivBy11 = Pipeline.ofAll(2, 4, 6, 8, 10)
                .noneMatch(x -> x % 11 == 0);
        Assertions.assertTrue(noneDivBy11);
    }

    @Test
    public void findFirst() {
        assertEquals(
            Option.of(29),
            Pipeline.ofAll(34, 67, 29, 31, 15, 17)
                    .filter(x -> x < 30)
                    .findFirst()
        );
    }

    @Test
    public void test_ToArray() {
        assertArrayEquals(
            new String[]{"hi", "yo"},
            Pipeline.ofAll("hi", "yo", "hello", "greetings")
                    .filter(x -> x.length() <= 2)
                    .toArray(String[]::new)
        );
    }

    @Test
    public void test_Limit() {
        assertIterableEquals(
            List.of(1, 2, 3, 4, 5),
            Pipeline.ofAll(1, 2, 3, 4, 5, 6, 7, 8, 9, 10)
                    .limit(5)
                    .collect(Collectors.toList())
        );
    }

    @Test
    public void test_Skip() {
        assertIterableEquals(
            List.of(6, 7, 8, 9, 10),
            Pipeline.ofAll(1, 2, 3, 4, 5, 6, 7, 8, 9, 10)
                    .skip(5)
                    .collect(Collectors.toList())
        );
    }

    @Test
    public void test_Iterate() {
        assertIterableEquals(
            List.of(1, 2, 3, 4, 5),
            Pipeline.iterate(1, x -> x + 1)
                    .limit(5)
                    .collect(Collectors.toList())
        );
    }

    @Test
    public void test_ZipWith() {
        assertEquals(
                "[1=Hi, 2=Hello]",
                Pipeline.ofAll(1, 2, 3, 4)
                        .zipWith(Arrays.asList("Hi", "Hello"), MapEntry::new)
                        .toString()
        );
    }

    @Test
    public void test_Cycle() {
        assertIterableEquals(
            List.of("Hi", "Hello", "Hi", "Hello", "Hi", "Hello"),
            Pipeline.cycle("Hi", "Hello")
                    .limit(6)
                    .collect(Collectors.toList())
        );
    }

    @Test
    public void test_CartesianProduct() {
        assertIterableEquals(
            List.of(1, 2, 2, 4, 3, 6),
            Pipeline.ofAll(1, 2, 3).product(Arrays.asList(1, 2), (x, y) -> x * y)
        );
    }

    @Test
    public void test_TerminatingIterate() {
        assertIterableEquals(
            List.of(1, 2, 3, 4, 5),
            Pipeline.iterate(1, x -> x > 5, x -> x + 1)
        );
    }

    @Test
    public void test_Optional() {
        assertIterableEquals(
            List.of(1, 3),
            Pipeline.ofAll(Optional.of(1), Optional.empty(), Optional.of(3))
                    .flatMap(Pipeline::fromOptional)
        );
    }

    @Test
    public void test_Concat() {
        assertIterableEquals(
            List.of(1, 2, 3, 4, 5),
            Pipeline.concat(
                Pipeline.ofAll(1, 2),
                Pipeline.ofAll(3),
                Pipeline.empty(),
                Pipeline.ofAll(4, 5)
            )
        );
    }
}