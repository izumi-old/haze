package org.izumi.haze.string;

import org.izumi.haze.util.CompareList;
import org.izumi.haze.util.Range;

import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringUtils {
    public static String replaceLast(String string, String regex, String replacement) {
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(string);
        CompareList<Range> ranges = new CompareList<>();
        while (matcher.find()) {
            ranges.add(new Range(matcher.start(), matcher.end()));
        }

        Optional<Range> optional = ranges.getMax();
        if (optional.isEmpty()) {
            return string;
        }

        Range range = optional.get();
        return string.substring(0, range.start) + replacement + string.substring(range.end);
    }

    private StringUtils() {}
}
