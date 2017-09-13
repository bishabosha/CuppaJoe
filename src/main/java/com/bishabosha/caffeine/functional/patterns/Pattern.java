/*
 * Copyright (c) 2017. Jamie Thompson <bishbashboshjt@gmail.com>
 */

package com.bishabosha.caffeine.functional.patterns;

import com.bishabosha.caffeine.functional.control.Option;

import java.util.Objects;

public interface Pattern {

    Option<PatternResult> test(Object obj);

    Option<PatternResult> PASS = Option.of(PatternResult.create());

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
        return x -> Objects.equals(x, toMatch) ? bind(x) : FAIL;
    }

    static Pattern $NOT(Object toMatch) {
        return x -> !Objects.equals(x, toMatch) ? bind(x) : FAIL;
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

