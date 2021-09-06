package org.izumi.haze.filesystem;

import lombok.NonNull;

public enum SupportedExtension implements Extension {
    JAVA("java");

    public static boolean isGivenOneOfSupported(@NonNull Extension candidate) {
        String candidateString = candidate.toString();
        for (SupportedExtension extension : SupportedExtension.values()) {
            if (extension.value.equals(candidateString)) {
                return true;
            }
        }

        return false;
    }

    private final String value;

    SupportedExtension(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return value;
    }
}
