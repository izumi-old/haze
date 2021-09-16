package org.izumi.haze.modules.java.source;

import java.util.Collection;

public class Method {
    private Collection<Class> topLevelClasses;
    private Collection<Scope> topLevelScopes;
    private Collection<Variable> variables;

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
