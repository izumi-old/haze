package org.izumi.haze.filesystem;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

public class Directory extends Element {
    public Directory(Path path) {
        super(path);
        if (Files.exists(path) && !Files.isDirectory(path)) {
            throw new IllegalArgumentException("Given path is not a directory. Given: " + path);
        }
    }

    public Map<String, File> getAllRelativizedSubFiles() {
        try {
            Map<String, File> result = new HashMap<>();
            Files.walk(path).filter(path1 -> !path.equals(path1)).filter(Files::isRegularFile).forEach(path1 -> {
                File file = new LazyFile(path1);
                String relativized = path.relativize(path1).toString()/*.replace(file.getFullName(), "")*/;
                result.put(relativized, file);
            });

            return result;
        } catch (IOException ex) {
            throw new FilesystemException("An error occurred while walking substructure. ", ex);
        }
    }
}
