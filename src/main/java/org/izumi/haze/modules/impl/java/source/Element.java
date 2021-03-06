package org.izumi.haze.modules.impl.java.source;

public interface Element extends Comparable<Element> {
    long getDeclarationOrder();
    void setDeclarationOrder(long order);

    void renameClassAndUsages(String name, String replacement);

    @Override
    default int compareTo(Element o) {
        return Long.compare(getDeclarationOrder(), o.getDeclarationOrder());
    }
}
