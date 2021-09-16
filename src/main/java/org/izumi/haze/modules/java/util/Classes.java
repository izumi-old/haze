package org.izumi.haze.modules.java.util;

import org.izumi.haze.modules.java.source.Class;

import java.util.LinkedList;

public class Classes extends LinkedList<Class> {
    public void shift(int changed) {
        for (Class clazz : this) {
            clazz.shift(changed);
        }
    }
}
