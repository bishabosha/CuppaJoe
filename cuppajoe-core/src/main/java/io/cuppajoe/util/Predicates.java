/*
 * Copyright (c) 2017. Jamie Thompson <bishbashboshjt@gmail.com>
 */

/*
 * Copyright (c) 2017. Jamie Thompson <bishbashboshjt@gmail.com>
 */

package io.cuppajoe.util;

import io.cuppajoe.annotation.NonNull;

import java.util.HashSet;
import java.util.Objects;
import java.util.function.Function;
import java.util.function.Predicate;

public final class Predicates {

    private Predicates() {
    }

    public static <E, O> Predicate<E> distinctProperty(@NonNull Function<E, O> propertyGetter) {
        Objects.requireNonNull(propertyGetter, "propertyGetter");
        var distinctProperties = new HashSet<O>();
        return pojo -> {
            var property = propertyGetter.apply(pojo);
            var notFound = !distinctProperties.contains(property);
            if (notFound) {
                distinctProperties.add(property);
            }
            return notFound;
        };
    }
}