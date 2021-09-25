package org.izumi.haze.modules.impl.java.source;

import org.izumi.haze.string.HazeString;
import org.izumi.haze.string.SeparatedStringPredicate;

public class Comment implements Element {
    private HazeString value;
    private long declarationOrder;

    public Comment(HazeString value) {
        this.value = value;
    }

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
        value = value.replaceAllIf(name, replacement, new SeparatedStringPredicate());
    }

    @Override
    public String toString() {
        return value.toString();
    }
}
