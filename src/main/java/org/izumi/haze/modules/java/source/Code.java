package org.izumi.haze.modules.java.source;

import lombok.NonNull;
import org.izumi.haze.modules.java.parsing.TopLevelClassesParsing;
import org.izumi.haze.util.Range;
import org.izumi.haze.util.StringBuilder;

import java.util.ArrayList;
import java.util.Collection;

public class Code {
    private final StringBuilder value;
    private Collection<Class> topLevelClasses;

    public Code(@NonNull String value) {
        this.value = new StringBuilder(value);
    }

    public Collection<Class> getClasses() {
        return new ArrayList<>(topLevelClasses);
    }

    /*public void replaceAll(@NonNull String toReplace, @NonNull String replacement) {
        for (Class clazz : topLevelClasses) {
            clazz.replaceAll(toReplace, replacement);
        }
    }*/

    public void renameAll(@NonNull String what, @NonNull String onWhat) {
        int changed = value.replaceAllIfSeparate(what, onWhat);
        for (Class clazz : topLevelClasses) {
            clazz.shift(changed);
        }
    }

    public void deleteAll(@NonNull String what) {
        value.replaceAllIfSeparate(what, "");
    }

    public void parse() {
        this.topLevelClasses = new TopLevelClassesParsing(value, new Range(0, value.length())).parse();
        for (Class topLevelClass : topLevelClasses) {
            topLevelClass.parse();
        }
    }

    @Override
    public String toString() {
        return "X"; //TODO:
    }
}
