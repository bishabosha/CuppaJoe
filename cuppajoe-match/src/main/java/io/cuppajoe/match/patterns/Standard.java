package io.cuppajoe.match.patterns;

import io.cuppajoe.control.Option;
import io.cuppajoe.match.PatternFactory;
import io.cuppajoe.match.Result;
import io.cuppajoe.tuples.*;

import java.util.Objects;

import static io.cuppajoe.API.None;
import static io.cuppajoe.API.Some;

public final class Standard {

    public static final Option<Result> PASS = Some(Result.empty());

    public static final Option<Result> FAIL = None();

    public static final Pattern $LT = x -> x instanceof Integer && ((Integer)x) < 0 ? PASS : FAIL;
    public static final Pattern $GT = x -> x instanceof Integer && ((Integer)x) > 0 ? PASS : FAIL;
    public static final Pattern $EQ = x -> x instanceof Integer && ((Integer)x) == 0 ? PASS : FAIL;

    public static final Pattern $_ = x -> PASS;

    public static final Pattern $a = x -> bind(x);
    public static final Pattern $b = $a;
    public static final Pattern $c = $a;
    public static final Pattern $d = $a;
    public static final Pattern $e = $a;
    public static final Pattern $f = $a;
    public static final Pattern $g = $a;
    public static final Pattern $h = $a;
    public static final Pattern $n = $a;
    public static final Pattern $l = $a;
    public static final Pattern $r = $a;
    public static final Pattern $x = $a;
    public static final Pattern $y = $a;
    public static final Pattern $z = $a;
    public static final Pattern $xs = $a;
    public static final Pattern $ys = $a;
    public static final Pattern $zs = $a;

    public static final Pattern $Unit = PatternFactory.gen0(Unit.INSTANCE);

    public static Pattern $Tuple1(Pattern $1) {
        return PatternFactory.gen1(Tuple1.class, $1);
    }

    public static Pattern $Tuple2(Pattern $1, Pattern $2) {
        return PatternFactory.gen2(Tuple2.class, $1, $2);
    }

    public static Pattern $Tuple3(Pattern $1, Pattern $2, Pattern $3) {
        return PatternFactory.gen3(Tuple3.class, $1, $2, $3);
    }

    public static Pattern $Tuple4(Pattern $1, Pattern $2, Pattern $3, Pattern $4) {
        return PatternFactory.gen4(Tuple4.class, $1, $2, $3, $4);
    }

    public static Pattern $Tuple5(Pattern $1, Pattern $2, Pattern $3, Pattern $4, Pattern $5) {
        return PatternFactory.gen5(Tuple5.class, $1, $2, $3, $4, $5);
    }

    public static Pattern $Tuple6(Pattern $1, Pattern $2, Pattern $3, Pattern $4, Pattern $5, Pattern $6) {
        return PatternFactory.gen6(Tuple6.class, $1, $2, $3, $4, $5, $6);
    }

    public static Pattern $Tuple7(Pattern $1, Pattern $2, Pattern $3, Pattern $4, Pattern $5, Pattern $6, Pattern $7) {
        return PatternFactory.gen7(Tuple7.class, $1, $2, $3, $4, $5, $6, $7);
    }

    public static Pattern $Tuple8(Pattern $1, Pattern $2, Pattern $3, Pattern $4, Pattern $5, Pattern $6, Pattern $7, Pattern $8) {
        return PatternFactory.gen8(Tuple8.class, $1, $2, $3, $4, $5, $6, $7, $8);
    }

    public static Option<Result> bind(Object $x) {
        return Option.of(Result.of($x));
    }

    public static Pattern $RegEx(String regex) {
        return $RegEx(java.util.regex.Pattern.compile(regex));
    }

    public static Pattern $RegEx(java.util.regex.Pattern pattern) {
        return x -> {
            if (x instanceof CharSequence) {
                var matcher = pattern.matcher((CharSequence) x);
                return matcher.matches() ? bind(x) : FAIL;
            }
            return FAIL;
        };
    }

    public static Pattern $class(Class<?> clazz) {
        return x -> clazz.isInstance(x) ? bind(x) : FAIL;
    }

    public static Pattern $(Object toMatch) {
        return x -> Objects.equals(x, toMatch) ? bind(x) : FAIL;
    }

    public static Pattern $_(Object toMatch) {
        return x -> Objects.equals(x, toMatch) ? PASS : FAIL;
    }

    public static <R> Pattern $any(R... values) {
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
