package org.izumi.haze.filesystem;

import java.nio.file.Path;
import java.util.Objects;

public abstract class Element {
    protected final Path path;

    public Element(Path path) {
        if (path == null) {
            throw new NullPointerException();
        }

        this.path = path;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Element element = (Element) o;
        return Objects.equals(path, element.path);
    }

    @Override
    public int hashCode() {
        return Objects.hash(path);
    }
}
