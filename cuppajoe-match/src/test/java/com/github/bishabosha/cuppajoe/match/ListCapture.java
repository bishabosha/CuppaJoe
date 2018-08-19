package com.github.bishabosha.cuppajoe.match;

import com.github.bishabosha.cuppajoe.collections.immutable.List;
import com.github.bishabosha.cuppajoe.match.patterns.Result;
import com.github.bishabosha.cuppajoe.match.patterns.ResultVisitor;

public class ListCapture<O> extends ResultVisitor {

    public static <O> List<O> capture(Result result) {
        var listCapture = new ListCapture<O>();
        result.accept(listCapture);
        return listCapture.getList();
    }

    private List<O> list = List.empty();

    @SuppressWarnings("unchecked")
    @Override
    public void onValue(Object a) {
        list = list.push((O) a);
    }

    @Override
    public boolean uninitialised() {
        return true;
    }

    private List<O> getList() {
        return list.reverse();
    }
}
