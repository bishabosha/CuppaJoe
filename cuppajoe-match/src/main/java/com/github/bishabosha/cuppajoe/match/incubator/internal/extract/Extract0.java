package com.github.bishabosha.cuppajoe.match.incubator.internal.extract;

import com.github.bishabosha.cuppajoe.higher.functions.Func1;
import com.github.bishabosha.cuppajoe.match.incubator.patterns.Pattern.Branch1;
import com.github.bishabosha.cuppajoe.match.incubator.patterns.Pattern.Branch2;
import com.github.bishabosha.cuppajoe.match.incubator.patterns.Pattern.Empty;
import com.github.bishabosha.cuppajoe.match.incubator.patterns.Pattern.Value;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.function.Predicate;

import static com.github.bishabosha.cuppajoe.match.incubator.internal.extract.Extractors.*;

public abstract class Extract0<I> extends Extractor {

    private boolean valuesOverflow = false;
    protected Predicate<I> matcher = neverMatch();
    private Deque<Func1<?, ?>> funcs = new ArrayDeque<>();

    {
        funcs.push(identity());
    }

    @SuppressWarnings("unchecked")
    protected <U, O> Func1<U, O> popFunc() {
        return (Func1<U, O>) funcs.pop();
    }

    @SuppressWarnings("unchecked")
    protected <U, O> void pushFunc(Func1<U, O> function) {
        funcs.push(function);
    }

    @Override
    protected boolean notInstantiated() {
        return valuesOverflow() || neverMatches(matcher);
    }

    final boolean valuesOverflow() {
        return valuesOverflow;
    }

    final void triggerValuesOverflow() {
        valuesOverflow = true;
    }

    @Override
    public final <T> void onEmpty(Empty<T> empty) {
        matcher = composePredicates(matcher, extractMatches(empty), popFunc());
    }

    @Override
    public <T> void onValue(Value<T> value) {
        popFunc();
        triggerValuesOverflow();
    }

    @Override
    public <O, Z> void onBranch1(Branch1<O, Z> branch) {
        Func1<I, O> func = popFunc();
        pushFunc(branch::extract1);
        matcher = composePredicates(matcher, branch::canBranch, func);
        branch.branch1().accept(this);
    }

    @Override
    public <O, Y, Z> void onBranch2(Branch2<O, Y, Z> branch) {
        Func1<I, O> func = popFunc();
        pushFunc(branch::extract2);
        pushFunc(branch::extract1);
        matcher = composePredicates(matcher, branch::canBranch, func);
        branch.branch1().accept(this);
        branch.branch2().accept(this);
    }
}
