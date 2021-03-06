package org.izumi.haze.filesystem;

import lombok.EqualsAndHashCode;

@EqualsAndHashCode
public class ExtensionImpl implements Extension {
    private static final String BLANK_EXTENSION_VALUE = "";
    public static final Extension EMPTY_EXTENSION = new ExtensionImpl(BLANK_EXTENSION_VALUE);
    private final String value;

    public ExtensionImpl(String value) {
        if (value == null) {
            this.value = BLANK_EXTENSION_VALUE;
        } else if (value.contains(".")) {
            throw new IllegalArgumentException("Given value contains dots. Given: " + value);
        } else {
            this.value = value;
        }
    }

    @Override
    public String toString() {
        return value;
    }
}
