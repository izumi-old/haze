package org.izumi.haze.modules.impl.java.source;

import org.izumi.haze.modules.impl.java.util.Classes;
import org.izumi.haze.modules.impl.java.util.Scopes;
import org.izumi.haze.util.Range;
import org.izumi.haze.string.HazeString;

import java.util.Collection;

public class Scope implements Element {
    private final HazeString value;
    private Range range;
    private long declarationOrder;

    private Classes topLevelClasses;
    private Scopes topLevelScopes;
    private Collection<Variable> variables;

    public Scope(HazeString value, Range range) {
        this.value = value;
        this.range = range;
    }

    @Override
    public long getDeclarationOrder() {
        return declarationOrder;
    }

    @Override
    public void setDeclarationOrder(long order) {
        this.declarationOrder = order;
    }

    public void renameClassAndUsages(Class clazz, String replacement) {
        //TODO: implement
    }

    void parse() {
        /*Range searchIn = new Range(range.start + 1, range.end - 1);
        topLevelClasses = new TopLevelClassesParsing(value, searchIn).parse();
        topLevelScopes = new TopLevelScopesParsing(value, searchIn).parse();
        variables = new LinkedList<>(); //TODO: implement*/
    }
}
