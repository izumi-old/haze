package org.izumi.haze.filesystem;

import java.nio.file.Path;

public abstract class Element {
    protected final Path path;

    public Element(Path path) {
        if (path == null) {
            throw new NullPointerException();
        }

        this.path = path;
    }
}
