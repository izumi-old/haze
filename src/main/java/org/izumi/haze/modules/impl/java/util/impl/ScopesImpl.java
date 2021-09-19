package org.izumi.haze.modules.impl.java.util.impl;

import org.izumi.haze.modules.impl.java.source.Scope;
import org.izumi.haze.modules.impl.java.util.Elements;
import org.izumi.haze.modules.impl.java.util.Scopes;

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
