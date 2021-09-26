package org.izumi.haze.modules.impl.java.parsing;

import org.izumi.haze.modules.impl.java.source.Class;
import org.izumi.haze.string.LemmaString;
import org.izumi.haze.util.CompareList;
import org.izumi.haze.util.Range;

import java.util.Optional;
import java.util.SortedMap;
import java.util.TreeMap;

public class TopLevelClassesParsing {
    private final LemmaString value;

    public TopLevelClassesParsing(CharSequence sequence) {
        this.value = new LemmaString(sequence);
    }

    public SortedMap<Range, Class> parse() {
        CompareList<Range> ranges = new CompareList<>();
        ranges.addAll(new TopLevelBracesIterator(value));
        includeSignatureAndAnnotationsIfHave(ranges);

        SortedMap<Range, Class> result = new TreeMap<>();
        ranges.forEach(range -> {
            LemmaString string = value.getSub(range);
            X.Element element = new X(string).x();
            if (element.isClass()) {
                result.put(range, new Class(string));
            }
        });

        return result;
    }

    private void includeSignatureAndAnnotationsIfHave(CompareList<Range> ranges) {
        for (int i = 0; i < ranges.size(); i++) {
            Range searchRange = ranges.get(i);
            while (true) {
                Optional<Range> lemmaBeforeRangeOptional = value.getLemmaRangeBefore(searchRange);
                if (lemmaBeforeRangeOptional.isEmpty()) {
                    break;
                }

                Range lemmaBeforeRange = lemmaBeforeRangeOptional.get();
                LemmaString string = value.getSub(lemmaBeforeRange);
                if (string.contains(";") || string.contains("}")) {
                    break;
                }

                searchRange = new Range(lemmaBeforeRange.start, searchRange.end);
            }

            ranges.set(i, searchRange);
        }
    }
}
