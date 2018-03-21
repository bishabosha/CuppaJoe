/*
 * Copyright (c) 2017. Jamie Thompson <bishbashboshjt@gmail.com>
 */

package io.cuppajoe.patterns;

import io.cuppajoe.control.Option;
import io.cuppajoe.functions.*;
import io.cuppajoe.tuples.*;

public class PatternFactory<I> {

    public static <U extends Unapply1<?>>
    Func1<Pattern, Pattern> gen1(Class<U> unapply1) {
        return $1 -> x -> Option.of(x)
            .cast(unapply1)
            .map(Unapply1::unapply)
            .flatMap(values -> $1.test(values.$1()));
    }

    public static <U extends Unapply2<?, ?>>
    Func2<Pattern, Pattern, Pattern> gen2(Class<U> unapply2) {
        return ($1, $2) -> x -> Option.of(x)
            .cast(unapply2)
            .map(Unapply2::unapply)
            .flatMap(
                values -> $1.test(values.$1()).flatMap(
                    a -> $2.test(values.$2()).map(
                        b -> Result.compose2().apply(a, b))));
    }

    public static <U extends Unapply3<?, ?, ?>>
    Func3<Pattern, Pattern, Pattern, Pattern> gen3(Class<U> unapply3) {
        return ($1, $2, $3) -> x -> Option.of(x)
           .cast(unapply3)
           .map(Unapply3::unapply)
           .flatMap(
               values -> $1.test(values.$1()).flatMap(
                   a -> $2.test(values.$2()).flatMap(
                       b -> $3.test(values.$3()).map(
                           c -> Result.compose3().apply(a, b, c)))));
    }

    public static <U extends Unapply4<?, ?, ?, ?>>
    Func4<Pattern, Pattern, Pattern, Pattern, Pattern> gen4(Class<U> unapply4) {
        return ($1, $2, $3, $4) -> x -> Option.of(x)
            .cast(unapply4)
            .map(Unapply4::unapply)
            .flatMap(
                values -> $1.test(values.$1()).flatMap(
                    a -> $2.test(values.$2()).flatMap(
                        b -> $3.test(values.$3()).flatMap(
                            c -> $4.test(values.$4()).map(
                                d -> Result.compose4().apply(a, b, c, d))))));
    }

    public static <U extends Unapply5<?, ?, ?, ?, ?>>
    Func5<Pattern, Pattern, Pattern, Pattern, Pattern, Pattern> gen5(Class<U> unapply5) {
        return ($1, $2, $3, $4, $5) -> x -> Option.of(x)
            .cast(unapply5)
            .map(Unapply5::unapply)
            .flatMap(
                values -> $1.test(values.$1()).flatMap(
                    a -> $2.test(values.$2()).flatMap(
                        b -> $3.test(values.$3()).flatMap(
                            c -> $4.test(values.$4()).flatMap(
                                d -> $5.test(values.$5()).map(
                                    e -> Result.compose5().apply(a, b, c, d, e)))))));
    }

    public static <U extends Unapply6<?, ?, ?, ?, ?, ?>>
    Func6<Pattern, Pattern, Pattern, Pattern, Pattern, Pattern, Pattern> gen6(Class<U> unapply6) {
        return ($1, $2, $3, $4, $5, $6) -> x -> Option.of(x)
            .cast(unapply6)
            .map(Unapply6::unapply)
            .flatMap(
                values -> $1.test(values.$1()).flatMap(
                    a -> $2.test(values.$2()).flatMap(
                        b -> $3.test(values.$3()).flatMap(
                            c -> $4.test(values.$4()).flatMap(
                                d -> $5.test(values.$5()).flatMap(
                                    e -> $6.test(values.$6()).map(
                                        f -> Result.compose6().apply(a, b, c, d, e, f))))))));
    }

    public static <U extends Unapply7<?, ?, ?, ?, ?, ?, ?>>
    Func7<Pattern, Pattern, Pattern, Pattern, Pattern, Pattern, Pattern, Pattern> gen7(Class<U> unapply7) {
        return ($1, $2, $3, $4, $5, $6, $7) -> x -> Option.of(x)
            .cast(unapply7)
            .map(Unapply7::unapply)
            .flatMap(
                values -> $1.test(values.$1()).flatMap(
                    a -> $2.test(values.$2()).flatMap(
                        b -> $3.test(values.$3()).flatMap(
                            c -> $4.test(values.$4()).flatMap(
                                d -> $5.test(values.$5()).flatMap(
                                    e -> $6.test(values.$6()).flatMap(
                                        f -> $7.test(values.$7()).map(
                                            g -> Result.compose7().apply(a, b, c, d, e, f, g)))))))));
    }

    public static <U extends Unapply8<?, ?, ?, ?, ?, ?, ?, ?>>
    Func8<Pattern, Pattern, Pattern, Pattern, Pattern, Pattern, Pattern, Pattern, Pattern> gen8(Class<U> unapply8) {
        return ($1, $2, $3, $4, $5, $6, $7, $8) -> x -> Option.of(x)
            .cast(unapply8)
            .map(Unapply8::unapply)
            .flatMap(
                values -> $1.test(values.$1()).flatMap(
                    a -> $2.test(values.$2()).flatMap(
                        b -> $3.test(values.$3()).flatMap(
                            c -> $4.test(values.$4()).flatMap(
                                d -> $5.test(values.$5()).flatMap(
                                    e -> $6.test(values.$6()).flatMap(
                                        f -> $7.test(values.$7()).flatMap(
                                            g -> $8.test(values.$8()).map(
                                                h -> Result.compose8().apply(a, b, c, d, e, f, g, h))))))))));
    }
}
