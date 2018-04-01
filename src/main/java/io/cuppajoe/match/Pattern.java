/*
 * Copyright (c) 2017. Jamie Thompson <bishbashboshjt@gmail.com>
 */

package io.cuppajoe.match;

import io.cuppajoe.Lazy;
import io.cuppajoe.Unit;
import io.cuppajoe.collections.immutable.List;
import io.cuppajoe.collections.immutable.List.Cons;
import io.cuppajoe.collections.immutable.Tree;
import io.cuppajoe.collections.immutable.Tree.Node;
import io.cuppajoe.control.Option;
import io.cuppajoe.control.Option.Some;
import io.cuppajoe.control.Try.Failure;
import io.cuppajoe.control.Try.Success;

import java.util.Objects;

import static io.cuppajoe.API.None;
import static io.cuppajoe.API.Some;

public interface Pattern {

    Option<Result<Object>> test(Object obj);

    Option<Result<Object>> PASS = Some(Result.empty());

    Option<Result<Object>> FAIL = None();

    static Option<Result<Object>> bind(Object x) {
        return Option.of(Result.of(x));
    }

    static Pattern $Node(Pattern node, Pattern left, Pattern right) {
        return PatternFactory.gen3(Node.class, node, left, right);
    }

    Pattern $Leaf = PatternFactory.gen0(Tree.Leaf.INSTANCE);

    static Pattern $Cons(Pattern $x, Pattern $xs) {
        return PatternFactory.gen2(Cons.class, $x, $xs);
    }

    Pattern $Nil = PatternFactory.gen0(List.Nil.INSTANCE);

    static Pattern $Success(Pattern pattern) {
        return PatternFactory.gen1(Success.class, pattern);
    }

    static Pattern $Failure(Pattern error) {
        return PatternFactory.gen1(Failure.class, error);
    }

    static Pattern $Lazy(Pattern pattern) {
        return PatternFactory.gen1(Lazy.class, pattern);
    }

    static Pattern $Some(Pattern pattern) {
        return PatternFactory.gen1(Some.class, pattern);
    }

    Pattern $None = PatternFactory.gen0(Option.None.INSTANCE);

    Pattern $Unit = PatternFactory.gen0(Unit.INSTANCE);

    Pattern $LT = x -> x instanceof Integer && ((Integer)x) < 0 ? PASS : FAIL;

    Pattern $GT = x -> x instanceof Integer && ((Integer)x) > 0 ? PASS : FAIL;

    Pattern $EQ = x -> x instanceof Integer && ((Integer)x) == 0 ? PASS : FAIL;

    Pattern $_ = x -> PASS;

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

    static Pattern $_(Object toMatch) {
        return x -> Objects.equals(x, toMatch) ? PASS : FAIL;
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

