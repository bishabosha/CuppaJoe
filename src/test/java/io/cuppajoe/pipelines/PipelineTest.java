/*
 * Copyright (c) 2017. Jamie Thompson <bishbashboshjt@gmail.com>
 */

package io.cuppajoe.pipelines;

import io.cuppajoe.collections.mutable.base.MapEntry;
import io.cuppajoe.control.Option;
import io.cuppajoe.match.Case;
import io.cuppajoe.math.Arithmetic;
import org.junit.Assert;
import org.junit.Test;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.*;
import java.util.stream.Collectors;

import static io.cuppajoe.match.Case.with;
import static io.cuppajoe.match.patterns.Standard.$any;
import static org.hamcrest.Matchers.contains;

public class PipelineTest {

    @Test
    public void testOf() {
        Assert.assertThat(
            Pipeline.of(1, 2, 3, 4, 5),
            contains(1,2,3,4,5)
        );
    }

    @Test
    public void testFilter() {
        Assert.assertThat(
            Pipeline.of(1,2,3,4,5,6,7,8,9,10)
                    .filter(x -> x <= 5),
            contains(1,2,3,4,5)
        );
    }

    @Test
    public void testMap() {
        Assert.assertThat(
            Pipeline.of(1,2,3,4,5)
                    .map(x -> x.toString()),
            contains("1", "2", "3", "4", "5")
        );
    }

    @Test
    public void testFlatMap() {
        Assert.assertThat(
            Pipeline.of(1,2,3)
                    .flatMap(x -> Pipeline.of(x*1, x*2, x*3)),
            contains(1, 2, 3, 2, 4, 6, 3, 6, 9)
        );
    }

    @Test
    public void testDistinct() {
        Assert.assertThat(
            Pipeline.of(1, 2, 2, 3, 3, 3, 4, 4, 4, 4, 5, 5, 5, 5, 5)
                    .distinct()
                    .collect(Collectors.toList()),
            contains(1,2,3,4,5)
        );
    }

    @Test
    public void testSorted() {
        Assert.assertThat(
            Pipeline.of(1, 5, 2, 4, 3)
                    .sorted(),
            contains(1,2,3,4,5)
        );
        Assert.assertThat(
            Pipeline.of(1, 5, 2, 4, 3)
                    .sorted(Comparator.reverseOrder()),
            contains(5,4,3,2,1)
        );
    }

    @Test
    public void testTakeWhile() {
        Assert.assertThat(
            Pipeline.of("a", "b", "c", "", "", "e", "f")
                    .takeWhile(x -> x.length() > 0),
            contains("a", "b", "c")
        );
    }

    @Test
    public void testDropWhile() {
        Assert.assertThat(
            Pipeline.of(1, 2, 3, 4, 5, 6, 7, 8, 9, 10)
                    .dropWhile(x -> x < 5),
            contains(5, 6, 7, 8, 9, 10)
        );
    }

    @Test
    public void testPeek() {
        List<Integer> list = new ArrayList<>();
        Pipeline.of(1, 2, 3, 4, 5, 6, 7, 8, 9, 10)
                .peek(list::add)
                .forEach(x -> {});
        Assert.assertEquals(Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10), list);
    }

    @Test
    public void testReduceIdentity() {
        int sum = Pipeline.of(1, 2, 3, 4, 5)
                          .reduce(0, Integer::sum);
        Assert.assertEquals(15, sum);
    }

    @Test
    public void testReduce() {
        Assert.assertEquals(
            Option.of(15),
            Pipeline.of(1, 2, 3, 4, 5)
                    .reduce(Integer::sum)
        );
    }

    @Test
    public void testCollect() {
        Assert.assertThat(
            Pipeline.of(1, 2, 3, 4, 5)
                    .collect(ArrayList::new, ArrayList::add),
            contains(1, 2, 3, 4, 5)
        );
    }

    @Test
    public void testCollectCollectors() {
        Assert.assertThat(
            Pipeline.of(1, 2, 3, 4, 5)
                    .collect(Collectors.toList()),
            contains(1, 2, 3, 4, 5)
        );
    }

    @Test
    public void testMax() {
        Assert.assertEquals(
            Option.of(1),
            Pipeline.of(1, 2, 3, 4, 5, 6, 7, 8, 9, 10)
                    .max(Comparator.reverseOrder())
        );
    }

    @Test
    public void testAllMatch() {
        var allEven = Pipeline.of(2, 4, 6, 8, 10)
                                  .allMatch(x -> x % 2 == 0);
        Assert.assertTrue(allEven);
    }

    @Test
    public void testAnyMatch() {
        var anyDivBy3 = Pipeline.of(2, 4, 8, 16)
                                    .anyMatch(x -> x > 8);
        Assert.assertTrue(anyDivBy3);
    }

    @Test
    public void testNoneMatch() {
        var noneDivBy11 = Pipeline.of(2, 4, 6, 8, 10)
                                      .noneMatch(x -> x % 11 == 0);
        Assert.assertTrue(noneDivBy11);
    }

    @Test
    public void findFirst() {
        Assert.assertEquals(
            Option.of(29),
            Pipeline.of(34, 67, 29, 31, 15, 17)
                .filter(x -> x < 30)
                .findFirst()
        );
    }

    @Test
    public void testToArray() {
        Assert.assertArrayEquals(
            new String[]{"hi", "yo"},
            Pipeline.of("hi", "yo", "hello", "greetings")
                    .filter(x -> x.length() <= 2)
                    .toArray(String[]::new)
        );
    }

    @Test
    public void testLimit() {
        Assert.assertThat(
            Pipeline.of(1, 2, 3, 4, 5, 6, 7, 8, 9, 10)
                    .limit(5)
                    .collect(Collectors.toList()),
            contains(1, 2, 3, 4, 5)
        );
    }

    @Test
    public void testSkip() {
        Assert.assertThat(
            Pipeline.of(1, 2, 3, 4, 5, 6, 7, 8, 9, 10)
                    .skip(5)
                    .collect(Collectors.toList()),
            contains(6, 7, 8, 9, 10)
        );
    }

    @Test
    public void testIterate() {
        Assert.assertThat(
            Pipeline.iterate(1, x -> x+1)
                    .limit(5)
                    .collect(Collectors.toList()),
            contains(1, 2, 3, 4, 5)
        );
    }

    @Test
    public void testZipWith() {
        Assert.assertEquals(
            "[1=Hi, 2=Hello]",
            Pipeline.of(1, 2, 3, 4)
            .zipWith(Arrays.asList("Hi", "Hello"), MapEntry::new)
            .toString()
        );
    }

    @Test
    public void testCycle() {
        Assert.assertThat(
            Pipeline.cycle("Hi", "Hello").limit(6).collect(Collectors.toList()),
            contains("Hi", "Hello", "Hi", "Hello", "Hi", "Hello")
        );
    }

    @Test
    public void testCartesianProduct() {
        Assert.assertThat(
            Pipeline.of(1, 2, 3).product(Arrays.asList(1, 2), Arithmetic::multiply),
            contains(1, 2, 2, 4, 3, 6)
        );
    }

    @Test
    public void testTerminatingIterate() {
        Assert.assertThat(
            Pipeline.iterate(1, x -> x > 5, x -> x+1),
            contains(1, 2, 3, 4, 5)
        );
    }

    @Test
    public void testOptional() {
        Assert.assertThat(
            Pipeline.of(Optional.of(1), Optional.empty(), Optional.of(3))
                    .flatMap(Pipeline::fromOptional),
            contains(1,3)
        );
    }

    @Test
    public void testMatching() {
        Assert.assertThat(
            Pipeline.of(
                "1.0", null, BigDecimal.valueOf(2), 4, BigInteger.valueOf(3), Optional.of(400),
                1, 2.0, "Batman", 3L, "1", "Fall through", null, BigInteger.valueOf(2)
            ).match(Case.combine(
                with($any(1, 1L, 1.0, "1", "1.0", BigInteger.ONE, BigDecimal.ONE), () -> "One"),
                with($any(2, 2L, 2.0, "2", "2.0", BigInteger.valueOf(2), BigDecimal.valueOf(2)), () -> "Two"),
                with($any(3, 3L, 3.0, "3", "3.0", BigInteger.valueOf(3), BigDecimal.valueOf(3)), () -> "Three")
            )),
            contains("One", "Two", "Three", "One", "Two", "Three", "One", "Two")
        );
    }

    @Test
    public void testConcat() {
        Assert.assertThat(
            Pipeline.concat(
                Pipeline.of(1, 2),
                Pipeline.of(3),
                Pipeline.empty(),
                Pipeline.of(4, 5)
            ),
            contains(1, 2, 3, 4, 5)
        );
    }
}