package org.izumi.haze.modules.impl.java.parsing;

import lombok.RequiredArgsConstructor;
import org.izumi.haze.string.HazeString;
import org.izumi.haze.util.Range;

@RequiredArgsConstructor
public class Lemma implements Comparable<Lemma> {
    public final Range range;
    public final HazeString string;

    @Override
    public int compareTo(Lemma o) {
        return range.compareTo(o.range);
    }

    @Override
    public String toString() {
        return string.toString();
    }
}
