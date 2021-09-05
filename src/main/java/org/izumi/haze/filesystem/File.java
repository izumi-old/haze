package org.izumi.haze.filesystem;

import java.nio.file.Files;
import java.nio.file.Path;

public class File extends Element {
    private String filename;
    private Extension extension;

    public File(Path path) {
        super(path);
        if (!Files.isRegularFile(path)) {
            throw new IllegalArgumentException("Given path is not a file. Given: " + path);
        }

        String[] parts = path.toAbsolutePath().toString().split("/");
        String[] nameParts = parts[parts.length - 1].split("\\.");
        filename = nameParts[0];
        if (nameParts.length > 1) {
            extension = new ExtensionImpl(nameParts[nameParts.length - 1]);
        }
    }

    public boolean isSupported() {
        return SupportedExtension.isGivenOneOfSupported(extension);
    }

    public boolean isNotSupported() {
        return !isSupported();
    }

    public Extension getExtension() {
        return extension;
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        return super.equals(obj);
    }
}
