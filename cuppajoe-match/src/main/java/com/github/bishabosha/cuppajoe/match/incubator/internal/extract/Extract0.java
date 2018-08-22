package com.github.bishabosha.cuppajoe.match.incubator.internal.extract;

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
    private Deque<Path<?, ?>> paths = new ArrayDeque<>();

    {
        paths.push(Path.identity());
    }

    @SuppressWarnings("unchecked")
    protected <U, O> Path<U, O> popPath() {
        return (Path<U, O>) paths.pop();
    }

    protected <U, O> void pushPath(Path<U, O> function) {
        paths.push(function);
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
        matcher = composePredicates(matcher, extractMatches(popPath(), empty));
    }

    @Override
    public <T> void onValue(Value<T> value) {
        popPath();
        triggerValuesOverflow();
    }

    @Override
    public <O, Z> void onBranch1(Branch1<O, Z> branch) {
        Path<I, O> path = popPath();
        pushPath(composePaths(path, branch::extract1));
        matcher = composePredicates(matcher, composeWithPath(path, branch::canBranch));
        branch.branch1().accept(this);
    }

    @Override
    public <O, Y, Z> void onBranch2(Branch2<O, Y, Z> branch) {
        Path<I, O> path = popPath();
        pushPath(composePaths(path, branch::extract2));
        pushPath(composePaths(path, branch::extract1));
        matcher = composePredicates(matcher, composeWithPath(path, branch::canBranch));
        branch.branch1().accept(this);
        branch.branch2().accept(this);
    }
}
