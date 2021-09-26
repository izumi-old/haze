package org.izumi.haze.modules.impl.java.parsing;

import org.izumi.haze.modules.impl.java.source.Annotation;
import org.izumi.haze.string.HazeRegexString;
import org.izumi.haze.string.Regex;

import java.util.Collection;
import java.util.stream.Collectors;

public class AnnotationsParsing {
    private final HazeRegexString string;

    public AnnotationsParsing(CharSequence sequence) {
        this.string = new HazeRegexString(sequence);
    }

    public Collection<Annotation> parse() {
        return string.split(new Regex("@")).stream()
                .filter(string -> !string.equals(""))
                .map(string -> new Annotation("@" + string.trim()))
                .collect(Collectors.toList());
    }
}
