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
        validateRangeToOperateIn(range);

        Range borders = new Range(this.range.start, range.start > 0 ? range.start - 1 : 0);
        int spaces = countOccurrences(" ", borders);
        if (spaces == 0) {
            return Optional.empty();
        } else if (spaces == 1) {
            int spaceIndex = firstRangeOf(" ", borders).get().start;
            if (spaceIndex == 0) {
                return Optional.empty();
            }

            return Optional.of(getSub(new Range(borders.start, spaceIndex - 1)));
        } else {
            int end = lastRangeOf(" ", borders).get().start - 1;
            if (end < borders.start) {
                return Optional.empty();
            }
            int start = lastRangeOf(" ", new Range(borders.start, end)).get().start + 1;

            return Optional.of(getSub(new Range(start, end)));
        }
    }
}
