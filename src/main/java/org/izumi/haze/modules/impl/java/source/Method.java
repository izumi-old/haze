package org.izumi.haze.modules.impl.java.source;

import java.util.Collection;

public class Method implements Element {
    private Collection<Class> topLevelClasses;
    private Collection<Scope> topLevelScopes;
    private Collection<Variable> variables;
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

    /*
    how can I detect a method?
    a method always have braces. difference with scope is that it has signature
    at least:
    RETURNING_TYPE NAME (){
    or
    RETURNING_TYPE NAME() {
    or
    RETURNING_TYPE NAME(){

    So I can trace braces and try to find signature before the braces.
    Does it mean now I detect scopes incorrectly? Yes, it should.
    Scope will be an unnamed block of code.
     */

    void x () {

    }
}
