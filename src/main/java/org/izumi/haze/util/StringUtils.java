package org.izumi.haze.util;

import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringUtils {
    public static String replaceLast(String string, String regex, String replacement) {
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(string);
        Ranges ranges = new Ranges();
        while (matcher.find()) {
            ranges.add(new Range(matcher.start(), matcher.end()));
        }

        Optional<Range> optional = ranges.getLastRange();
        if (optional.isEmpty()) {
            return string;
        }

        Range range = optional.get();
        return string.substring(0, range.start) + replacement + string.substring(range.end);
    }

    private StringUtils() {}
}
