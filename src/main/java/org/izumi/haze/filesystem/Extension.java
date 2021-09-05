package org.izumi.haze.filesystem;

/**
 * Use toString() to get the string value of an instance
 */
public interface Extension {
    default boolean is(String candidate) {
        return toString().equalsIgnoreCase(candidate);
    }
}
