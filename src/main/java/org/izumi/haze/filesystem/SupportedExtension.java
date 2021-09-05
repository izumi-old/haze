package org.izumi.haze.filesystem;

public enum SupportedExtension implements Extension {
    JAVA("java");

    public static boolean isGivenOneOfSupported(Extension candidate) {
        if (candidate == null) {
            return false;
        }

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
