package org.izumi.haze.modules.java.source;

import lombok.RequiredArgsConstructor;
import org.izumi.haze.util.Range;
import org.izumi.haze.util.StringBuilder;

@RequiredArgsConstructor
public class Imports {
    private final StringBuilder value;
    private final Range range;

    public int renameClass(Class clazz, String replacement) {
        return value.replaceAllIfSeparate(range, clazz.getName(), replacement);
        /*int changed = 0;
        int start = 0;
        while (start < range.end) {
            int importingStart = value.indexOf(Keyword.IMPORT.toString(), start);
            int importingEnd = value.indexOf(";", importingStart);
            start = importingEnd;
            int lastDot = value.lastIndexOf(".", new Range(importingStart, importingEnd));
            if (lastDot == -1) {
                continue;
            }

            Range rangeOfProbablyClassName = new Range(lastDot + 1, importingEnd);
            boolean haveBeReplaced = value.substring(rangeOfProbablyClassName).trim().equals(clazz.getName());
            if (haveBeReplaced) {
                changed++;
                value.replace(rangeOfProbablyClassName, replacement);
            }
        }

        return changed * (clazz.getName().length() - replacement.length());*/
    }
}
