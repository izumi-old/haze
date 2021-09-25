package org.izumi.haze.modules.impl.java.source;

import org.izumi.haze.string.HazeString;
import org.izumi.haze.string.SeparatedStringPredicate;

public class Import implements Element {
    private HazeString value;
    private long declarationOrder;

    public Import(HazeString string) {
        this.value = string;
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
