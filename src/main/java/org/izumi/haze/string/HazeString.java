package org.izumi.haze.string;

import org.izumi.haze.util.CompareList;
import org.izumi.haze.util.Range;

import java.util.Collection;
import java.util.Optional;

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

    public Optional<Range> firstRangeOf(CharSequence sequence) {
        return firstRangeOf(sequence, range);
    }

    public Optional<Range> firstRangeOf(CharSequence sequence, int fromIndex) {
        return firstRangeOf(sequence, new Range(fromIndex, range.end));
    }

    public Optional<Range> firstRangeOf(CharSequence sequence, Range range) {
        int index = firstIndexOf(sequence, range);
        if (index == -1 || index + sequence.length() - 1 > range.end) {
            return Optional.empty();
        }

        return Optional.of(new Range(index, index + sequence.length() - 1));
    }

    public Optional<Range> firstRangeOf(Collection<CharSequence> sequences, int fromIndex) {
        CompareList<Range> ranges = new CompareList<>();
        sequences.forEach(sequence -> firstRangeOf(sequence, fromIndex).ifPresent(ranges::add));

        return ranges.getMin();
    }

    public Optional<Range> lastRangeOf(CharSequence sequence) {
        return lastRangeOf(sequence, range);
    }

    public Optional<Range> lastRangeOf(CharSequence sequence, Range inRange) {
        String str = sequence.toString();
        char lastChar = str.charAt(str.length() - 1);
        for (int i = inRange.end ; i >= inRange.start; i--) {
            boolean probablyEndOfSearched = string.charAt(i) == lastChar;
            if (probablyEndOfSearched) {
                if (matchesInverse(i, str)) {
                    return Optional.of(new Range(i - str.length() + 1, i));
                }
            }
        }

        return Optional.empty();
    }

    public Optional<Range> lastRangeOf(Collection<CharSequence> sequences, Range inRange) {
        CompareList<Range> ranges = new CompareList<>();
        sequences.forEach(sequence -> lastRangeOf(sequence, inRange).ifPresent(ranges::add));
        return ranges.getMax();
    }

    public boolean contains(CharSequence sequence) {
        return contains(sequence, range);
    }

    public boolean contains(CharSequence sequence, Range inRange) {
        return firstRangeOf(sequence, inRange).isPresent();
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

    private int firstIndexOf(CharSequence sequence, Range inRange) {
        int index = string.indexOf(sequence.toString(), inRange.start);
        return inRange.doesNotContain(index) ? -1 : index;
    }

    private boolean matchesInverse(int index, String str) {
        for (int j = str.length() - 2, k = 1; j > 0; j--, k++) {
            char c1 = charAt(index - k);
            char c2 = str.charAt(j);
            if (c1 != c2) {
                return false;
            }
        }
        return true;
    }
}
