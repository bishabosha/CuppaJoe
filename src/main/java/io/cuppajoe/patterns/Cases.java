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
    public interface NewCase<I, O> {
        Option<O> match(I input);
    }

    public interface Parent {}

    public interface Child<P> {}

//    public static <U> Case<Unapply0, U> when(Unapply0 o, Func0<U> ifMatch) {
//        return x -> Objects.equals(o, x) ? Some(ifMatch.apply()) : Nothing();
//    }
//
//    public static <I, U> Case<Unapply1<I>, U> when(Unapply1<I> o, Func1<I, U> ifMatch) {
//        return base(o, ifMatch.tupled());
//    }
//
//    public static <I, T, U> Case<Unapply2<I, T>, U> when(Unapply2<I, T> o, Func2<I, T, U> ifMatch) {
//        return base(o, ifMatch.tupled());
//    }

    public static
    <P extends Parent, U extends Unapply0 & Child<P>, O>
    NewCase<P, O>
    when(U o, Func0<O> ifMatch) {
        return x -> Objects.equals(o, x) ? Some(ifMatch.apply()) : Nothing();
    }

    public static
    <P extends Parent, A, U extends Unapply1<A> & Child<P>, O>
    NewCase<P, O>
    when(U o, Func1<A, O> ifMatch) {
        return base(o, ifMatch.tupled());
    }

    public static
    <P extends Parent, A, B, U extends Unapply2<A, B> & Child<P>, O>
    NewCase<P, O>
    when(U o, Func2<A, B, O> ifMatch) {
        return base(o, ifMatch.tupled());
    }

    public static <P extends Parent, O> NewCase<P, O> many(NewCase<P, O> first, NewCase<P, O> second) {
        return x -> first.match(x).or(() -> second.match(x));
    }

//    public static <I, O> NewCase<I, O> many(NewCase<I, O> first, NewCase<I, O> second, NewCase<I, O> third) {
//        return x -> first.match(x).map(o -> o).or(() -> second.match(x)).or(() -> third.match(x));
//    }

    private static
    <Z extends Parent, U extends Unapply<P>, O, P extends Product, F extends Func1<P, O>>
    NewCase<Z, O> base(U o, F ifMatch) {
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
