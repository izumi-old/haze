package org.izumi.haze.util;

import java.util.Objects;

public class Range implements Comparable<Range> {
    public static final Range EMPTY_RANGE = new Range(0, 0);
    public final int start;
    public final int end;

    /**
     * @param start - value, beginning index, include
     * @param end = value, ending index, include
     */
    public Range(int start, int end) {
        if (end < start) {
            throw new IllegalArgumentException("End is smaller than start. End: " + end + ". Start: " + start);
        }
        this.start = start;
        this.end = end;
    }

    public Range(CharSequence sequence) {
        this(0, sequence.length() - 1);
    }

    public Range(Range range) {
        this(range.start, range.end);
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

    public boolean contains(int i) {
        return i >= start && i <= end;
    }

    public boolean doesNotContain(int i) {
        return !contains(i);
    }

    public boolean doesNotContainAny(int... is) {
        for (int i : is) {
            if (contains(i)) {
                return false;
            }
        }

        return true;
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

    @Override
    public String toString() {
        return String.format("[%d;%d]", start, end);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Range range = (Range) o;
        return start == range.start && end == range.end;
    }

    @Override
    public int hashCode() {
        return Objects.hash(start, end);
    }
}
