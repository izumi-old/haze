package org.izumi.haze.string;

import org.izumi.haze.util.ExtendedList;
import org.izumi.haze.util.Range;

import java.util.Collection;
import java.util.function.Predicate;

public class HazeString extends HazeCharSequence {
    public HazeString(CharSequence sequence) {
        super(sequence);
    }

    public HazeString getSub(Range range) throws IndexOutOfBoundsException {
        if (range.end + 1 > length()) {
            throw new IndexOutOfBoundsException(range.end + " is out of bounds");
        }
        return new HazeString(string.substring(range.start, range.end + 1));
    }

    public HazeString replaceAllIf(String toReplace, String replacement, Predicate<SeparatedString> predicate) {
        return replaceAllIf(range, toReplace, replacement, predicate);
    }

    public HazeString replaceAllIf(Range range,
                                   String toReplace,
                                   String replacement,
                                   Predicate<SeparatedString> predicate) {
        int diff = toReplace.length() - replacement.length();
        int from = range.start;
        String result = string;
        while (true) {
            int index = firstIndexOf(toReplace, from);
            if (index == -1 || index > range.end) {
                break;
            }

            from = index + toReplace.length();
            if (isSeparated(index, toReplace, predicate)) {
                result = result.substring(0, index) + replacement +
                        result.substring(index + toReplace.length(), replacement.length());
                range.shift(diff);
            }
        }

        return new HazeString(result);
    }

    public int firstIndexOf(CharSequence sequence) {
        return firstIndexOf(sequence, range);
    }

    public int firstIndexOf(CharSequence sequence, int fromIndex) {
        return firstIndexOf(sequence, new Range(fromIndex, range.end));
    }

    public int firstIndexOf(Collection<CharSequence> sequences, int fromIndex) {
        ExtendedList<Integer> indexes = new ExtendedList<>();
        for (CharSequence sequence : sequences) {
            int index = firstIndexOf(sequence, fromIndex);
            if (index != -1) {
                indexes.add(index);
            }
        }

        return indexes.getMin().orElse(-1);
    }

    public int firstIndexOf(CharSequence sequence, Range inRange) {
        int index = string.indexOf(sequence.toString(), inRange.start);
        return index <= inRange.end ? index : -1;
    }

    public int lastIndexOf(CharSequence sequence) {
        return lastIndexOf(sequence, range);
    }

    public int lastIndexOf(CharSequence sequence, int fromIndex) {
        return lastIndexOf(sequence, new Range(fromIndex, range.end));
    }

    public int lastIndexOf(CharSequence sequence, Range inRange) {
        String str = sequence.toString();
        char lastChar = str.charAt(str.length() - 1);
        for (int i = inRange.end ; i >= inRange.start; i--) {
            boolean probablyEndOfSearched = string.charAt(i) == lastChar;
            if (probablyEndOfSearched) {
                if (matchesInverse(i, str)) {
                    return i - str.length() + 1;
                }
            }
        }

        return -1;
    }

    public int lastIndexOf(Collection<CharSequence> sequences, Range inRange) {
        ExtendedList<Integer> indexes = new ExtendedList<>();
        for (CharSequence sequence : sequences) {
            int index = lastIndexOf(sequence, inRange);
            if (index != -1) {
                indexes.add(index);
            }
        }

        return indexes.getMax().orElse(-1);
    }

    public boolean contains(CharSequence sequence) {
        return contains(sequence, range);
    }

    public boolean contains(CharSequence sequence, Range inRange) {
        return firstIndexOf(sequence, inRange) != -1;
    }

    public int countOccurrences(CharSequence sequence) {
        return countOccurrences(sequence, range);
    }

    public int countOccurrences(CharSequence sequence, Range range) {
        int index;
        int result = 0;
        while (true) {
            index = firstIndexOf(sequence, range);
            if (index == -1 || index >= range.end - 1) {
                break;
            }

            range = new Range(index + 1, range.end);
            result++;
        }

        return result;
    }

    private boolean isSeparated(int index, String toReplace, Predicate<SeparatedString> predicate) {
        int beforeIndex = index > 0 ? index - 1 : 0;
        int afterIndex = index + toReplace.length();
        char before = charAt(beforeIndex);
        char after = charAt(afterIndex);
        return predicate.test(new SeparatedString(before, after, toReplace));
    }

    private boolean matchesInverse(int index, String str) {
        for (int j = str.length() - 2, k = 1; j > 0; j--, k++) {
            char c1 = charAt(index + k);
            char c2 = str.charAt(j);
            if (c1 != c2) {
                return false;
            }
        }
        return true;
    }
}
