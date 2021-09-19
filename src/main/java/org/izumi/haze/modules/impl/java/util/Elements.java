package org.izumi.haze.modules.impl.java.util;

import org.izumi.haze.modules.impl.java.source.Class;
import org.izumi.haze.modules.impl.java.source.Element;

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
