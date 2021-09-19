package org.izumi.haze.string;

import org.izumi.haze.util.Range;

import java.util.Objects;
import java.util.function.Predicate;
import java.util.stream.IntStream;

public class HazeString implements CharSequence {
    protected final java.lang.StringBuilder value;

    public HazeString(CharSequence value) {
        this.value = new java.lang.StringBuilder(value);
    }

    @Override
    public CharSequence subSequence(int start, int end) {
        return value.subSequence(start, end);
    }

    @Override
    public boolean isEmpty() {
        return value.isEmpty();
    }

    @Override
    public IntStream chars() {
        return value.chars();
    }

    @Override
    public char charAt(int index) {
        return value.charAt(index);
    }

    public String substring(Range range) {
        return value.substring(range.start, range.end);
    }

    public HazeString sub(Range range) {
        return new HazeString(value.substring(range.start, range.end + 1));
    }

    public void deleteAll(String toReplace) {
        int index = value.indexOf(toReplace, 0);
        while (index != -1 && index + toReplace.length() <= length()) {
            value.replace(index, index + toReplace.length(), "");
            index = value.indexOf(toReplace, index + toReplace.length());
        }
    }

    public void replaceAllIfSeparate(String toReplace, String replacement, Predicate<SeparatedString> predicate) {
        replaceAllIfSeparate(new Range(value), toReplace, replacement, predicate);
    }

    public void replaceAllIfSeparate(Range range,
                                     String toReplace,
                                     String replacement,
                                     Predicate<SeparatedString> predicate) {
        int diff = toReplace.length() - replacement.length();
        int from = range.start;
        while (true) {
            int index = value.indexOf(toReplace, from);
            if (index == -1 || index > range.end) {
                break;
            }

            from = index + toReplace.length();
            if (isSeparated(index, toReplace, predicate)) {
                value.replace(index, index + toReplace.length(), replacement);
                range.shift(diff);
            }
        }
    }

    public int indexOf(String str) {
        return value.indexOf(str);
    }

    public int indexOf(String str, int fromIndex) {
        return value.indexOf(str, fromIndex);
    }

    public int firstIndexOf(String str) {
        return value.indexOf(str);
    }

    public int lastIndexOf(String str) {
        return value.lastIndexOf(str);
    }

    public int firstIndexOf(String str, Range inRange) {
        int index = value.indexOf(str, inRange.start);
        return index < inRange.end ? index : -1;
    }

    public int lastIndexOf(String str, Range inRange) {
        char lastChar = str.charAt(str.length() - 1);
        for (int i = inRange.end - 1 ; i > inRange.start; i--) {
            boolean probablyEndOfSearched = value.charAt(i) == lastChar;
            if (probablyEndOfSearched) {
                if (matchesInverse(i, str)) {
                    return i - str.length() + 1;
                }
            }
        }

        return -1;
    }

    public int length() {
        return value.length();
    }

    @Override
    public String toString() {
        return value.toString();
    }

    private boolean isSeparated(int index, String toReplace, Predicate<SeparatedString> predicate) {
        int beforeIndex = index > 0 ? index - 1 : 0;
        int afterIndex = index + toReplace.length();
        char before = value.charAt(beforeIndex);
        char after = value.charAt(afterIndex);
        return predicate.test(new SeparatedString(before, after, toReplace));
    }

    private boolean matchesInverse(int index, String str) {
        for (int j = str.length() - 2, k = 1; j > 0; j--, k++) {
            char c1 = value.charAt(index + k);
            char c2 = str.charAt(j);
            if (c1 != c2) {
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        HazeString that = (HazeString) o;
        return value.equals(that.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }
}
