package org.izumi.haze.modules.impl.java.parsing;

import lombok.RequiredArgsConstructor;
import org.izumi.haze.HazeException;
import org.izumi.haze.modules.impl.java.source.Keyword;
import org.izumi.haze.modules.impl.java.source.Type;
import org.izumi.haze.string.HazeString;

import java.util.Collection;

@RequiredArgsConstructor
public class Recognition {
    private final Collection<Keyword> keywords;
    private final Collection<HazeString> unresolved;

    /**
     * @return true if given is a class or an annotation (@interface) or an interface or an enum
     */
    public boolean isAnyClassType() {
        Element recognized = recognize();
        return recognized == Element.CLASS || recognized == Element.ANNOTATION ||
                recognized == Element.INTERFACE || recognized == Element.ENUM;
    }

    public boolean isScope() {
        return recognize() == Element.SCOPE;
    }

    private Element recognize() {
        if (keywords.contains(Keyword.VOID)) {
            return Element.METHOD;
        }

        for (Type type : Type.values()) {
            if (keywords.contains(type.getKeyword())) {
                return Element.of(type);
            }
        }

        throw new HazeException("Unable to recognize. Keywords: " + keywords +
                ". Unresolved: " + unresolved.toString());
    }

    enum Element {
        CLASS, ENUM, INTERFACE, ANNOTATION, METHOD, SCOPE;

        public static Element of(Type type) {
            if (type == Type.CLASS) {
                return CLASS;
            } else if (type == Type.ENUM) {
                return ENUM;
            } else if (type == Type.INTERFACE) {
                return INTERFACE;
            } else if (type == Type.ANNOTATION) {
                return ANNOTATION;
            }

            throw new HazeException("An unknown typeEnum was given. The typeEnum: " + type);
        }
    }
}
