package io.cuppajoe.patterns;

import io.cuppajoe.control.Option;
import io.cuppajoe.functions.Func0;
import io.cuppajoe.functions.Func1;
import io.cuppajoe.functions.Func2;
import io.cuppajoe.tuples.*;

import java.util.Objects;

import static io.cuppajoe.API.Nothing;
import static io.cuppajoe.API.Some;

public final class Cases {

    @FunctionalInterface
    public interface Case<I, O> {
        Option<O> match(Object input);
    }

    public static <U> Case<Unapply0, U> when(Unapply0 o, Func0<U> ifMatch) {
        return x -> Objects.equals(o, x) ? Some(ifMatch.apply()) : Nothing();
    }

    public static <I, U> Case<Unapply1<I>, U> when(Unapply1<I> o, Func1<I, U> ifMatch) {
        return base(o, ifMatch.tupled());
    }

    public static <I, T, U> Case<Unapply2<I, T>, U> when(Unapply2<I, T> o, Func2<I, T, U> ifMatch) {
        return base(o, ifMatch.tupled());
    }

    public static <O, U, V> Case<Object, Object> many(Case<? extends Object, U> first, Case<? extends Object, V> second) {
        return x -> first.match(x).cast(Object.class).or(() -> second.match(x));
    }

    public static <O, U extends O, V extends O, T extends O> Case<Object, O> many(Case<? extends Object, U> first, Case<? extends Object, V> second, Case<? extends Object, T> third) {
        return x -> first.match(x).map(o -> (O)o).or(() -> second.match(x)).or(() -> third.match(x));
    }

    private static  <U extends Unapply<P>, O, P extends Product, F extends Func1<P, O>> Case<U, O> base(U o, F ifMatch) {
        return x -> {
            var clazz = o.getClass();
            if (clazz.isInstance(x)) {
                var tupX = clazz.cast(x).unapply();
                if (Objects.equals(tupX, o.unapply())) {
                    return Some(ifMatch.apply((P)tupX));
                }
            }
            return Nothing();
        };
    }
}
