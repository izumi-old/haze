package org.izumi.haze.modules.java.source;

import lombok.RequiredArgsConstructor;
import org.izumi.haze.string.HazeString;
import org.izumi.haze.string.SeparatedStringPredicate;

@RequiredArgsConstructor
public class Comment implements Element {
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
        value.replaceAllIfSeparate(clazz.getName(), replacement, new SeparatedStringPredicate());
    }

    @Override
    public String toString() {
        return value.toString();
    }
}
