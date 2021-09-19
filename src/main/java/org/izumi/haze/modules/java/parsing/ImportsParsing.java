package org.izumi.haze.modules.java.parsing;

import lombok.RequiredArgsConstructor;
import org.izumi.haze.modules.java.source.Keyword;
import org.izumi.haze.modules.java.source.Import;
import org.izumi.haze.util.Range;
import org.izumi.haze.string.HazeString;

import java.util.SortedMap;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@RequiredArgsConstructor
public class ImportsParsing {
    private final HazeString value;

    public SortedMap<Range, Import> parse() {
        int importsEnd = value.lastIndexOf(Keyword.IMPORT.toString());
        importsEnd = value.indexOf(";", importsEnd) + 1;

        Pattern pattern = Pattern.compile(Keyword.IMPORT + ".*;");
        Matcher matcher = pattern.matcher(value).region(0, importsEnd);
        SortedMap<Range, Import> map = new TreeMap<>();
        while (matcher.find()) {
            Range range = new Range(matcher.start(), matcher.end());
            HazeString string = value.sub(range);
            string.deleteAll("\n");
            map.put(range, new Import(string));
        }

        return map;
    }
}
