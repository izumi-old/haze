package org.izumi.haze.modules.java.util.impl;

import org.izumi.haze.modules.java.source.Class;
import org.izumi.haze.modules.java.source.Scope;
import org.izumi.haze.modules.java.util.Elements;
import org.izumi.haze.modules.java.util.Scopes;

import java.util.Collection;
import java.util.LinkedList;

public class ScopesImpl extends LinkedList<Scope> implements Scopes {
    public ScopesImpl() {
    }

    public ScopesImpl(Collection<? extends Scope> c) {
        super(c);
    }

    @Override
    public Elements<Scope> getOrderedCopy() {
        ScopesImpl scopes = new ScopesImpl(this);
        scopes.sort(new ElementComparator());
        return scopes;
    }
}
