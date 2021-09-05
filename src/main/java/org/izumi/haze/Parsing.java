package org.izumi.haze;

import lombok.RequiredArgsConstructor;
import org.izumi.haze.filesystem.Directory;
import org.izumi.haze.filesystem.File;
import org.izumi.haze.filesystem.LazyFile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.geom.IllegalPathStateException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collection;
import java.util.Map;

@RequiredArgsConstructor
public class Parsing {
    private static final Logger log = LoggerFactory.getLogger(Parsing.class);
    private final Collection<String> input;
    private final Wishes wishes = new Wishes();
    private boolean parsed = false;

    public void parse() {
        if (parsed) {
            log.warn("Got a request to parse input, but the input was already parsed");
            return;
        }

        for (String anInput : input) {
            validateInput(anInput).ifGoodOtherwise(() -> {
                String[] tmp = anInput.split("#");
                String toSave;
                if (tmp.length == 2) {
                    toSave = tmp[1];
                } else {
                    toSave = Settings.DEFAULT_SAVING_PATH_AS_STRING;
                }

                try {
                    Path sourcePath = Paths.get(anInput);
                    if (java.nio.file.Files.isRegularFile(sourcePath)) {
                        File source = new LazyFile(sourcePath);
                        Path targetPath = Paths.get(toSave + "/" + source.getFullName());
                        File target = new LazyFile(targetPath);
                        wishes.add(new Wish(source, target));
                    } else if (java.nio.file.Files.isDirectory(sourcePath)) {
                        Directory directory = new Directory(sourcePath);
                        Map<String, File> relativizedSubFiles = directory.getAllRelativizedSubFiles();
                        for (Map.Entry<String, File> relativizedSubFile : relativizedSubFiles.entrySet()) {
                            File source = relativizedSubFile.getValue();
                            File target = new LazyFile(Paths.get(toSave + "/" + relativizedSubFile.getKey()));
                            wishes.add(new Wish(source, target));
                        }
                    } else {
                        log.warn("Cannot determine type of the given type (is it a directory or a regular file?)");
                    }
                } catch (IllegalPathStateException ex) {
                    log.warn("One of the given wishes is invalid. The wish: " + anInput);
                }
            }, log::warn);
        }

        wishes.filter(wish -> wish.source.isSupported());
        parsed = true;
    }

    public Wishes getWishes() {
        if (!parsed) {
            log.warn("A collection of files to obfuscate was requested, but wishes are not parsed yet");
        }

        return new Wishes(wishes);
    }

    private CompletedValidation validateInput(String input) {
        String message;
        CompletedValidation.Result result;
        if (input == null) {
            message = "One of the given wishes is null";
            result = CompletedValidation.Result.BAD;
        } else if (input.isBlank()) {
            message = "One of the given wishes is blank";
            result = CompletedValidation.Result.BAD;
        } else {
            return new CompletedValidation(CompletedValidation.Result.GOOD);
        }

        return new CompletedValidation(message, result);
    }
}
