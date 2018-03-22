package io.cuppajoe.patterns;

import io.cuppajoe.API;
import io.cuppajoe.control.Option;
import io.cuppajoe.patterns.Cases.NewCase;
import org.junit.Assert;
import org.junit.Test;

import static io.cuppajoe.API.*;
import static io.cuppajoe.patterns.Cases.when;
import static org.junit.Assert.*;

public class CasesTest {

    @Test
    public void cases() {

        var matchSome = Cases.when(Some(1), (Integer elem) -> elem + 1);
        var matchNone = Cases.when(Nothing(), () -> "Nothing");

        assertTrue(matchNone.match(Some(1)).isEmpty());
        Assert.assertFalse(matchSome.match(Some(1)).isEmpty());

        var matchOption = Cases.many(
            when(Some(1), (Integer elem) -> elem + 1),
            when(Nothing(), () -> "Nothing")
        );

        assertTrue(matchOption.match(Success(1)).isEmpty());
        assertFalse(matchOption.match(Some(1)).isEmpty());

        assertTrue(matchOption.match(Tuple()).isEmpty());
        assertFalse(matchOption.match(Nothing()).isEmpty());

    }

}