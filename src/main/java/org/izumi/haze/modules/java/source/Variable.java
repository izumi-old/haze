package org.izumi.haze.modules.java.source;

import lombok.RequiredArgsConstructor;
import org.izumi.haze.string.HazeString;

@RequiredArgsConstructor
public class Variable implements Element {
    private final HazeString string;
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
    public void renameClassAndUsages(Class clazz, String replacement) {
        //TODO:
    }

    @Override
    public String toString() {
        return string.toString();
    }
}
