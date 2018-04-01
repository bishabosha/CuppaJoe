/*
 * Copyright (c) 2017. Jamie Thompson <bishbashboshjt@gmail.com>
 */

package io.cuppajoe.match;

import io.cuppajoe.control.Option;
import io.cuppajoe.tuples.*;

public class PatternFactory {

    public static Pattern gen0(Unapply0 target) {
        return x -> target.equals(x) ? Pattern.PASS : Pattern.FAIL;
    }

    public static Pattern gen1(Class<? extends Unapply1> target, Pattern p1) {
        return x -> Option.of(x)
          .filter(target::isInstance)
          .map(verified -> ((Unapply1<Object>)verified).unapply())
          .flatMap(values ->
              values.compose(p1::test));
    }

    public static Pattern gen2(Class<? extends Unapply2> target, Pattern p1, Pattern p2) {
        return x -> Option.of(x)
            .filter(target::isInstance)
            .map(verified -> ((Unapply2<Object, Object>)verified).unapply())
            .flatMap(values ->
                values.compose((v1, v2) ->
                    p1.test(v1).flatMap(r1 ->
                        p2.test(v2).map(r2 ->
                            Result.compose(r1, r2)))));
    }

    public static Pattern gen3(Class<? extends Unapply3> target, Pattern p1, Pattern p2, Pattern p3) {
        return x -> Option.of(x)
           .filter(target::isInstance)
           .map(verified -> ((Unapply3<Object, Object, Object>)verified).unapply())
           .flatMap(values ->
               values.compose((v1, v2, v3) ->
                   p1.test(v1).flatMap(r1 ->
                       p2.test(v2).flatMap(r2 ->
                           p3.test(v3).map(r3 ->
                               Result.compose(r1, r2, r3))))));
    }

    public static Pattern gen4(Class<? extends Unapply4> target, Pattern p1, Pattern p2, Pattern p3, Pattern p4) {
        return x -> Option.of(x)
            .filter(target::isInstance)
            .map(verified -> ((Unapply4<Object, Object, Object, Object>)verified).unapply())
            .flatMap(values ->
                values.compose((v1, v2, v3, v4) ->
                    p1.test(v1).flatMap(r1 ->
                        p2.test(v2).flatMap(r2 ->
                            p3.test(v3).flatMap(r3 ->
                                p4.test(v4).map(r4 ->
                                    Result.compose(r1, r2, r3, r4)))))));
    }

    public static Pattern gen5(Class<? extends Unapply5> target, Pattern p1, Pattern p2, Pattern p3, Pattern p4, Pattern p5) {
        return x -> Option.of(x)
            .filter(target::isInstance)
            .map(verified -> ((Unapply5<Object, Object, Object, Object, Object>)verified).unapply())
            .flatMap(values ->
                values.compose((v1, v2, v3, v4, v5) ->
                    p1.test(v1).flatMap(r1 ->
                        p2.test(v2).flatMap(r2 ->
                            p3.test(v3).flatMap(r3 ->
                                p4.test(v4).flatMap(r4 ->
                                    p5.test(v5).map(r5 ->
                                        Result.compose(r1, r2, r3, r4, r5))))))));
    }

    public static Pattern gen6(Class<? extends Unapply6> target, Pattern p1, Pattern p2, Pattern p3, Pattern p4, Pattern p5, Pattern p6) {
        return x -> Option.of(x)
            .filter(target::isInstance)
            .map(verified -> ((Unapply6<Object, Object, Object, Object, Object, Object>)verified).unapply())
            .flatMap(values ->
                values.compose((v1, v2, v3, v4, v5, v6) ->
                    p1.test(v1).flatMap(r1 ->
                        p2.test(v2).flatMap(r2 ->
                            p3.test(v3).flatMap(r3 ->
                                p4.test(v4).flatMap(r4 ->
                                    p5.test(v5).flatMap(r5 ->
                                        p6.test(v6).map(r6 ->
                                            Result.compose(r1, r2, r3, r4, r5, r6)))))))));
    }

    public static Pattern gen7(Class<? extends Unapply7> target, Pattern p1, Pattern p2, Pattern p3, Pattern p4, Pattern p5, Pattern p6, Pattern p7) {
        return x -> Option.of(x)
            .filter(target::isInstance)
            .map(verified -> ((Unapply7<Object, Object, Object, Object, Object, Object, Object>)verified).unapply())
            .flatMap(values ->
                values.compose((v1, v2, v3, v4, v5, v6, v7) ->
                    p1.test(v1).flatMap(r1 ->
                        p2.test(v2).flatMap(r2 ->
                            p3.test(v3).flatMap(r3 ->
                                p4.test(v4).flatMap(r4 ->
                                    p5.test(v5).flatMap(r5 ->
                                        p6.test(v6).flatMap(r6 ->
                                            p7.test(v7).map(r7 ->
                                                Result.compose(r1, r2, r3, r4, r5, r6, r7))))))))));
    }

    public static Pattern gen8(Class<? extends Unapply8> target, Pattern p1, Pattern p2, Pattern p3, Pattern p4, Pattern p5, Pattern p6, Pattern p7, Pattern p8) {
        return x -> Option.of(x)
            .filter(target::isInstance)
            .map(verified -> ((Unapply8<Object, Object, Object, Object, Object, Object, Object, Object>)verified).unapply())
            .flatMap(values ->
                values.compose((v1, v2, v3, v4, v5, v6, v7, v8) ->
                    p1.test(v1).flatMap(r1 ->
                        p2.test(v2).flatMap(r2 ->
                            p3.test(v3).flatMap(r3 ->
                                p4.test(v4).flatMap(r4 ->
                                    p5.test(v5).flatMap(r5 ->
                                        p6.test(v6).flatMap(r6 ->
                                            p7.test(v7).flatMap(r7 ->
                                                p8.test(v8).map(r8 ->
                                                    Result.compose(r1, r2, r3, r4, r5, r6, r7, r8)))))))))));
    }
}
