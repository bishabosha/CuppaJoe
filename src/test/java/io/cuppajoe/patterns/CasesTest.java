package io.cuppajoe.patterns;

import io.cuppajoe.patterns.Cases.MatchException;
import org.junit.Test;

import static io.cuppajoe.API.*;
import static io.cuppajoe.patterns.Cases.caseOf;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class CasesTest {

    @Test
    public void cases() {

        var matchOption = Cases.many(
            caseOf(Some(1), elem -> elem + 1),
            caseOf(Nothing(), () -> 0)
        );

        var matchList = Cases.many(
            caseOf(Cons(1, List()), (head, tail) -> head + " : " + tail),
            caseOf(List(), () -> "[]")
        );

        assertEquals("1 : []", matchList.get(Cons(1, List())));
        assertEquals("[]", matchList.get(List()));
        assertEquals(Nothing(), matchList.match(List(5)));

        assertEquals(2, matchOption.get(Some(1)).intValue());
        assertEquals(0, matchOption.get(Nothing()).intValue());
        assertEquals(Nothing(), matchOption.match(Some(2)));

        assertThrows(MatchException.class, () -> matchOption.get(Some(2)), "No match found for object: Some(2)");
    }

}