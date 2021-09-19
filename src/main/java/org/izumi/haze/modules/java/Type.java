package org.izumi.haze.modules.java;

import lombok.NonNull;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public enum Type {
    CLASS(Keyword.CLASS),
    INTERFACE(Keyword.INTERFACE),
    ANNOTATION(Keyword.ANNOTATION),
    ENUM(Keyword.ENUM);

    public static Optional<Type> of(@NonNull Keyword keyword) {
        return of(keyword.toString());
    }

    public static Optional<Type> of(@NonNull String value) {
        for (Type type : Type.values()) {
            if (type.toString().equals(value)) {
                return Optional.of(type);
            }
        }

        return Optional.empty();
    }

    public static Collection<Keyword> asKeywords() {
        return List.of(Keyword.CLASS, Keyword.INTERFACE, Keyword.ANNOTATION, Keyword.ENUM);
    }

    Type(Keyword keyword) {
        this.keyword = keyword;
    }

    private final Keyword keyword;

    public Keyword getKeyword() {
        return keyword;
    }

    @Override
    public String toString() {
        return keyword.toString();
    }
}
