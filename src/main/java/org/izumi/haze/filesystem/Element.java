package org.izumi.haze.filesystem;

import lombok.EqualsAndHashCode;
import lombok.NonNull;

import java.nio.file.Path;

@EqualsAndHashCode
public abstract class Element {
    protected Path path;

    public Element(@NonNull Path path) {
        this.path = path;
    }

    public abstract void delete();
}
