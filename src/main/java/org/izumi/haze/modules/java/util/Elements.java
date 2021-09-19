package org.izumi.haze.modules.java.util;

import org.izumi.haze.modules.java.source.Class;
import org.izumi.haze.modules.java.source.Element;

import java.util.Collection;

public interface Elements<E extends Element> extends Collection<E> {
    Elements<E> getOrderedCopy();

    default void renameClassAndUsages(Class clazz, String replacement) {
       for (Element element : this) {
           element.renameClassAndUsages(clazz, replacement);
       }
    }

    default boolean isNotEmpty() {
        return !isEmpty();
    }
}
