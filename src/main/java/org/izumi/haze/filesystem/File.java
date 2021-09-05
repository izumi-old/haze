package org.izumi.haze.filesystem;

import java.nio.file.Files;
import java.nio.file.Path;

public class File extends Element {
    private String filename;
    private String extension;

    public File(Path path) {
        super(path);
        if (!Files.isRegularFile(path)) {
            throw new IllegalArgumentException("Given path is not a file. Given: " + path);
        }

        String[] parts = path.toAbsolutePath().toString().split("/");
        String[] nameParts = parts[parts.length - 1].split("\\.");
        filename = nameParts[0];
        if (nameParts.length > 1) {
            extension = nameParts[nameParts.length - 1];
        }
    }

    public boolean isSupported() {
        return SupportedExtension.isGivenOneOfSupported(extension);
    }

    public boolean isNotSupported() {
        return !isSupported();
    }
}
