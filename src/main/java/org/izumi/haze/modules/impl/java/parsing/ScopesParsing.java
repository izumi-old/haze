package org.izumi.haze.modules.impl.java.parsing;

import org.izumi.haze.modules.impl.java.source.Scope;
import org.izumi.haze.string.LemmaString;
import org.izumi.haze.util.CompareList;
import org.izumi.haze.util.Range;

import java.util.SortedMap;
import java.util.TreeMap;

public class ScopesParsing {
    private final LemmaString value;

    public ScopesParsing(CharSequence sequence) {
        this.value = new LemmaString(sequence);
    }

    public SortedMap<Range, Scope> parse() {
        CompareList<Range> ranges = new CompareList<>();
        ranges.addAll(new TopLevelElementsIterator(value));

        SortedMap<Range, Scope> result = new TreeMap<>();
        ranges.forEach(range -> {
            LemmaString string = value.getSub(range);
            Recognition.Element element = new Recognition(string).recognize();
            if (element.isScope()) {
                Scope scope = new Scope(string);
                scope.parse();
                result.put(range, scope);
            }
        });

        return result;
    }
}
