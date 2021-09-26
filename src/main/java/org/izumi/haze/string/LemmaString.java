package org.izumi.haze.string;

import org.izumi.haze.util.Range;

import java.util.Optional;

public class LemmaString extends HazeString {
    public LemmaString(CharSequence sequence) {
        super(sequence);
    }

    @Override
    public LemmaString getSub(Range range) throws IndexOutOfBoundsException {
        validateRangeToOperateIn(range);

        return new LemmaString(string.substring(range.start, range.end + 1));
    }

    public Optional<LemmaString> getLemmaBefore(Range range) throws IndexOutOfBoundsException {
        return getLemmaRangeBefore(range).map(this::getSub);
    }

    public Optional<Range> getLemmaRangeBefore(Range range) throws IndexOutOfBoundsException {
        validateRangeToOperateIn(range);

        Range borders = new Range(this.range.start, range.start > 0 ? range.start - 1 : 0);
        int spaces = countOccurrences(" ", borders);
        if (spaces == 0) {
            return Optional.empty();
        } else if (spaces == 1) {
            int spaceIndex = firstRangeOf(" ", borders).get().start;
            if (spaceIndex == 0) { //space is the beginning of string
                return Optional.empty();
            }

            return Optional.of(new Range(borders.start, spaceIndex - 1));
        }

        int end = lastRangeOf(" ", borders).get().start - 1;
        if (end < borders.start) {
            return Optional.empty();
        }
        int start = lastRangeOf(" ", new Range(borders.start, end)).get().start + 1;

        return Optional.of(new Range(start, end));
    }

    public Optional<Range> getFirstLemmaRange() {
        if (doesNotContain(" ")) {
            return Optional.of(range);
        }

        int from = range.start;
        while (charAt(from) == ' ') {
            from++;
        }

        Optional<Range> rangeOfSpace = firstRangeOf(" ", from);
        return Optional.of(new Range(from, rangeOfSpace.get().start - 1));
    }

    public Optional<Range> getLemmaRangeAfter(Range range) throws IndexOutOfBoundsException {
        validateRangeToOperateIn(range);

        Range borders = new Range(range.end, this.range.end);
        int spaces = countOccurrences(" ", borders);
        if (spaces == 0) {
            return Optional.empty();
        } else if (spaces == 1) {
            int spaceIndex = firstRangeOf(" ").get().start;
            if (spaceIndex - 1 == range.end) { //space is the ending of string
                return Optional.empty();
            }

            return Optional.of(new Range(range.start, spaceIndex - 1));
        }

        int start = firstRangeOf(" ", borders).get().start + 1;
        if (start > borders.end) {
            return Optional.empty();
        }
        int end = firstRangeOf(" ", new Range(start, borders.end)).get().start - 1;

        return Optional.of(new Range(start, end));
    }

    public Optional<LemmaString> getLemmaAfter(Range range) throws IndexOutOfBoundsException {
        return getLemmaRangeAfter(range).map(this::getSub);
    }
}
