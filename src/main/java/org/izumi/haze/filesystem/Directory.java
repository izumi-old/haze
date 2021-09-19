package org.izumi.haze.filesystem;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

public class Directory extends Element {
    public Directory(Path path) {
        super(path);
        if (Files.exists(path) && !Files.isDirectory(path)) {
            throw new IllegalArgumentException("Given path is not a directory. Given: " + path);
        }
    }

    @Override
    public void delete() {
        try {
            if (Files.exists(path)) {
                String[] elements = path.toFile().list();
                if (elements != null) {
                    for (String element : elements) {
                        Path path = this.path.toAbsolutePath().resolve(element);
                        if (Files.isDirectory(path)) {
                            new Directory(path).delete();
                        } else if (Files.isRegularFile(path)) {
                            new LazyFile(path).delete();
                        }
                    }
                }

                Files.delete(this.path);
            }

        } catch (IOException ex) {
            throw new FilesystemException("An error while deleting a directory occurred", ex);
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

    public void createIfNotExists() {
        try {
            String[] parts = path.toAbsolutePath().toString().split("/");
            for (int i = 0; i < parts.length - 1; i++) {
                StringBuilder directoryPath = new StringBuilder();
                for (int j = 0; j <= i; j++) {
                    directoryPath.append("/").append(parts[j]);
                }

                Path directory = Paths.get(directoryPath.toString());
                if (!Files.exists(directory)) {
                    Files.createDirectory(directory);
                }
            }
        } catch (IOException var5) {
            throw new FilesystemException("An error occurred while creating a directory", var5);
        }
    }

    public void clear() {
        if (Files.exists(path)) {
            String[] elements = path.toFile().list();
            if (elements != null) {
                for (String element : elements) {
                    Path path = this.path.toAbsolutePath().resolve(element);
                    if (Files.isDirectory(path)) {
                        new Directory(path).delete();
                    } else if (Files.isRegularFile(path)) {
                        new LazyFile(path).delete();
                    }
                }
            }
        }
    }
}
