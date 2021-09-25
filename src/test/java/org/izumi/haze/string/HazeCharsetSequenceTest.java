package org.izumi.haze.string;

import org.izumi.haze.util.Range;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class HazeCharsetSequenceTest {
    private static class Inheritor extends HazeCharSequence {
        public Inheritor(CharSequence sequence) {
            super(sequence);
        }
    }

    @Test
    public void lengthTest() {
        HazeCharSequence sequence1 = new Inheritor("abc");
        assert sequence1.length() == 3;

        HazeCharSequence sequence2 = new Inheritor("");
        assert sequence2.length() == 0;
    }

    @Test
    public void charAtTest() {
        HazeCharSequence sequence1 = new Inheritor("abc");
        assert sequence1.charAt(0) == 'a';
        assert sequence1.charAt(2) == 'c';
        Assertions.assertThrows(IndexOutOfBoundsException.class, () -> sequence1.charAt(999));

        HazeCharSequence sequence2 = new Inheritor("");
        Assertions.assertThrows(IndexOutOfBoundsException.class, () -> sequence2.charAt(0));
        Assertions.assertThrows(IndexOutOfBoundsException.class, () -> sequence2.charAt(-1));
    }

    @Test
    public void subSequenceTest() {
        HazeCharSequence sequence1 = new Inheritor("abc");
        HazeCharSequence sequence2 = new Inheritor("");
        assert sequence1.subSequence(0, 1).equals("a");

        Range range = sequence1.getRange().get();
        assert sequence1.subSequence(range).equals("abc");

        Assertions.assertThrows(IndexOutOfBoundsException.class, () -> sequence2.subSequence(new Range(0, 0)));
    }

    @Test
    public void isEmptyTest() {
        assert !new Inheritor("-").isEmpty();
        assert new Inheritor("").isEmpty();
    }

    @Test
    public void isNotEmptyTest() {
        assert new Inheritor("-").isNotEmpty();
        assert !new Inheritor("").isNotEmpty();
    }

    @Test
    public void getRangeTest() {
        HazeCharSequence sequence1 = new Inheritor("-");
        HazeCharSequence sequence2 = new Inheritor("");
        HazeCharSequence sequence3 = new HazeString("abcdefg");
        assert sequence1.getRange().get().equals(new Range(0, 0));
        assert sequence2.getRange().isEmpty();
        assert sequence3.getRange().get().equals(new Range(0, 6));
    }
}
