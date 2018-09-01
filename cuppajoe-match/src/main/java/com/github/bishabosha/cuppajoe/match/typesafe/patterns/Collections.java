package com.github.bishabosha.cuppajoe.match.typesafe.patterns;

import com.github.bishabosha.cuppajoe.collections.immutable.tuples.*;
import com.github.bishabosha.cuppajoe.control.Option;
import com.github.bishabosha.cuppajoe.control.Option.Some;
import com.github.bishabosha.cuppajoe.match.typesafe.patterns.Pattern.*;
import com.github.bishabosha.cuppajoe.match.typesafe.patterns.Pattern.Branch;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;

import static com.github.bishabosha.cuppajoe.API.None;
import static com.github.bishabosha.cuppajoe.collections.immutable.API.Tuple;
import static com.github.bishabosha.cuppajoe.match.typesafe.patterns.Pattern.*;
import static com.github.bishabosha.cuppajoe.match.typesafe.patterns.Standard.is;

public class Collections {

    private static class OptionHandles {
        private static final MethodHandle VALUE;

        static {
            try {
                VALUE = MethodHandles.lookup().findGetter(Some.class, "value", Object.class);
            } catch (NoSuchFieldException | IllegalAccessException e) {
                throw new Error(e);
            }
        }
    }

    public static Branch some(Pattern value) {
        return branch1(Option.Some.class, value, OptionHandles.VALUE);
    }

    public static Empty none() {
        return is(None());
    }

    private static class Tuple2Handles {
        private static final MethodHandle $1;
        private static final MethodHandle $2;

        static {
            try {
                $1 = MethodHandles.lookup().findGetter(Tuple2.class, "$1", Object.class);
                $2 = MethodHandles.lookup().findGetter(Tuple2.class, "$2", Object.class);
            } catch (NoSuchFieldException | IllegalAccessException e) {
                throw new Error(e);
            }
        }
    }

    public static Branch tuple(Pattern $1, Pattern $2) {
        return branchN(
            Tuple2.class,
            Tuple($1, Tuple2Handles.$1),
            Tuple($2, Tuple2Handles.$2)
        );
    }

    private static class Tuple8Handles {
        private static final MethodHandle $1;
        private static final MethodHandle $2;
        private static final MethodHandle $3;
        private static final MethodHandle $4;
        private static final MethodHandle $5;
        private static final MethodHandle $6;
        private static final MethodHandle $7;
        private static final MethodHandle $8;

        static {
            try {
                $1 = MethodHandles.lookup().findGetter(Tuple8.class, "$1", Object.class);
                $2 = MethodHandles.lookup().findGetter(Tuple8.class, "$2", Object.class);
                $3 = MethodHandles.lookup().findGetter(Tuple8.class, "$3", Object.class);
                $4 = MethodHandles.lookup().findGetter(Tuple8.class, "$4", Object.class);
                $5 = MethodHandles.lookup().findGetter(Tuple8.class, "$5", Object.class);
                $6 = MethodHandles.lookup().findGetter(Tuple8.class, "$6", Object.class);
                $7 = MethodHandles.lookup().findGetter(Tuple8.class, "$7", Object.class);
                $8 = MethodHandles.lookup().findGetter(Tuple8.class, "$8", Object.class);
            } catch (NoSuchFieldException | IllegalAccessException e) {
                throw new Error(e);
            }
        }
    }

    public static Branch tuple(Pattern $1, Pattern $2, Pattern $3, Pattern $4, Pattern $5, Pattern $6, Pattern $7, Pattern $8) {
        return branchN(
            Tuple8.class,
            Tuple($1, Tuple8Handles.$1),
            Tuple($2, Tuple8Handles.$2),
            Tuple($3, Tuple8Handles.$3),
            Tuple($4, Tuple8Handles.$4),
            Tuple($5, Tuple8Handles.$5),
            Tuple($6, Tuple8Handles.$6),
            Tuple($7, Tuple8Handles.$7),
            Tuple($8, Tuple8Handles.$8)
        );
    }
}
