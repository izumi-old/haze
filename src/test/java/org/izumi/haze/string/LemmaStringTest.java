package org.izumi.haze.string;

import org.izumi.haze.util.Range;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class LemmaStringTest {

    @Test
    public void getSubTest() {
        LemmaString string = new LemmaString("I know that I know nothing?");
        assert string.getSub(new Range(0, 5)).equals("I know");
        assert string.getSub(new Range(0, 0)).equals("I");
        assert string.getSub(string.getRange().get()).equals(string);
        Assertions.assertThrows(IndexOutOfBoundsException.class, () -> string.getSub(new Range(5, 27)));
        Assertions.assertThrows(IndexOutOfBoundsException.class, () -> string.getSub(new Range(28, 999)));
    }

    @Test
    public void getLemmaBeforeTest() {
        LemmaString string = new LemmaString("I know that I know nothing?");
        assert string.getLemmaBefore(new Range(2, 6)).get().equals("I");
        assert string.getLemmaBefore(new Range(8, 11)).get().equals("know");
        assert string.getLemmaBefore(new Range(21, 26)).get().equals("know");

        Assertions.assertThrows(IndexOutOfBoundsException.class, () -> string.getLemmaBefore(new Range(0, 999)));
    }
}
