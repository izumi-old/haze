package org.izumi.haze.string;

import org.izumi.haze.util.Range;

import java.util.Optional;

public class LemmaString extends HazeString {
    public LemmaString(CharSequence sequence) {
        super(sequence);
    }

    @Override
    public LemmaString getSub(Range range) {
        return new LemmaString(string.substring(range.start, range.end + 1));
    }

    public Optional<LemmaString> getLemmaBefore(Range range) {
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
            int start = lastRangeOf(" ", new Range(borders.start, end)).get().start;

            return Optional.of(getSub(new Range(start, end)));
        }
    }

    public static void main(String[] args) {
        String s = "a a  leamm3";
        LemmaString lemmaString = new LemmaString(s);

        Range range = new Range(4, 5);
        System.out.println("Given range: {" + lemmaString.getSub(range) + "}");

        System.out.println("Result: " + lemmaString.getLemmaBefore(range));
    }
}
