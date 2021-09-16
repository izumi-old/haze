package org.izumi.haze.modules.java;

import lombok.NonNull;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public enum Type {
    CLASS(Keyword.CLASS.toString()),
    INTERFACE(Keyword.INTERFACE.toString()),
    ANNOTATION(Keyword.ANNOTATION.toString()),
    ENUM(Keyword.ENUM.toString());

    public static Optional<Type> of(@NonNull Keyword keyword) {
        return of(keyword.toString());
    }

    public static Optional<Type> of(@NonNull String value) {
        for (Type type : Type.values()) {
            if (type.value.equals(value)) {
                return Optional.of(type);
            }
        }

        return Optional.empty();
    }

    public static Collection<Keyword> asKeywords() {
        return List.of(Keyword.CLASS, Keyword.INTERFACE, Keyword.ANNOTATION, Keyword.ENUM);
    }

    Type(String value) {
        this.value = value;
    }

    private final String value;

    @Override
    public String toString() {
        return value;
    }
}
