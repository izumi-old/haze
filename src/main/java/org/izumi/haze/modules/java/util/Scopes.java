package org.izumi.haze.modules.java.util;

import org.izumi.haze.modules.java.source.Scope;

import java.util.LinkedList;

public class Scopes extends LinkedList<Scope> {
    public void shift(int changed) {
        for (Scope scope : this) {
            scope.shift(changed);
        }
    }
}
