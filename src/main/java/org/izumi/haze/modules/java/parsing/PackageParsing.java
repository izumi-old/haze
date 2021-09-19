package org.izumi.haze.modules.java.parsing;

import lombok.RequiredArgsConstructor;
import org.izumi.haze.modules.java.Keyword;
import org.izumi.haze.modules.java.source.Package;
import org.izumi.haze.util.HazeString;
import org.izumi.haze.util.Range;

import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@RequiredArgsConstructor
public class PackageParsing {
    private final HazeString string;

    public SortedMap<Range, Package> parse() {
        Pattern pattern = Pattern.compile(Keyword.PACKAGE + ".*;");
        Matcher matcher = pattern.matcher(string);

        Range range;
        Package aPackage;
        if (matcher.find()) {
            range = new Range(matcher.start(), matcher.end());
            HazeString string = this.string.sub(range);
            string.deleteAll("\n");
            aPackage = new Package(string);
        } else {
            range = Range.EMPTY_RANGE;
            aPackage = Package.DEFAULT_PACKAGE;
        }

        return new TreeMap<>(Map.of(range, aPackage));
    }
}
