package org.izumi.haze.modules.java;

import lombok.NonNull;

import java.util.Optional;

public enum AccessModifier {
    PUBLIC(Keyword.PUBLIC.toString()),
    DEFAULT(""),
    PROTECTED(Keyword.PROTECTED.toString()),
    PRIVATE(Keyword.PRIVATE.toString());

    public static Optional<AccessModifier> of(@NonNull String value) {
        for (AccessModifier modifier : AccessModifier.values()) {
            if (modifier.value.equals(value)) {
                return Optional.of(modifier);
            }
        }

        return Optional.empty();
    }

    AccessModifier(String value) {
        this.value = value;
    }

    private final String value;

    @Override
    public String toString() {
        return value;
    }
}
