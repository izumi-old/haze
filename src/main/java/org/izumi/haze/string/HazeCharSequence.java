package org.izumi.haze.string;

import org.izumi.haze.util.Range;

import java.util.Objects;
import java.util.Optional;

public abstract class HazeCharSequence implements CharSequence {
    protected final String string;
    protected final Range range;

    public HazeCharSequence(CharSequence sequence) {
        this.string = sequence.toString();

        if (string.isEmpty()) {
            range = null;
        } else {
            range = new Range(string);
        }
    }

    @Override
    public int length() {
        return string.length();
    }

    @Override
    public char charAt(int index) throws IndexOutOfBoundsException {
        return string.charAt(index);
    }

    @Override
    public CharSequence subSequence(int start, int end) throws IndexOutOfBoundsException {
        return string.subSequence(start, end);
    }

    public CharSequence subSequence(Range range) throws IndexOutOfBoundsException {
        return subSequence(range.start, range.end + 1);
    }

    @Override
    public boolean isEmpty() {
        return string.isEmpty();
    }

    public boolean isNotEmpty() {
        return !isEmpty();
    }

    public Optional<Range> getRange() {
        return Optional.ofNullable(range);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        HazeCharSequence that = (HazeCharSequence) o;
        return Objects.equals(string, that.string);
    }

    public boolean equals(CharSequence sequence) {
        if (sequence == null) {
            return false;
        }

        return sequence.toString().equals(string);
    }

    @Override
    public int hashCode() {
        return Objects.hash(string);
    }

    @Override
    public String toString() {
        return string;
    }
}
