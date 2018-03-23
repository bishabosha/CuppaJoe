package io.cuppajoe.patterns;

import io.cuppajoe.API;
import io.cuppajoe.control.Option;
import io.cuppajoe.functions.Func0;
import io.cuppajoe.functions.Func1;
import io.cuppajoe.functions.Func2;
import io.cuppajoe.tuples.*;

import java.util.Objects;

import static io.cuppajoe.API.Nothing;
import static io.cuppajoe.API.Some;

public final class Cases {

    public static class MatchException extends RuntimeException {
        public MatchException(Object input) {
            super("No match found for object: " + input);
        }
    }

    public interface Root1 {
    }

    public interface CaseOf<P> {}

    public static
    <U extends Unapply0 & CaseOf<R>, R extends Root1, O>
    Case<R, O> caseOf(U o, Func0<O> ifMatch) {
        return x -> Objects.equals(o, x) ? Some(ifMatch.apply()) : Nothing();
    }

    public static
    <A, U extends Unapply1<A> & CaseOf<R>, R extends Root1, O>
    Case<R, O> caseOf(U o, Func1<A, O> ifMatch) {
        return base(o, ifMatch.tupled());
    }

    public static
    <A, B, U extends Unapply2<A, B> & CaseOf<R>, R extends Root1, O>
    Case<R, O> caseOf(U o, Func2<A, B, O> ifMatch) {
        return base(o, ifMatch.tupled());
    }

    @SuppressWarnings("unchecked")
    private static
    <I, O, U extends Unapply<P>, P extends Product, F extends Func1<P, O>>
    Case<I, O> base(U o, F ifMatch) {
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

    public static <I, O> Case<I, O> many(
            Case<I, O> first,
            Case<I, O> second) {
        return x -> first.match(x).or(() -> second.match(x));
    }

    public static <I, O> Case<I, O> many(
            Case<I, O> first,
            Case<I, O> second,
            Case<I, O> third) {
        return x -> first.match(x).or(() -> second.match(x)).or(() -> third.match(x));
    }

    public interface Capture<O> {
        Option<O> get();
        Option<Product1<O>> test(Capture<O> value);
    }

    public static <U> Capture<U> $() {
        return new Capture<>() {
            @Override
            public Option<U> get() {
                return Nothing();
            }

            @Override
            public Option<Product1<U>> test(Capture<U> value) {
                return value.get().map(API::Tuple);
            }
        };
    }

    public static <U> Capture<U> $(U elem) {
        return new Capture<>() {
            @Override
            public Option<U> get() {
                return Some(elem);
            }

            @Override
            public Option<Product1<U>> test(Capture<U> value) {
                return Objects.equals(get(), value.get()) ? get().map(API::Tuple) : Nothing();
            }
        };
    }

    public static
    <A, U extends Unapply1<Capture<A>> & CaseOf<R>, R extends Root1, O>
    Case<R, O> casePatt(U o, Func1<A, O> ifMatch) {
        return x -> {
            var clazz = o.getClass();
            if (clazz.isInstance(x)) {
                var tupX = ((U) x).unapply();
                return o.unapply()
                    .$1()
                    .test(tupX.$1())
                    .map(ifMatch.tupled());
            }
            return Nothing();
        };
    }
}
