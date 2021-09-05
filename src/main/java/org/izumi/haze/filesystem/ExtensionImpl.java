package org.izumi.haze.filesystem;

import lombok.EqualsAndHashCode;

@EqualsAndHashCode
public class ExtensionImpl implements Extension {
    private final String value;

    public ExtensionImpl(String value) {
        if (value.contains(".")) {
            throw new IllegalArgumentException("Given value contains dots. Given: " + value);
        }

        this.value = value;
    }

    @Override
    public String toString() {
        return value;
    }
}
