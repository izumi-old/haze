package org.izumi.haze.util;

import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedList;
import java.util.Optional;

public class OptionalList<T> extends LinkedList<Optional<T>> {
    public OptionalList() {
    }

    public OptionalList(Optional<T>... optionals) {
        this(Arrays.asList(optionals));
    }

    public OptionalList(Collection<? extends Optional<T>> c) {
        super(c);
    }

    public boolean anyEmpty() {
        for (Optional<T> optional : this) {
            if (optional.isEmpty()) {
                return true;
            }
        }

        return false;
    }

    public boolean allPresent() {
        return !anyEmpty();
    }

    public Collection<T> getAllPresent() {
        Collection<T> ts = new LinkedList<>();
        forEach(optional -> optional.ifPresent(ts::add));

        return ts;
    }
}
