package org.izumi.haze.modules.impl.java.source;

import org.izumi.haze.string.HazeRegexString;
import org.izumi.haze.string.Regex;

public class Annotation implements Element {
    private HazeRegexString string;

    public Annotation(CharSequence sequence) {
        this.string = new HazeRegexString(sequence);
    }

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
        if (string.contains(name)) {
            string = string.replaceAll(new Regex(name), replacement);
        }
    }

    @Override
    public String toString() {
        return string.toString();
    }
}
