package io.cuppajoe.patterns;

import org.junit.Test;

import static io.cuppajoe.API.*;
import static io.cuppajoe.patterns.Cases.when;
import static org.junit.Assert.*;

public class CasesTest {

    @Test
    public void cases() {

        var matchSome = when(Some(1), (Integer elem) -> elem + 1);
        var matchNone = when(Nothing(), () -> "Nothing");

        var matchOption = Cases.many(
            when(Some(1), (Integer elem) -> elem + 1),
            when(Nothing(), () -> "Nothing")
        );

        var matchOptionOrList = Cases.many(
            when(Some(1), (Integer elem) -> elem + 1),
            when(Nothing(), () -> 0),
            when(List(1, 2), (head, tail) -> head + 1)
        );

        assertTrue(matchSome.match(Success(1)).isEmpty());
        assertFalse(matchSome.match(Some(1)).isEmpty());

        assertTrue(matchNone.match(Tuple()).isEmpty());
        assertFalse(matchNone.match(Nothing()).isEmpty());

        assertTrue(matchOption.match(Some(2)).isEmpty());
        assertEquals(Some(2), matchOption.match(Some(1)));
        assertEquals(Some("Nothing"), matchOption.match(Nothing()));

        assertTrue(matchOptionOrList.match(List()).isEmpty());
        assertEquals(Some(2), matchOptionOrList.match(List(1, 2)));

    }

}