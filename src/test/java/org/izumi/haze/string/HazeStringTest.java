package org.izumi.haze.string;

import org.izumi.haze.util.Range;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;

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
    public void firstRangeOfTest() {
        HazeString string = new HazeString("I know that I know nothing?");
        assert string.firstRangeOf("know").get().equals(new Range(2, 5));
        assert string.firstRangeOf("know", 6).get().equals(new Range(14, 17));
        assert string.firstRangeOf(List.of("I", "that"), 0).get().equals(new Range(0, 0));
        assert string.firstRangeOf("I", new Range(2, string.getRange().get().end)).get()
                .equals(new Range(12, 12));

        assert string.firstRangeOf("something").isEmpty();
        assert string.firstRangeOf("II").isEmpty();
        assert string.firstRangeOf("  ").isEmpty();
    }

    @Test
    public void lastRangeOfTest() {
        HazeString string = new HazeString("I know that I know nothing?");
        assert string.lastRangeOf("know", new Range(0, 10)).get().equals(new Range(2, 5));
        assert string.lastRangeOf("know").get().equals(new Range(14, 17));
        assert string.lastRangeOf(List.of("I", "that"), string.getRange().get()).get().equals(new Range(12, 12));
        assert string.lastRangeOf("I").get().equals(new Range(12, 12));

        assert string.firstRangeOf("something").isEmpty();
        assert string.firstRangeOf("II").isEmpty();
        assert string.firstRangeOf("  ").isEmpty();
    }

    @Test
    public void containsTest() {
        HazeString string = new HazeString("I know that I know nothing?");
        assert string.contains("ow");
        assert string.contains("I");
        assert string.contains("?");
        assert !string.contains("!");
        assert !string.contains("  ");
        assert !string.contains("tht");

        assert !string.contains("know", new Range(0, 3));
        Assertions.assertThrows(IndexOutOfBoundsException.class,
                () -> string.contains("a", new Range(998, 999)));
    }

    @Test
    public void doesNotContainTest() {
        HazeString string = new HazeString("I know that I know nothing?");
        assert !string.doesNotContain("ow");
        assert !string.doesNotContain("I");
        assert !string.doesNotContain("?");
        assert string.doesNotContain("!");
        assert string.doesNotContain("  ");
        assert string.doesNotContain("tht");

        assert string.doesNotContain("know", new Range(0, 3));
        Assertions.assertThrows(IndexOutOfBoundsException.class,
                () -> string.contains("a", new Range(998, 999)));
    }

    @Test
    public void countOccurrencesTest() {
        HazeString string = new HazeString("I know that I know nothing?");
        assert string.countOccurrences("know") == 2;
        assert string.countOccurrences("know", new Range(2, 5)) == 1;
        assert string.countOccurrences("Ik") == 0;
        assert string.countOccurrences("I", new Range(1, 1)) == 0;
        assert string.countOccurrences(" ", new Range(0, 1)) == 1;
        assert string.countOccurrences(" ", new Range(1, 2)) == 1;
    }
}
