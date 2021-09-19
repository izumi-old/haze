package org.izumi.haze.util;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.Map;
import java.util.TreeMap;

public class RangeMap<V> extends TreeMap<Range, V> {
    public Collection<Range> getUnusedRanges(Range inRange) {
        if (isEmpty()) {
            return Collections.singleton(inRange);
        }

        int end = inRange.start;
        Collection<Range> ranges = new LinkedList<>();
        for (Map.Entry<Range, V> entry : entrySet()) {
            Range range = entry.getKey();
            if (range.start > end) {
                ranges.add(new Range(end, range.start));
                end = range.end;
            }

            if (end > inRange.end) {
                break;
            }
        }

        return ranges;
    }

    @SafeVarargs
    public final void putAll(Map<Range, ? extends V>... maps) {
        for (Map<Range, ? extends V> map : maps) {
            putAll(map);
        }
    }
}
