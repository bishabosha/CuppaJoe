package com.github.bishabosha.cuppajoe.match.incubator.internal.extract;

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
        var path = popPath();
        @SuppressWarnings("unchecked") var matchPath = (Path<I, T>) path;
        matcher = composePredicates(matcher, extractMatches(matchPath, value));
        if (pathsNotInstantiated()) {
            @SuppressWarnings("unchecked") var extractPath = (Path<I, A>) path;
            path1 = completePath(extractPath);
        } else {
            triggerValuesOverflow();
        }
    }
}
