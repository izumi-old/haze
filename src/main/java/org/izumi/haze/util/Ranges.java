package org.izumi.haze.util;

import java.util.Iterator;
import java.util.Optional;

public class Ranges extends ExtendedList<Range> {
    public Optional<Range> getLastRange() {
        if (isEmpty()) {
            return Optional.empty();
        }

        Iterator<Range> iterator = this.iterator();
        Range max = iterator.next();
        while (iterator.hasNext()) {
            Range range = iterator.next();
            if (range.start > max.start) {
                max = range;
            }
        }

        return Optional.of(max);
    }
}
