package org.izumi.haze.util;

import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedList;
import java.util.Objects;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ExtendedList<?> list = (ExtendedList<?>) o;
        if (size() != list.size()) {
            return false;
        }

        for (Object obj : this) {
            if (!list.contains(obj)) {
                return false;
            }
        }

        return true;
    }

    @Override
    public int hashCode() {
        Object[] arr = new Object[size()];
        for (int i = 0; i < size(); i++) {
            arr[i] = get(i);
        }

        return Objects.hash(arr);
    }
}
