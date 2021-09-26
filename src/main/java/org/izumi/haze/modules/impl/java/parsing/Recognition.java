package org.izumi.haze.modules.impl.java.parsing;

import org.izumi.haze.modules.impl.java.source.Keyword;
import org.izumi.haze.string.HazeRegexString;
import org.izumi.haze.string.LemmaString;
import org.izumi.haze.string.Regex;
import org.izumi.haze.util.ExtendedList;
import org.izumi.haze.util.Range;

import java.util.Collection;
import java.util.Optional;

/**
 * Accept a string which contains braces and (optional) signature and annotations
 */
public class Recognition {
    private final LemmaString string;
    private final HazeRegexString regexString;

    public Recognition(CharSequence sequence) {
        string = new LemmaString(sequence);
        regexString = new HazeRegexString(sequence);
    }

    public Element recognize() {
        Optional<Range> bracesRangeOptional = regexString.firstRangeOfRegex(new Regex("\\{.*}")); //TODO: can be in literal or in comment
        Range searchBefore = bracesRangeOptional.get();
        Optional<Range> optionalLemmaRange = string.getLemmaRangeBefore(searchBefore);
        if (optionalLemmaRange.isEmpty()) {
            return Element.SCOPE;
        }

        while (optionalLemmaRange.isPresent()) {
            Range lemmaRange = optionalLemmaRange.get();
            LemmaString string = this.string.getSub(lemmaRange);

            Collection<CharSequence> sequences = new ExtendedList<>(
                    Keyword.CLASS, Keyword.ANNOTATION, Keyword.ENUM, Keyword.INTERFACE);

            if (string.containsAny(sequences)) {
                return Element.CLASS;
            }

            searchBefore = new Range(lemmaRange.start, searchBefore.end);
            optionalLemmaRange = this.string.getLemmaRangeBefore(searchBefore);
        }

        return Element.METHOD;
    }

    enum Element {
        CLASS, METHOD, SCOPE;

        public boolean isClass() {
            return this == CLASS;
        }

        public boolean isMethod() {
            return this == METHOD;
        }

        public boolean isScope() {
            return this == SCOPE;
        }
    }
}
