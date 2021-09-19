package org.izumi.haze.modules.impl.java.source;

import lombok.RequiredArgsConstructor;
import org.izumi.haze.string.HazeString;
import org.izumi.haze.string.SeparatedStringPredicate;

@RequiredArgsConstructor
public class Import implements Element {
    private final HazeString value;
    private long declarationOrder;

    @Override
    public long getDeclarationOrder() {
        return declarationOrder;
    }

    @Override
    public void setDeclarationOrder(long order) {
        this.declarationOrder = order;
    }

    @Override
    public void renameClassAndUsages(String name, String replacement) {
        value.replaceAllIfSeparate(name, replacement, new SeparatedStringPredicate());
    }

    @Override
    public String toString() {
        return value.toString();
    }
}
