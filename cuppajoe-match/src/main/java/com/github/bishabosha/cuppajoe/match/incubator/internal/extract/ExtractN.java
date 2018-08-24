package com.github.bishabosha.cuppajoe.match.incubator.internal.extract;

import com.github.bishabosha.cuppajoe.match.incubator.ExtractionFailedException;
import com.github.bishabosha.cuppajoe.match.incubator.Path;
import com.github.bishabosha.cuppajoe.match.incubator.patterns.Pattern.Branch;
import com.github.bishabosha.cuppajoe.match.incubator.patterns.Pattern.Empty;
import com.github.bishabosha.cuppajoe.match.incubator.patterns.Pattern.Value;

import static com.github.bishabosha.cuppajoe.match.incubator.internal.extract.Extractors.composePaths;

public class ExtractN extends Extractor {

    protected final Path[] paths;
    private final int size;
    private int cursor;

    public ExtractN(int size) {
        if (size < 0) {
            throw new IllegalArgumentException("size < 0");
        }
        cursor = 0;
        paths = new Path[size];
        this.size = size;
    }

    @Override
    protected boolean notInstantiated() {
        return super.notInstantiated() || cursor < size;
    }

    @Override
    public final <T> void onEmpty(Empty<T> empty) {
        appendPredicates(popPath(), empty);
    }

    @Override
    public <T> void onValue(Value<T> value) {
        var path = popPath();
        if (cursor >= size) {
            throw new ExtractionFailedException("To many values were found");
        }
        appendPredicates(path, value);
        paths[cursor++] = path;
    }

    @Override
    public final <O> void onBranch(Branch<O> branch) {
        var path = popPath();
        appendPredicates(path, branch);
        branch.pathsAscending().map(composePaths(path)).forEach(this::pushPath);
        branch.visitEachBranch(this);
    }
}
