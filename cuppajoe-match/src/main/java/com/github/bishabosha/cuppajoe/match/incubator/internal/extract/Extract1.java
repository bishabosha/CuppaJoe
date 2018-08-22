package com.github.bishabosha.cuppajoe.match.incubator.internal.extract;

import com.github.bishabosha.cuppajoe.higher.functions.Func1;
import com.github.bishabosha.cuppajoe.match.incubator.patterns.Pattern.Branch1;
import com.github.bishabosha.cuppajoe.match.incubator.patterns.Pattern.Branch2;
import com.github.bishabosha.cuppajoe.match.incubator.patterns.Pattern.Value;

import static com.github.bishabosha.cuppajoe.match.incubator.internal.extract.Extractors.*;

public class Extract1<I, A> extends Extract0<I> {
    protected Path<I, A> path1 = toNowhere();

    @Override
    protected final boolean notInstantiated() {
        return super.notInstantiated() || pathsNotInstantiated();
    }

    protected boolean pathsNotInstantiated() {
        return !path1.isComplete();
    }

    @Override
    public <T> void onValue(Value<T> value) {
        matcher = composePredicates(matcher, extractMatches(value), popFunc());
        if (pathsNotInstantiated()) {
            path1 = completePath(path1);
        } else {
            triggerValuesOverflow();
        }
    }

    @Override
    public <O, Z> void onBranch1(Branch1<O, Z> branch) {
        Func1<I, O> func = popFunc();
        pushFunc(branch::extract1);
        matcher = composePredicates(matcher, branch::canBranch, func);
        if (pathsNotInstantiated()) {
            path1 = composePaths(path1, branch::extract1);
        }
        branch.branch1().accept(this);
    }

    @Override
    public <O, Y, Z> void onBranch2(Branch2<O, Y, Z> branch) {
        Func1<I, O> func = popFunc();
        pushFunc(branch::extract2);
        pushFunc(branch::extract1);
        matcher = composePredicates(matcher, branch::canBranch, func);
        var pathBackup = path1;
        if (pathsNotInstantiated()) {
            path1 = composePaths(path1, branch::extract1);
        }
        branch.branch1().accept(this);
        if (pathsNotInstantiated()) {
            path1 = composePaths(pathBackup, branch::extract2);
        }
        branch.branch2().accept(this);
    }
}
