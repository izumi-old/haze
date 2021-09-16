package org.izumi.haze.modules.java.source;

import org.izumi.haze.modules.java.parsing.TopLevelClassesParsing;
import org.izumi.haze.modules.java.parsing.TopLevelScopesParsing;
import org.izumi.haze.modules.java.util.Classes;
import org.izumi.haze.modules.java.util.Scopes;
import org.izumi.haze.util.Range;
import org.izumi.haze.util.StringBuilder;

import java.util.Collection;
import java.util.LinkedList;

public class Scope {
    private final StringBuilder value;
    private Range range;

    private Classes topLevelClasses;
    private Scopes topLevelScopes;
    private Collection<Variable> variables;

    public Scope(StringBuilder value, Range range) {
        this.value = value;
        this.range = range;
    }

    public void shift(int changed) {
        range = range.shift(changed);
        topLevelClasses.shift(changed);
        topLevelScopes.shift(changed);
    }

    void parse() {
        Range searchIn = new Range(range.start + 1, range.end - 1);
        topLevelClasses = new TopLevelClassesParsing(value, searchIn).parse();
        topLevelScopes = new TopLevelScopesParsing(value, searchIn).parse();
        variables = new LinkedList<>(); //TODO: implement
    }
}
