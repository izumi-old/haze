package org.izumi.haze.modules.java.util.impl;

import org.izumi.haze.modules.java.source.Element;
import org.izumi.haze.modules.java.util.Elements;

import java.util.Collection;
import java.util.LinkedList;

public class ElementsImpl extends LinkedList<Element> implements Elements<Element> {
    public ElementsImpl(Collection<Element> elements) {
        super(elements);
    }

    @SafeVarargs
    public ElementsImpl(Collection<? extends Element>... collections) {
        for (Collection<? extends Element> elements : collections) {
            addAll(elements);
        }
    }

    @Override
    public Elements<Element> getOrderedCopy() {
        ElementsImpl elements = new ElementsImpl(this);
        elements.sort(new ElementComparator());
        return elements;
    }
}
