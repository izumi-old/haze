package org.izumi.haze.modules.impl.java.source;

import lombok.NonNull;

import java.util.Optional;

public enum AccessModifier {
    PUBLIC(Keyword.PUBLIC),
    DEFAULT(Keyword.DEFAULT_ACCESS_MODIFIER),
    PROTECTED(Keyword.PROTECTED),
    PRIVATE(Keyword.PRIVATE);

    public static Optional<AccessModifier> of(@NonNull String value) {
        for (AccessModifier modifier : AccessModifier.values()) {
            if (modifier.toString().equals(value)) {
                return Optional.of(modifier);
            }
        }

        return Optional.empty();
    }

    AccessModifier(Keyword keyword) {
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
