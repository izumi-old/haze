package org.izumi.haze.modules.impl.java.parsing;

import lombok.RequiredArgsConstructor;
import org.izumi.haze.modules.impl.java.source.Keyword;
import org.izumi.haze.modules.impl.java.source.Import;
import org.izumi.haze.string.HazeRegexString;
import org.izumi.haze.string.Regex;
import org.izumi.haze.util.Range;

import java.util.SortedMap;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@RequiredArgsConstructor
public class ImportsParsing {
    private final HazeRegexString value;

    public SortedMap<Range, Import> parse() {
        int importsEnd = value.lastRangeOf(Keyword.IMPORT.toString()).get().start;
        importsEnd = value.firstRangeOf(";", importsEnd).get().start + 1;

        Pattern pattern = Pattern.compile(Keyword.IMPORT + ".*;");
        Matcher matcher = pattern.matcher(value).region(0, importsEnd);
        SortedMap<Range, Import> map = new TreeMap<>();
        while (matcher.find()) {
            Range range = new Range(matcher.start(), matcher.end());
            HazeRegexString string = value.getSub(range).deleteAll(new Regex("\n"));
            map.put(range, new Import(string));
        }

        return map;
    }
}
