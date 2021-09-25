package org.izumi.haze.util;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.Optional;

public class ExtendedList<T extends Comparable<? super T>> extends LinkedList<T> {
    public Optional<T> getMin() {
        if (isEmpty()) {
            return Optional.empty();
        }

        Iterator<T> iterator = iterator();
        T min = iterator.next();
        while (iterator.hasNext()) {
            T t = iterator.next();
            if (min.compareTo(t) > 0) {
                min = t;
            }
        }

        return Optional.of(min);
    }

    public Optional<T> getMax() {
        if (isEmpty()) {
            return Optional.empty();
        }

        Iterator<T> iterator = iterator();
        T max = iterator.next();
        while (iterator.hasNext()) {
            T t = iterator.next();
            if (max.compareTo(t) < 0) {
                max = t;
            }
        }

        return Optional.of(max);
    }
}
