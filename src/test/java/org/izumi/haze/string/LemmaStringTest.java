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

    @Test
    public void getLemmaRangeBeforeTest() {
        LemmaString string = new LemmaString("I know that I know nothing?");
        assert string.getLemmaRangeBefore(new Range(2, 6)).get().equals(new Range(0, 0));
        assert string.getLemmaRangeBefore(new Range(8, 11)).get().equals(new Range(2, 5));
        assert string.getLemmaRangeBefore(new Range(21, 26)).get().equals(new Range(14, 17));

        Assertions.assertThrows(IndexOutOfBoundsException.class, () -> string.getLemmaRangeBefore(new Range(0, 999)));
    }

    @Test
    public void getFirstLemmaRangeTest() {
        assert new LemmaString("I know that I know nothing?").getFirstLemmaRange().get()
                .equals(new Range(0, 0));
        assert new LemmaString("An another string, hooray!").getFirstLemmaRange().get()
                .equals(new Range(0, 1));
        assert new LemmaString("  complex sample").getFirstLemmaRange().get()
                .equals(new Range(2, 8));

        LemmaString string = new LemmaString("something");
        assert string.getFirstLemmaRange().get().equals(string.getRange().get());
    }

    @Test
    public void getLemmaRangeAfterTest() {
        LemmaString string = new LemmaString("I know that I know nothing?");
        assert string.getLemmaRangeAfter(new Range(2, 6)).get().equals(new Range(7, 10));
        assert string.getLemmaRangeAfter(new Range(8, 11)).get().equals(new Range(12, 12));
        assert string.getLemmaRangeAfter(new Range(21, 26)).isEmpty();

        Assertions.assertThrows(IndexOutOfBoundsException.class, () -> string.getLemmaRangeAfter(new Range(0, 999)));
    }

    @Test
    public void getLemmaAfterTest() {
        LemmaString string = new LemmaString("I know that I know nothing?");
        assert string.getLemmaAfter(new Range(2, 6)).get().equals("that");
        assert string.getLemmaAfter(new Range(8, 11)).get().equals("I");
        assert string.getLemmaAfter(new Range(21, 26)).isEmpty();

        Assertions.assertThrows(IndexOutOfBoundsException.class, () -> string.getLemmaRangeAfter(new Range(0, 999)));
    }
}
