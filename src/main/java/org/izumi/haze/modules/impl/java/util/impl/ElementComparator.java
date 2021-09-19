package org.izumi.haze.modules.impl.java.util.impl;

import org.izumi.haze.modules.impl.java.source.Element;
import org.springframework.stereotype.Component;

import java.util.Comparator;

@Component
public class ElementComparator implements Comparator<Element> {

    @Override
    public int compare(Element o1, Element o2) {
        return o1.compareTo(o2);
    }
}
