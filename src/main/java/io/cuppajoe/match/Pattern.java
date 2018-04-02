/*
 * Copyright (c) 2017. Jamie Thompson <bishbashboshjt@gmail.com>
 */

package io.cuppajoe.match;

import io.cuppajoe.Lazy;
import io.cuppajoe.collections.immutable.List;
import io.cuppajoe.collections.immutable.List.Cons;
import io.cuppajoe.collections.immutable.Tree;
import io.cuppajoe.collections.immutable.Tree.Node;
import io.cuppajoe.control.Option;
import io.cuppajoe.control.Option.Some;
import io.cuppajoe.control.Try.Failure;
import io.cuppajoe.control.Try.Success;
import io.cuppajoe.tuples.*;

import java.util.Objects;

import static io.cuppajoe.API.None;
import static io.cuppajoe.API.Some;

public interface Pattern {

    Option<Result<Object>> test(Object obj);

    Option<Result<Object>> PASS = Some(Result.empty());

    Option<Result<Object>> FAIL = None();

    static Option<Result<Object>> bind(Object $x) {
        return Option.of(Result.of($x));
    }

    static Pattern $Node(Pattern $node, Pattern $left, Pattern $right) {
        return PatternFactory.gen3(Node.class, $node, $left, $right);
    }

    Pattern $Leaf = PatternFactory.gen0(Tree.Leaf.INSTANCE);

    static Pattern $Cons(Pattern $x, Pattern $xs) {
        return PatternFactory.gen2(Cons.class, $x, $xs);
    }

    Pattern $Nil = PatternFactory.gen0(List.Nil.INSTANCE);

    static Pattern $Success(Pattern $value) {
        return PatternFactory.gen1(Success.class, $value);
    }

    static Pattern $Failure(Pattern $error) {
        return PatternFactory.gen1(Failure.class, $error);
    }

    static Pattern $Lazy(Pattern $value) {
        return PatternFactory.gen1(Lazy.class, $value);
    }

    static Pattern $Some(Pattern $value) {
        return PatternFactory.gen1(Some.class, $value);
    }

    Pattern $None = PatternFactory.gen0(Option.None.INSTANCE);

    Pattern $Unit = PatternFactory.gen0(Unit.INSTANCE);

    static Pattern $Tuple1(Pattern $1) {
        return PatternFactory.gen1(Tuple1.class, $1);
    }

    static Pattern $Tuple2(Pattern $1, Pattern $2) {
        return PatternFactory.gen2(Tuple2.class, $1, $2);
    }

    static Pattern $Tuple3(Pattern $1, Pattern $2, Pattern $3) {
        return PatternFactory.gen3(Tuple3.class, $1, $2, $3);
    }

    static Pattern $Tuple4(Pattern $1, Pattern $2, Pattern $3, Pattern $4) {
        return PatternFactory.gen4(Tuple4.class, $1, $2, $3, $4);
    }

    static Pattern $Tuple5(Pattern $1, Pattern $2, Pattern $3, Pattern $4, Pattern $5) {
        return PatternFactory.gen5(Tuple5.class, $1, $2, $3, $4, $5);
    }

    static Pattern $Tuple6(Pattern $1, Pattern $2, Pattern $3, Pattern $4, Pattern $5, Pattern $6) {
        return PatternFactory.gen6(Tuple6.class, $1, $2, $3, $4, $5, $6);
    }

    static Pattern $Tuple7(Pattern $1, Pattern $2, Pattern $3, Pattern $4, Pattern $5, Pattern $6, Pattern $7) {
        return PatternFactory.gen7(Tuple7.class, $1, $2, $3, $4, $5, $6, $7);
    }

    static Pattern $Tuple8(Pattern $1, Pattern $2, Pattern $3, Pattern $4, Pattern $5, Pattern $6, Pattern $7, Pattern $8) {
        return PatternFactory.gen8(Tuple8.class, $1, $2, $3, $4, $5, $6, $7, $8);
    }

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

