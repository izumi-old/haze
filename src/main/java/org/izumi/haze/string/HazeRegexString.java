package org.izumi.haze.string;

import org.izumi.haze.util.CompareList;
import org.izumi.haze.util.ExtendedList;
import org.izumi.haze.util.Range;

import java.util.Optional;
import java.util.function.Predicate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class HazeRegexString extends HazeString {
    public HazeRegexString(CharSequence sequence) {
        super(sequence);
    }

    public Optional<Range> firstRangeOfRegex(Regex regex) {
        return firstRangeOfRegex(new Range(this), regex);
    }

    public Optional<Range> firstRangeOfRegex(Range range, Regex regex) throws IndexOutOfBoundsException {
        validateRangeToOperateIn(range);

        Pattern pattern = Pattern.compile(regex.toString());
        Matcher matcher = pattern.matcher(string).region(range.start, range.end + 1);
        if (matcher.find()) {
            return Optional.of(new Range(matcher.start(), matcher.end() - 1));
        }

        return Optional.empty();
    }

    public Optional<Range> lastRangeOfRegex(Regex regex) {
        return lastRangeOfRegex(range, regex);
    }

    public Optional<Range> lastRangeOfRegex(Range range, Regex regex) throws IndexOutOfBoundsException {
        validateRangeToOperateIn(range);

        Pattern pattern = Pattern.compile(regex.toString());
        Matcher matcher = pattern.matcher(string).region(range.start, range.end + 1);
        CompareList<Range> ranges = new CompareList<>();
        while (matcher.find()) {
            ranges.add(new Range(matcher.start(), matcher.end() - 1));
        }

        return ranges.getMax();
    }

    public CompareList<Range> rangesOfRegex(Regex regex) {
        return rangesOfRegex(range, regex);
    }

    public CompareList<Range> rangesOfRegex(Range range, Regex regex) throws IndexOutOfBoundsException {
        validateRangeToOperateIn(range);

        Pattern pattern = Pattern.compile(regex.toString());
        Matcher matcher = pattern.matcher(string).region(range.start, range.end + 1);
        CompareList<Range> ranges = new CompareList<>();
        while (matcher.find()) {
            ranges.add(new Range(matcher.start(), matcher.end() - 1));
        }

        return ranges;
    }

    public HazeRegexString replaceAll(Regex regex, String replacement) {
        return new HazeRegexString(string.replaceAll(regex.toString(), replacement));
    }

    @Override
    public HazeRegexString getSub(Range range) throws IndexOutOfBoundsException {
        validateRangeToOperateIn(range);

        return new HazeRegexString(string.substring(range.start, range.end + 1));
    }

    public HazeRegexString deleteAll(Regex regex) {
        return new HazeRegexString(string.replaceAll(regex.toString(), ""));
    }

    public HazeRegexString replaceAllIf(Regex regex, String replacement, Predicate<SeparatedString> predicate) {
        return replaceAllIf(range, regex, replacement, predicate);
    }

    public HazeRegexString replaceAllIf(Range range,
                                        Regex regex,
                                        String replacement,
                                        Predicate<SeparatedString> predicate) throws IndexOutOfBoundsException {
        validateRangeToOperateIn(range);

        Pattern pattern = Pattern.compile(regex.toString());
        Matcher matcher = pattern.matcher(string);
        ExtendedList<Range> replaceRanges = new ExtendedList<>();
        while (matcher.find() && matcher.end() <= range.end) {
            if (isPredicateTest(matcher.start(), regex.toString(), predicate)) {
                replaceRanges.add(new Range(matcher.start(), matcher.end()));
            }
        }

        StringBuilder result = new StringBuilder(string);
        for (int i = 0; i < replaceRanges.size(); i++) {
            Range replaceRange = replaceRanges.get(i);
            result.replace(replaceRange.start, replaceRange.end, replacement);
            for (int j = i; j < replaceRanges.size(); j++) {
                replaceRanges.update(j, range1 -> range1.shift((int) replaceRange.getLength() - replacement.length()));
            }
        }

        return new HazeRegexString(result);
    }

    private boolean isPredicateTest(int index, String toReplace, Predicate<SeparatedString> predicate) {
        int beforeIndex = index > 0 ? index - 1 : 0;
        int afterIndex = index + toReplace.length();
        char before = charAt(beforeIndex);
        char after = charAt(afterIndex);
        return predicate.test(new SeparatedString(before, after, toReplace));
    }
}
