package org.izumi.haze.modules.java.util.impl;

import org.izumi.haze.modules.java.source.Import;
import org.izumi.haze.modules.java.util.Elements;
import org.izumi.haze.modules.java.util.Imports;

import java.util.Collection;
import java.util.LinkedList;

public class ImportsImpl extends LinkedList<Import> implements Imports {
    public ImportsImpl() {
    }

    public ImportsImpl(Collection<? extends Import> c) {
        super(c);
    }

    @Override
    public Elements<Import> getOrderedCopy() {
        ImportsImpl imports = new ImportsImpl(this);
        imports.sort(new ElementComparator());
        return imports;
    }
}
