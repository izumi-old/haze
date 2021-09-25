package org.izumi.haze.string;

import org.izumi.haze.util.Range;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class HazeStringTest {

    @Test
    public void getSub() {
        HazeString string = new HazeString("I know that I know nothing?");
        assert string.getSub(new Range(0, 5)).equals("I know");
        assert string.getSub(new Range(0, 0)).equals("I");
        assert string.getSub(string.getRange().get()).equals(string);
        Assertions.assertThrows(IndexOutOfBoundsException.class, () -> string.getSub(new Range(5, 27)));
        Assertions.assertThrows(IndexOutOfBoundsException.class, () -> string.getSub(new Range(28, 999)));
    }

    @Test
    public void replaceAllIfTest() {
        HazeString string = new HazeString("I know that I know nothing?");
        assert string.replaceAllIf("now", "won", s -> true).equals("I kwon that I kwon nothing?");
        assert string.replaceAllIf("now", "won", s -> false).equals("I know that I know nothing?");
    }
}
