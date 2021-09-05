package org.izumi.haze;

import org.izumi.haze.filesystem.Extension;

import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;
import java.util.function.Predicate;

public class Wishes extends LinkedList<Wish> {
    public Wishes() {
        super();
    }

    public Wishes(Collection<? extends Wish> c) {
        super(c);
        for (Wish wish : c) {
            if (wish == null) {
                throw new IllegalArgumentException("An element of given collection is null");
            }
        }
    }

    public Collection<Extension> getDistinctExtensions() {
        Set<Extension> extensions = new HashSet<>();
        for (Wish wish : this) {
            extensions.add(wish.source.getExtension());
        }

        return extensions;
    }

    public void filter(Predicate<Wish> predicate) {
        removeIf(wish -> !predicate.test(wish));
    }
}
