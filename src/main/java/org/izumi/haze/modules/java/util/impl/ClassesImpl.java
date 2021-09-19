package org.izumi.haze.modules.java.util.impl;

import org.izumi.haze.modules.java.source.Class;
import org.izumi.haze.modules.java.util.Classes;
import org.izumi.haze.modules.java.util.Elements;

import java.util.Collection;
import java.util.LinkedList;

public class ClassesImpl extends LinkedList<Class> implements Classes {
    public ClassesImpl() {}

    public ClassesImpl(Collection<? extends Class> c) {
        super(c);
    }

    @Override
    public Elements<Class> getOrderedCopy() {
        ClassesImpl classes = new ClassesImpl(this);
        classes.sort(new ElementComparator());
        return classes;
    }
}
