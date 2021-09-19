package org.izumi.haze.modules.impl.java.util;

import org.izumi.haze.modules.impl.java.source.Element;

import java.util.Collection;

public interface Elements<E extends Element> extends Collection<E> {
    Elements<E> getOrderedCopy();

    default void renameClassAndUsages(String name, String replacement) {
       for (Element element : this) {
           element.renameClassAndUsages(name, replacement);
       }
    }

    default boolean isNotEmpty() {
        return !isEmpty();
    }
}
