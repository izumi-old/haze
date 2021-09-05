package org.izumi.haze.filesystem;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collection;
import java.util.LinkedList;
import java.util.stream.Collectors;

public class Directory extends Element {
    public Directory(Path path) {
        super(path);
        if (!Files.isDirectory(path)) {
            throw new IllegalArgumentException("Given path is not a directory. Given: " + path);
        }
    }

    public Collection<File> getSubFiles() {
        try {
            return Files.walk(path, 1)
                    .filter(Files::isRegularFile)
                    .map(File::new)
                    .collect(Collectors.toList());
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    public Collection<Directory> getSubDirectories() {
        try {
            return Files.walk(path, 1)
                    .filter(path1 -> !path.equals(path1))
                    .filter(Files::isDirectory)
                    .map(Directory::new)
                    .collect(Collectors.toList());
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    public Collection<File> getAllSubFiles() {
        Collection<File> result = new LinkedList<>(getSubFiles());
        for (Directory directory : getSubDirectories()) {
            result.addAll(directory.getAllSubFiles());
        }

        return result;
    }
}
