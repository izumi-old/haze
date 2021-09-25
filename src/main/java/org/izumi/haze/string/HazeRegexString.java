package org.izumi.haze.string;

import org.izumi.haze.util.Range;
import org.izumi.haze.util.Ranges;

import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class HazeRegexString extends HazeString {
    public HazeRegexString(CharSequence sequence) {
        super(sequence);
    }

    public Optional<Range> firstRangeOfRegex(Regex regex) {
        return firstRangeOfRegex(new Range(this), regex);
    }

    public Optional<Range> firstRangeOfRegex(Range inRange, Regex regex) {
        Pattern pattern = Pattern.compile(regex.regex);
        Matcher matcher = pattern.matcher(string).region(inRange.start, inRange.end);
        if (matcher.find()) {
            return Optional.of(new Range(matcher.start(), matcher.end() + 1));
        }

        return Optional.empty();
    }

    public Ranges rangesOfRegex(Regex regex) {
        return rangesOfRegex(new Range(this), regex);
    }

    public Ranges rangesOfRegex(Range inRange, Regex regex) {
        Pattern pattern = Pattern.compile(regex.regex);
        Matcher matcher = pattern.matcher(string).region(inRange.start, inRange.end);
        Ranges ranges = new Ranges();
        if (matcher.find()) {
            ranges.add(new Range(matcher.start(), matcher.end()));
        }

        return ranges;
    }

    public HazeRegexString replaceAll(String regex, String replacement) {
        return new HazeRegexString(string.replaceAll(regex, replacement));
    }

    @Override
    public HazeRegexString getSub(Range range) {
        return new HazeRegexString(string.substring(range.start, range.end + 1));
    }

    public HazeRegexString deleteAll(String regex) {
        return new HazeRegexString(string.replaceAll(regex, ""));
    }
}
