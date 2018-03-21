/*
 * Copyright (c) 2017. Jamie Thompson <bishbashboshjt@gmail.com>
 */

package io.cuppajoe.patterns;

import io.cuppajoe.control.Option;

import java.util.Objects;

import static io.cuppajoe.API.Nothing;
import static io.cuppajoe.API.Some;

public interface Pattern {

    Option<Result<Object>> test(Object obj);

    Option<Result<Object>> PASS = Some(Result.empty());

    Option<Result<Object>> FAIL = Nothing();

    static Option<Result<Object>> bind(Object x) {
        return Option.of(Result.of(x));
    }

    Pattern ¥LT = x -> x instanceof Integer && ((Integer)x) < 0 ? PASS : FAIL;

    Pattern ¥GT = x -> x instanceof Integer && ((Integer)x) > 0 ? PASS : FAIL;

    Pattern ¥EQ = x -> x instanceof Integer && ((Integer)x) == 0 ? PASS : FAIL;

    Pattern ¥true = x -> Objects.equals(x, true) ? PASS : FAIL;

    Pattern ¥false = x -> Objects.equals(x, false) ? PASS : FAIL;

    Pattern ¥null = x -> x == null ? PASS : FAIL;

    Pattern ¥_ = x -> PASS;

    Pattern $a = x -> bind(x);
    Pattern $b = $a;
    Pattern $c = $a;
    Pattern $d = $a;
    Pattern $e = $a;
    Pattern $f = $a;
    Pattern $g = $a;
    Pattern $h = $a;

    Pattern $n = $a;
    Pattern $l = $a;
    Pattern $r = $a;

    Pattern $x = $a;
    Pattern $y = $a;
    Pattern $z = $a;

    Pattern $xs = $a;
    Pattern $ys = $a;
    Pattern $zs = $a;

    static Pattern $RegEx(String regex) {
        return $RegEx(java.util.regex.Pattern.compile(regex));
    }

    static Pattern $RegEx(java.util.regex.Pattern pattern) {
        return x -> {
            if (x instanceof CharSequence) {
                var matcher = pattern.matcher((CharSequence) x);
                return matcher.matches() ? bind(x) : FAIL;
            }
            return FAIL;
        };
    }

    static Pattern $class(Class<?> clazz) {
        return x -> clazz.isInstance(x) ? bind(x) : FAIL;
    }

    static Pattern $(Object toMatch) {
        return x -> Objects.equals(x, toMatch) ? bind(x) : FAIL;
    }

    static Pattern $NOT(Object toMatch) {
        return x -> !Objects.equals(x, toMatch) ? bind(x) : FAIL;
    }

    static <R> Pattern $any(R... values) {
        return x -> {
            for (var val: values) {
                if (Objects.equals(x, val)) {
                    return bind(x);
                }
            }
            return FAIL;
        };
    }
}

