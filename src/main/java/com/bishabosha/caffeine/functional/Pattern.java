/*
 * Copyright (c) 2017. Jamie Thompson <bishbashboshjt@gmail.com>
 */

package com.bishabosha.caffeine.functional;

import com.bishabosha.caffeine.functional.tuples.Tuple;

import java.util.Objects;
import java.util.Optional;
import java.util.function.BooleanSupplier;
import java.util.function.Predicate;

public interface Pattern {

    Option<PatternResult> test(Object obj);

    Option<PatternResult> PASS = Option.of(PatternResult.of());

    Option<PatternResult> FAIL = Option.nothing();

    static Option<PatternResult> bind(Object x) {
        return Option.of(PatternResult.of(x));
    }

    static Option<PatternResult> bind(Object... xs) {
        return Option.of(PatternResult.of(xs));
    }

    static Option<PatternResult> compile(PatternResult result) {
        return Option.of(result.flatten());
    }

    Pattern ¥LT = x -> x instanceof Integer && ((Integer)x) < 0 ? PASS : FAIL;

    Pattern ¥GT = x -> x instanceof Integer && ((Integer)x) > 0 ? PASS : FAIL;

    Pattern ¥EQ = x -> x instanceof Integer && ((Integer)x) == 0 ? PASS : FAIL;

    Pattern ¥true = x -> x.equals(true) ? PASS : FAIL;

    Pattern ¥false = x -> x.equals(false) ? PASS : FAIL;

    Pattern ¥nil = x -> x == null || Optional.empty().equals(x) || x == Tuple.EMPTY ? bind(x) : FAIL;

    Pattern $null = x -> x == null ? bind(x) : FAIL;

    Pattern ¥_ = x -> PASS;
    Pattern $a = x -> bind(x);
    Pattern $b = $a;
    Pattern $c = $a;
    Pattern $d = $a;
    Pattern $e = $a;
    Pattern $f = $a;
    Pattern $g = $a;
    Pattern $h = $a;
    Pattern $i = $a;
    Pattern $j = $a;
    Pattern $k = $a;
    Pattern $l = $a;
    Pattern $n = $a;
    Pattern $r = $a;
    Pattern $s = $a;
    Pattern $w = $a;
    Pattern $x = $a;
    Pattern $y = $a;
    Pattern $z = $a;
    Pattern $ws = $a;
    Pattern $xs = $a;
    Pattern $ys = $a;
    Pattern $zs = $a;

    static Pattern $RegEx(String regex) {
        return $RegEx(java.util.regex.Pattern.compile(regex));
    }

    static Pattern $RegEx(java.util.regex.Pattern pattern) {
        return x -> {
            if (x instanceof CharSequence) {
                java.util.regex.Matcher matcher = pattern.matcher((CharSequence) x);
                return matcher.matches() ? bind(x) : FAIL;
            }
            return FAIL;
        };
    }

    static Pattern instance(Class<?> clazz) {
        return x -> clazz.isInstance(x) ? bind(x) : FAIL;
    }

    static Pattern $(Object toMatch) {
        return x -> x != null && x.equals(toMatch) ? bind(x) : FAIL;
    }

    static Pattern $NOT(Object toMatch) {
        return x -> x != null && !x.equals(toMatch) ? bind(x) : FAIL;
    }

    static Pattern $(Predicate toTest) {
        return x -> toTest.test(x) ? bind(x) : FAIL;
    }

    static Pattern ¥(Predicate toTest) {
        return x -> toTest.test(x) ? PASS : FAIL;
    }

    static Pattern ¥(BooleanSupplier toTest) {
        return x -> toTest.getAsBoolean() ? PASS : FAIL;
    }

    static <R> Pattern $any(R... values) {
        return x -> {
            for (R val: values) {
                if (Objects.equals(x, val)) {
                    return bind(x);
                }
            }
            return FAIL;
        };
    }
}

