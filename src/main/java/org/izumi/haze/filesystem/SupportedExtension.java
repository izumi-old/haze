package org.izumi.haze.filesystem;

public enum SupportedExtension {
    JAVA("java");

    public static boolean isGivenOneOfSupported(String candidate) {
        for (SupportedExtension extension : SupportedExtension.values()) {
            if (extension.value.equals(candidate)) {
                return true;
            }
        }

        return false;
    }

    private final String value;

    SupportedExtension(String value) {
        this.value = value;
    }
}
