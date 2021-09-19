package org.izumi.haze.modules.impl.java.parsing;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.izumi.haze.HazeException;
import org.izumi.haze.modules.impl.java.source.Keyword;
import org.izumi.haze.modules.impl.java.source.Type;
import org.izumi.haze.util.Range;
import org.izumi.haze.string.HazeString;

import java.util.Collection;
import java.util.LinkedList;
import java.util.Optional;

@RequiredArgsConstructor
public class Recognition {

    @NonNull
    private final HazeString value;

    @NonNull
    private final Range bracesRange;
    private Collection<String> unresolvedLemmas;

    public Element recognize() {
        if (bracesRange.start == 0) {
            return Element.SCOPE;
        }

        String value = this.value.substring(new Range(0, bracesRange.end));
        String before = value.substring(0, bracesRange.start);

        unresolvedLemmas = new LinkedList<>();
        while (true) {
            int lastSpaceIndex = before.lastIndexOf(" ");
            if (lastSpaceIndex == -1) {
                break; //TODO: ?
            }

            String lemma = before.substring(lastSpaceIndex + 1);
            for (Type type : Type.values()) {
                if (lemma.contains(type.toString())) {
                    return Element.of(type);
                }
            }

            if (lemma.contains(Keyword.VOID.toString())) {
                return Element.METHOD;
            }

            unresolvedLemmas.add(lemma);
            Optional<Element> resolved = resolveLemmas();
            if (resolved.isPresent()) {
                return resolved.get();
            }

            before = before.substring(0, lastSpaceIndex);
        }

        return Element.SCOPE;
    }

    private Optional<Element> resolveLemmas() {
        return Optional.empty(); //TODO: try recognize signature
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
