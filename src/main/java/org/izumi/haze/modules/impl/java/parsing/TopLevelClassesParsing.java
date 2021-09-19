package org.izumi.haze.modules.impl.java.parsing;

import lombok.RequiredArgsConstructor;
import org.izumi.haze.modules.impl.java.source.Keyword;
import org.izumi.haze.modules.impl.java.source.Type;
import org.izumi.haze.modules.impl.java.source.Class;
import org.izumi.haze.util.Range;
import org.izumi.haze.string.HazeString;

import java.util.Optional;
import java.util.SortedMap;
import java.util.TreeMap;

@RequiredArgsConstructor
public class TopLevelClassesParsing {
    private final HazeString value;
    private final Range range;

    public TopLevelClassesParsing(HazeString value) {
        this(value, new Range(value));
    }

    public SortedMap<Range, Class> parse() {
        SortedMap<Range, Class> map = new TreeMap<>();
        for (int start = range.start; start <= range.end; start++) {
            start = value.firstIndexOf("{", new Range(start, range.end));
            if (start == -1) {
                break;
            }

            int openBraces = 0;
            for (int end = start+1; end <= range.end; end++) {
                char c = value.charAt(end);
                if (c == '}') {
                    if (openBraces != 0) {
                        openBraces--;
                    } else {
                        Optional<Range> optional = withSignature(new Range(start, end));
                        optional.ifPresent(range1 -> map.put(range1, new Class(value.sub(range1))));

                        start = start + end;
                        if (start >= range.end) {
                            return map;
                        }

                        break;
                    }
                }

                if (c == '{') {
                    openBraces++;
                }
            }
        }

        return map;
    }

    private Optional<Range> withSignature(Range range) {
        boolean exceptForEnd = true;
        int start;
        int end = range.start - 1;
        while (true) {
            start = value.lastIndexOf(" ", new Range(0, end)) + 1;
            if (start == -1 || end == -1) {
                return Optional.empty();
            }

            String lemma = value.substring(new Range(start, end));
            Optional<Keyword> optional = Keyword.of(lemma);
            if (optional.isPresent()) {
                if (Type.of(optional.get()).isPresent()) {
                    exceptForEnd = false;
                }
            } else if (!exceptForEnd) {
                Optional<String> optionalLemma = checkForLeftKeywords(lemma);
                if (optionalLemma.isPresent()) {
                    start += lemma.length() - optionalLemma.get().length();
                }
                return Optional.of(new Range(start, range.end));
            }

            end = start - 1;
        }
    }

    private Optional<String> checkForLeftKeywords(String lemma) {
        for (String s : lemma.split("\n")) {
            if (!"".equals(s) && Keyword.of(s).isPresent()) {
                return Optional.of(s);
            }
        }
        return Optional.empty();
    }
}
