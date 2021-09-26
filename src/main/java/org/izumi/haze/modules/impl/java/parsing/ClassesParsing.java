package org.izumi.haze.modules.impl.java.parsing;

import org.izumi.haze.modules.impl.java.source.Class;
import org.izumi.haze.string.LemmaString;
import org.izumi.haze.util.CompareList;
import org.izumi.haze.util.Range;

import java.util.SortedMap;
import java.util.TreeMap;

public class ClassesParsing {
    private final LemmaString value;

    public ClassesParsing(CharSequence sequence) {
        this.value = new LemmaString(sequence);
    }

    public SortedMap<Range, Class> parse() {
        CompareList<Range> ranges = new CompareList<>();
        ranges.addAll(new TopLevelElementsIterator(value));

        SortedMap<Range, Class> result = new TreeMap<>();
        ranges.forEach(range -> {
            LemmaString string = value.getSub(range);
            X.Element element = new X(string).x();
            if (element.isClass()) {
                Class clazz = new Class(string);
                clazz.parse();
                result.put(range, clazz);
            }
        });

        return result;
    }
}
