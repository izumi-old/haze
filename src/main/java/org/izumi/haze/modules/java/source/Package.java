package org.izumi.haze.modules.java.source;

import lombok.RequiredArgsConstructor;
import org.izumi.haze.util.HazeString;

@RequiredArgsConstructor
public class Package implements Element {
    public static final Package DEFAULT_PACKAGE = new Package(new HazeString(""));
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

    public boolean isDefault() {
        return string.equals(new HazeString(""));
    }

    public boolean isNotDefault() {
        return !isDefault();
    }

    @Override
    public String toString() {
        return string.toString();
    }
}
