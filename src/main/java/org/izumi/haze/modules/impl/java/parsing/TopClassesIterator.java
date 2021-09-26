package org.izumi.haze.modules.impl.java.parsing;

import org.izumi.haze.HazeException;
import org.izumi.haze.modules.impl.java.source.Class;
import org.izumi.haze.string.Regex;
import org.izumi.haze.string.HazeRegexString;
import org.izumi.haze.util.CompareList;
import org.izumi.haze.util.Range;

import java.util.Iterator;
import java.util.Optional;

public class TopClassesIterator implements Iterator<RangeClass> {
    private final HazeRegexString value;
    private final Range range;
    private Range currentRange;
    private Iterator<Range> topLevelBracesIterator;

    public TopClassesIterator(HazeRegexString value) {
        this.value = value;
        this.range = new Range(value);
        this.currentRange = new Range(value);
        this.topLevelBracesIterator = new TopLevelElementsIterator(value);
    }

    @Override
    public boolean hasNext() {
        return value.contains("{", currentRange);
    }

    @Override
    public RangeClass next() {
        int start = value.firstRangeOf("{", currentRange).get().start;
        int openBraces = 1;
        for (int end = start+1; end <= range.end; end++) {
            char c = value.charAt(end);
            if (c == '}') {
                openBraces--;
                if (openBraces == 0) {
                    Range bracesRange = new Range(start, end);
                    Recognition recognition = new LazySignature(value.getSub(bracesRange)).recognize();
                    if (recognition.isAnyClassType()) {
                        Range withAnnotations = withAnnotations(range);
                        return new RangeClass(withAnnotations, new Class(value.getSub(withAnnotations)));
                    }

                    currentRange = new Range(end, range.end);
                    break;
                }
            }

            if (c == '{') {
                openBraces++;
            }
        }

        throw new HazeException("An error occurred while iterating. No classes found");
    }

    private Range withAnnotations(Range range) {
        Range annotationsZone = new Range(0, range.start > 0 ? range.start - 1 : 0);
        HazeRegexString regexString = new HazeRegexString(value.getSub(annotationsZone));
        CompareList<Range> ranges = regexString.rangesOfRegex(new Regex("@.*@"));

        Optional<Range> min = ranges.getMin();
        if (min.isEmpty()) {
            ranges = regexString.rangesOfRegex(new Regex("@.*"));
            min = ranges.getMin();
            if (min.isEmpty()) {
                //necessary to include the signature
                return range;
            } else {
                Range range1 = min.get();
                return new Range(range1.start, range.end);
            }
        } else {
            Range range1 = min.get();
            return new Range(range1.start, range.end);
        }
    }
}
