package org.izumi.haze.util;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class Range implements Comparable<Range> {
    public static final Range EMPTY_RANGE = new Range(0, 0);
    public final int start;
    public final int end;

    public Range(CharSequence sequence) {
        this(0, sequence.length() - 1);
    }

    @Override
    public int compareTo(Range o) {
        if (contains(o)) {
            return 1;
        } else if (o.contains(this)) {
            return -1;
        }

        return Integer.compare(start, o.start);
    }

    /**
     * Does given range intersect this range
     */
    public boolean haveIntersection(Range range) {
        return (start < range.start && end < range.end) || (start > range.start && end > range.end);
    }

    /**
     * Is given range inside this range. Does this range contain given range.
     * Including borders, if we have two ranges like 0-2 and 0-2 then the second is inside the first
     */
    public boolean contains(Range range) {
        return start <= range.start && end >= range.end;
    }

    public Range shift(int changed) {
        if (changed > end) {
            throw new IllegalArgumentException("Cannot shift because length of range is too small");
        }

        return new Range(start, end - changed);
    }

    public long getLength() {
        return end - start;
    }

    public boolean isEmpty() {
        return start == 0 && end == 0;
    }
}
