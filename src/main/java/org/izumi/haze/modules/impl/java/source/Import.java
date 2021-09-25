package org.izumi.haze.modules.impl.java.source;

import org.izumi.haze.string.HazeRegexString;
import org.izumi.haze.string.Regex;
import org.izumi.haze.string.SeparatedStringPredicate;

public class Import implements Element {
    private HazeRegexString value;
    private long declarationOrder;

    public Import(HazeRegexString string) {
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
        value = value.replaceAllIf(new Regex(name), replacement, new SeparatedStringPredicate());
    }

    @Override
    public String toString() {
        return value.toString();
    }
}
