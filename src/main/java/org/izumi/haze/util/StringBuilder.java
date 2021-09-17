package org.izumi.haze.util;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.util.Collection;
import java.util.List;

@RequiredArgsConstructor
public class StringBuilder {
    private final Collection<Character> separated = List.of(' ', '.', '<', '>', '(', ';');

    @NonNull
    private final java.lang.StringBuilder value;

    public StringBuilder(@NonNull CharSequence value) {
        this(new java.lang.StringBuilder(value));
    }

    public String substring(Range range) {
        return value.substring(range.start, range.end);
    }

    public int replaceAllIfSeparate(@NonNull String toReplace, @NonNull String replacement) {
        return replaceAllIfSeparate(new Range(0, value.length() - 1), toReplace, replacement);
        /*int number = 0;
        int index = value.indexOf(toReplace);
        while (index != -1) {
            if (isSeparated(index, toReplace.length())) {
                number++;
                value.replace(index, index + toReplace.length(), replacement);
            }

            index = value.indexOf(toReplace, index + toReplace.length());
        }
        int changing = toReplace.length() - replacement.length();
        return changing * number;*/
    }

    public int replaceAllIfSeparate(Range range, String toReplace, String replacement) {
        int number = 0;
        int index = value.indexOf(toReplace, range.start);
        while (index != -1 && index + toReplace.length() < range.end) {
            if (isSeparated(index, toReplace.length())) {
                number++;
                value.replace(index, index + toReplace.length(), replacement);
            }

            index = value.indexOf(toReplace, index + toReplace.length());
        }
        int changing = toReplace.length() - replacement.length();
        return changing * number;
    }

    public void replace(Range range, String replacement) {
        value.replace(range.start, range.end, replacement);
    }

    public char charAt(int index) {
        return value.charAt(index);
    }

    public int indexOf(String str) {
        return value.indexOf(str);
    }

    public int indexOf(String str, int fromIndex) {
        return value.indexOf(str, fromIndex);
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

    private boolean isSeparated(int index, int length) {
        int afterIndex = index + length;
        char before = value.charAt(index - 1);
        char after = value.charAt(afterIndex);
        return separated.contains(before) && separated.contains(after);
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
}
