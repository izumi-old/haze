package org.izumi.haze.modules.impl.java.source;

public class Annotation implements Element {
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
        //TODO:
    }
}
