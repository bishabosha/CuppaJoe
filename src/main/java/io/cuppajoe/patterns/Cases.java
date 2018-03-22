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

        default O get(I input) throws MatchException {
            return match(input).orElseThrow(() -> new MatchException(input));
        }
    }

    public static class MatchException extends RuntimeException {
        public MatchException(Object input) {
            super("No match found for object: " + input);
        }
    }

    public interface Root {}

    public interface CaseOf<P> {}

    public static
    <U extends Unapply0 & CaseOf<R>, R extends Root, O>
    NewCase<R, O> caseOf(U o, Func0<O> ifMatch) {
        return x -> Objects.equals(o, x) ? Some(ifMatch.apply()) : Nothing();
    }

    public static
    <A, U extends Unapply1<A> & CaseOf<R>, R extends Root, O>
    NewCase<R, O> caseOf(U o, Func1<A, O> ifMatch) {
        return base(o, ifMatch.tupled());
    }

    public static
    <A, B, U extends Unapply2<A, B> & CaseOf<R>, R extends Root, O>
    NewCase<R, O> caseOf(U o, Func2<A, B, O> ifMatch) {
        return base(o, ifMatch.tupled());
    }

    @SuppressWarnings("unchecked")
    private static
    <I, O, U extends Unapply<P>, P extends Product, F extends Func1<P, O>>
    NewCase<I, O> base(U o, F ifMatch) {
        return x -> {
            var clazz = o.getClass();
            if (clazz.isInstance(x)) {
                var tupX = ((U) x).unapply();
                if (Objects.equals(tupX, o.unapply())) {
                    return Some(ifMatch.apply(tupX));
                }
            }
            return Nothing();
        };
    }

    public static <I, O> NewCase<I, O> many(
            NewCase<I, O> first,
            NewCase<I, O> second) {
        return x -> first.match(x).or(() -> second.match(x));
    }

    public static <I, O> NewCase<I, O> many(
            NewCase<I, O> first,
            NewCase<I, O> second,
            NewCase<I, O> third) {
        return x -> first.match(x).or(() -> second.match(x)).or(() -> third.match(x));
    }
}
