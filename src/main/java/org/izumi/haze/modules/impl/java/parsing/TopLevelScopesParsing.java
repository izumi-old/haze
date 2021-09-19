package org.izumi.haze.modules.impl.java.parsing;

import lombok.RequiredArgsConstructor;
import org.izumi.haze.modules.impl.java.source.Scope;
import org.izumi.haze.util.Range;
import org.izumi.haze.string.HazeString;

import java.util.SortedMap;
import java.util.TreeMap;

@RequiredArgsConstructor
public class TopLevelScopesParsing {
    private final HazeString value;
    private final Range range;

    public SortedMap<Range, Scope> parse() {
        int metUnresolvedOpening = -1;
        int start = -1;
        int end;

        SortedMap<Range, Scope> map = new TreeMap<>();
        for (int i = range.start; i <= range.end; i++) {
            char c = value.charAt(i);
            if (c == '{') {
                if (metUnresolvedOpening == -1) {
                    start = i;
                    metUnresolvedOpening = 0;
                } else {
                    metUnresolvedOpening++;
                }
            }

            if (c == '}') {
                if (metUnresolvedOpening == 0) {
                    end = i+1;

                    Range range = new Range(start, end);
                    if (isScope(range)) {
                        map.put(range, new Scope(value, range));
                    }
                    metUnresolvedOpening = -1;
                } else {
                    metUnresolvedOpening--;
                }
            }
        }

        return map;
    }

    private boolean isScope(Range range) {
        return new Recognition(value, range).recognize() == Recognition.Element.SCOPE;
    }
}
