package org.izumi.haze.string;

import org.izumi.haze.util.CompareList;
import org.izumi.haze.util.Range;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class HazeRegexStringTest {

    @Test
    public void replaceAllIfTest() {
        HazeRegexString string = new HazeRegexString("I know thatt I know nothing?");
        HazeRegexString string1 = new HazeRegexString("public void x() { " +
                "A a = new A(1, 2); A aa = new A(a.a, a.a); B b = new B(); B bb = new B(); }");

        assert string.replaceAllIf(new Regex("now"), "won", s -> true)
                .equals("I kwon thatt I kwon nothing?");
        assert string.replaceAllIf(new Regex("now"), "won", s -> false)
                .equals("I know thatt I know nothing?");
        assert string.replaceAllIf(new Regex("123"), "", s -> true).equals(string);
        assert string.replaceAllIf(new Regex("that.?"), "", s -> true)
                .equals("I know  I know nothing?");
        assert string1.replaceAllIf(new Regex("A"), "Zo", s -> true)
                .equals("public void x() { " +
                        "Zo a = new Zo(1, 2); Zo aa = new Zo(a.a, a.a); B b = new B(); B bb = new B(); }");
    }

    @Test
    public void deleteAllTest() {
        HazeRegexString string = new HazeRegexString("I know thatt I know nothing?");
        assert string.deleteAll(new Regex("now")).equals("I k thatt I k nothing?");
        assert string.deleteAll(new Regex("123")).equals(string);
        assert string.deleteAll(new Regex("that.?")).equals("I know  I know nothing?");
    }

    @Test
    public void firstRangeOfRegexTest() {
        HazeRegexString string = new HazeRegexString("I know thatt I know nothing?");
        assert string.firstRangeOfRegex(string.getRange().get(), new Regex("now")).get()
                .equals(new Range(3, 5));
        assert string.firstRangeOfRegex(string.getRange().get(), new Regex("123")).isEmpty();
        assert string.firstRangeOfRegex(string.getRange().get(), new Regex("that.?")).get()
                .equals(new Range(7, 11));

        Assertions.assertThrows(IndexOutOfBoundsException.class,
                () -> string.firstRangeOfRegex(new Range(1, 999), new Regex("t")));
    }

    @Test
    public void lastRangeOfRegexTest() {
        HazeRegexString string = new HazeRegexString("I know thatt I know nothing?");
        assert string.lastRangeOfRegex(string.getRange().get(), new Regex("now")).get()
                .equals(new Range(16, 18));
        assert string.lastRangeOfRegex(string.getRange().get(), new Regex("123")).isEmpty();
        assert string.lastRangeOfRegex(string.getRange().get(), new Regex("that.?")).get()
                .equals(new Range(7, 11));

        Assertions.assertThrows(IndexOutOfBoundsException.class,
                () -> string.lastRangeOfRegex(new Range(1, 999), new Regex("t")));
    }

    @Test
    public void rangesOfRegexTest() {
        HazeRegexString string = new HazeRegexString("I know thatt I know nothing?");

        assert string.rangesOfRegex(new Regex("know"))
                .equals(new CompareList<>(new Range(2, 5), new Range(15, 18)));
        assert string.rangesOfRegex(new Regex("t"))
                .equals(new CompareList<>(new Range(7), new Range(10), new Range(11), new Range(22)));
        assert string.rangesOfRegex(new Range(0, 5), new Regex("know"))
                .equals(new CompareList<>(new Range(2, 5)));
    }

    @Test
    public void replaceAllTest() {
        HazeRegexString string = new HazeRegexString("I know thatt I know nothing?");
        assert string.replaceAll(new Regex("now"), "won").equals("I kwon thatt I kwon nothing?");
        assert string.replaceAll(new Regex("123"), "").equals(string);

        assert string.replaceAll(new Regex("that.?"), "").equals("I know  I know nothing?");
    }

    @Test
    public void getSubTest() {
        HazeRegexString string = new HazeRegexString("I know that I know nothing?");
        assert string.getSub(new Range(0, 5)).equals("I know");
        assert string.getSub(new Range(0, 0)).equals("I");
        assert string.getSub(string.getRange().get()).equals(string);
        Assertions.assertThrows(IndexOutOfBoundsException.class, () -> string.getSub(new Range(5, 27)));
        Assertions.assertThrows(IndexOutOfBoundsException.class, () -> string.getSub(new Range(28, 999)));
    }
}
