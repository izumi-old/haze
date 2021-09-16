package org.izumi.haze.modules.java.source;

import org.izumi.haze.modules.java.AccessModifier;
import org.izumi.haze.modules.java.parsing.TopLevelClassesParsing;
import org.izumi.haze.modules.java.Type;
import org.izumi.haze.modules.java.parsing.TopLevelScopesParsing;
import org.izumi.haze.modules.java.util.Classes;
import org.izumi.haze.modules.java.util.Scopes;
import org.izumi.haze.util.Range;
import org.izumi.haze.util.StringBuilder;

import java.util.Collection;
import java.util.LinkedList;

public class Class {
    private final StringBuilder value;
    private Range range;

    private Classes topLevelClasses;
    private Scopes topLevelScopes;
    private Collection<Method> methods;
    private Collection<Variable> variables;

    private AccessModifier modifier;
    private Type type;
    private String name;
    private boolean isAbstract;
    private boolean isFinal;
    private boolean isStatic;

    public Class(StringBuilder value, Range range) {
        this.value = value;
        this.range = range;
    }

    public String getName() {
        return name;
    }

    public void shift(int changed) {
        range = range.shift(changed);
        topLevelClasses.shift(changed);
        topLevelScopes.shift(changed);
    }

    void parse() {
        initFields();

        int start = this.value.firstIndexOf("{", range);
        int end = this.value.lastIndexOf("}", range);

        Range searchIn = new Range(start, end);

        this.topLevelClasses = new TopLevelClassesParsing(value, searchIn).parse();
        this.topLevelScopes = new TopLevelScopesParsing(value, searchIn).parse();
        this.methods = new LinkedList<>(); //TODO:
        this.variables = new LinkedList<>(); //TODO:

        for (Class topLevelClass : topLevelClasses) {
            topLevelClass.parse();
        }
        for (Scope topLevelScope : topLevelScopes) {
            topLevelScope.parse();
        }
    }

    private void initFields() {
        String signature = value.substring(0, value.indexOf("{")).trim();
        signature = signature.replaceAll(" implements.*", ""); //remove implements information
        signature = signature.replaceAll(" extends.*", ""); //remove extends information
        signature = signature.replaceAll("<.+>", ""); //remove generics information

        isAbstract = signature.contains("abstract");
        isFinal = signature.contains("final");
        isStatic = signature.contains("static");

        for (AccessModifier accessModifier : AccessModifier.values()) {
            if (accessModifier != AccessModifier.DEFAULT && signature.contains(accessModifier.toString())) {
                modifier = accessModifier;
                break;
            }
        }
        if (modifier == null) {
            modifier = AccessModifier.DEFAULT;
        }

        type = Type.CLASS; //to satisfy compiler, there always be a type in compilable Java code
        for (Type type1 : Type.values()) {
            if (signature.contains(type1.toString())) {
                type = type1;
                break;
            }
        }

        String typeAsString = type.toString();
        int nameIndex = signature.indexOf(typeAsString) + typeAsString.length() + 1;
        name = signature.substring(nameIndex);
    }

    @Override
    public String toString() {
        return value.substring(range);
    }
}
