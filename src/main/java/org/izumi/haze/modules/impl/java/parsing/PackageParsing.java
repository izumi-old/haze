package org.izumi.haze.modules.impl.java.parsing;

import lombok.RequiredArgsConstructor;
import org.izumi.haze.modules.impl.java.source.Keyword;
import org.izumi.haze.modules.impl.java.source.Package;
import org.izumi.haze.string.HazeRegexString;
import org.izumi.haze.string.Regex;
import org.izumi.haze.util.CompareList;
import org.izumi.haze.util.Range;

import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

@RequiredArgsConstructor
public class PackageParsing {
    private final HazeRegexString string;

    public SortedMap<Range, Package> parse() {
        CompareList<Range> ranges = string.rangesOfRegex(new Regex(Keyword.PACKAGE + ".*?;"));
        if (ranges.isEmpty()) {
            return new TreeMap<>(Map.of(Range.EMPTY_RANGE, Package.DEFAULT_PACKAGE));
        } else {
            SortedMap<Range, Package> result = new TreeMap<>();
            ranges.forEach(range ->
                    result.put(range, new Package(this.string.getSub(range).deleteAll(new Regex("\n")))));

            return result;
        }
    }
}
