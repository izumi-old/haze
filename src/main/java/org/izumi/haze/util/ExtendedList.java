package org.izumi.haze.util;

import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedList;
import java.util.function.Function;

public class ExtendedList<T> extends LinkedList<T> {
    public ExtendedList() {
    }

    @SafeVarargs
    public ExtendedList(T... ts) {
        this(Arrays.asList(ts));
    }

    public ExtendedList(Collection<? extends T> c) {
        super(c);
    }

    public void update(int index, Function<T, T> function) {
        set(index, function.apply(get(index)));
    }
}
