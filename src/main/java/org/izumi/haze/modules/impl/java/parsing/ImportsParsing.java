package org.izumi.haze.modules.impl.java.parsing;

import org.izumi.haze.modules.impl.java.source.Keyword;
import org.izumi.haze.modules.impl.java.source.Import;
import org.izumi.haze.string.HazeRegexString;
import org.izumi.haze.string.Regex;
import org.izumi.haze.util.CompareList;
import org.izumi.haze.util.Range;

import java.util.SortedMap;
import java.util.TreeMap;

public class ImportsParsing {
    private final HazeRegexString string;

    public ImportsParsing(CharSequence sequence) {
        this.string = new HazeRegexString(sequence);
    }

    public SortedMap<Range, Import> parse() {
        CompareList<Range> ranges = string.rangesOfRegex(new Regex(Keyword.IMPORT + ".*?;"));
        if (ranges.isEmpty()) {
            return new TreeMap<>();
        } else {
            SortedMap<Range, Import> result = new TreeMap<>();
            ranges.forEach(range ->
                    result.put(range, new Import(string.getSub(range).deleteAll(new Regex("\n")))));

            return result;
        }
    }
}
