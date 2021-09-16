package org.izumi.haze.modules.java;

import java.util.Collection;
import java.util.LinkedList;

public class Keywords extends LinkedList<Keyword> {
    public boolean hasAny(Collection<Keyword> keywords) {
        for (Keyword keyword : keywords) {
            if (contains(keyword)) {
                return true;
            }
        }

        return false;
    }
}
