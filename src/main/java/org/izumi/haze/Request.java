package org.izumi.haze;

import lombok.RequiredArgsConstructor;
import org.izumi.haze.filesystem.Directory;
import org.izumi.haze.filesystem.File;
import org.izumi.haze.filesystem.Files;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.geom.IllegalPathStateException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collection;
import java.util.LinkedList;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class Request {
    private static final Logger log = LoggerFactory.getLogger(Request.class);
    private final Collection<String> paths;
    private final Files files = new Files();
    private boolean handled = false;

    public void handlePaths() {
        if (handled) {
            log.warn("Got a request to handle paths, but the paths were already handled");
            return;
        }

        Collection<File> notFiltered = new LinkedList<>();
        for (String pathToFolderOrFile : paths) {
            if (pathToFolderOrFile == null) {
                log.warn("One of the given paths is null");
                continue;
            }

            if (pathToFolderOrFile.isBlank()) {
                log.warn("One of the given paths is blank");
                continue;
            }

            try {
                Path path = Paths.get(pathToFolderOrFile);
                if (java.nio.file.Files.isRegularFile(path)) {
                    notFiltered.add(new File(path));
                } else if (java.nio.file.Files.isDirectory(path)) {
                    notFiltered.addAll(new Directory(path).getAllSubFiles());
                } else {
                    log.warn("Cannot determine type of the given type (is it a directory or a regular file?)");
                }
            } catch (IllegalPathStateException ex) {
                log.warn("One of the given paths is invalid. THe path: " + pathToFolderOrFile);
            }
        }
        files.addAll(notFiltered.stream().filter(File::isSupported).collect(Collectors.toList()));

        handled = true;
    }

    public Files getFiles() {
        if (!handled) {
            log.warn("A collection of files to obfuscate was requested, but paths are not handled yet");
        }

        return new Files(files);
    }
}
