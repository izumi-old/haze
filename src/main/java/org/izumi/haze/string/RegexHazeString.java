package org.izumi.haze.string;

import lombok.NonNull;
import org.izumi.haze.util.Range;

import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegexHazeString extends HazeString {
    public RegexHazeString(@NonNull CharSequence value) {
        super(new java.lang.StringBuilder(value));
    }

    public Optional<Range> firstRangeOfRegex(Regex regex) {
        return firstRangeOfRegex(new Range(this), regex);
    }

    public Optional<Range> firstRangeOfRegex(Range inRange, Regex regex) {
        Pattern pattern = Pattern.compile(regex.regex);
        Matcher matcher = pattern.matcher(value).region(inRange.start, inRange.end);
        if (matcher.find()) {
            return Optional.of(new Range(matcher.start(), matcher.end() + 1));
        }

        return Optional.empty();
    }
}
