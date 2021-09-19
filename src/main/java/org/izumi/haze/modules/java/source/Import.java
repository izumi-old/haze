package org.izumi.haze.modules.java.source;

import lombok.RequiredArgsConstructor;
import org.izumi.haze.util.HazeString;

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
    public void renameClassAndUsages(Class clazz, String replacement) {
        value.replaceAllIfSeparate(clazz.getName(), replacement);
    }

    @Override
    public String toString() {
        return value.toString();
    }
}
