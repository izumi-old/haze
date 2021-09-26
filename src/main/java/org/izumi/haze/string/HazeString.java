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
        validateRangeToOperateIn(range);

        return new HazeString(string.substring(range.start, range.end + 1));
    }

    public Optional<Range> firstRangeOf(CharSequence sequence) {
        return firstRangeOf(sequence, range);
    }

    public Optional<Range> firstRangeOf(CharSequence sequence, int fromIndex) throws IndexOutOfBoundsException {
        validateFromIndexToOperateIn(fromIndex);

        return firstRangeOf(sequence, new Range(fromIndex, range.end));
    }

    public Optional<Range> firstRangeOf(CharSequence sequence, Range range) throws IndexOutOfBoundsException {
        validateRangeToOperateIn(range);

        int index = firstIndexOf(sequence, range);
        if (index == -1 || index + sequence.length() - 1 > range.end) {
            return Optional.empty();
        }

        return Optional.of(new Range(index, index + sequence.length() - 1));
    }

    public Optional<Range> firstRangeOf(Collection<CharSequence> sequences, int fromIndex)
            throws IndexOutOfBoundsException {
        validateFromIndexToOperateIn(fromIndex);

        CompareList<Range> ranges = new CompareList<>();
        sequences.forEach(sequence -> firstRangeOf(sequence, fromIndex).ifPresent(ranges::add));

        return ranges.getMin();
    }

    public Optional<Range> lastRangeOf(CharSequence sequence) {
        return lastRangeOf(sequence, range);
    }

    public Optional<Range> lastRangeOf(CharSequence sequence, Range inRange) throws IndexOutOfBoundsException {
        validateRangeToOperateIn(inRange);

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

    public Optional<Range> lastRangeOf(Collection<CharSequence> sequences, Range inRange)
            throws IndexOutOfBoundsException {
        validateRangeToOperateIn(inRange);

        CompareList<Range> ranges = new CompareList<>();
        sequences.forEach(sequence -> lastRangeOf(sequence, inRange).ifPresent(ranges::add));
        return ranges.getMax();
    }

    public boolean contains(CharSequence sequence) {
        return contains(sequence, range);
    }

    public boolean contains(CharSequence sequence, Range inRange) throws IndexOutOfBoundsException {
        validateRangeToOperateIn(inRange);

        return firstRangeOf(sequence, inRange).isPresent();
    }

    public boolean doesNotContain(CharSequence sequence) {
        return doesNotContain(sequence, range);
    }

    public boolean doesNotContain(CharSequence sequence, Range range) throws IndexOutOfBoundsException {
        return !contains(sequence, range);
    }

    public boolean containsAny(Collection<CharSequence> sequences) { //TODO: cover with tests
        for (CharSequence sequence : sequences) {
            if (contains(sequence)) {
                return true;
            }
        }

        return false;
    }

    public int countOccurrences(CharSequence sequence) {
        return countOccurrences(sequence, range);
    }

    public int countOccurrences(CharSequence sequence, Range range) throws IndexOutOfBoundsException {
        validateRangeToOperateIn(range);

        int result = 0;
        while (true) {
            Optional<Range> rangeOptional = firstRangeOf(sequence, range);
            if (rangeOptional.isEmpty()) {
                break;
            }

            result++;
            Range range1 = rangeOptional.get();
            if (range1.end + 1 > range.end) {
                break;
            }

            range = new Range(range1.end + 1, range.end);
        }

        return result;
    }

    protected void validateFromIndexToOperateIn(int fromIndex) throws IndexOutOfBoundsException {
        if (fromIndex < 0 || fromIndex > range.end) {
            throw new IndexOutOfBoundsException("Given index is out of bounds. Given: " + fromIndex);
        }
    }

    protected void validateRangeToOperateIn(Range range) throws IndexOutOfBoundsException {
        if (range.end > this.range.end) {
            throw new IndexOutOfBoundsException("Given range is out of bounds. The range: " + range +
                    ". Bounds: " + this.range);
        }
    }

    private int firstIndexOf(CharSequence sequence, Range inRange) {
        int index = string.indexOf(sequence.toString(), inRange.start);
        return inRange.doesNotContain(new Range(index, index + sequence.length() - 1)) ? -1 : index;
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
