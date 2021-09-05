package org.izumi.haze.filesystem;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

public abstract class File extends Element {
    private String filename;
    private Extension extension;
    private Charset charset;
    private String content;

    private boolean contentChanged = false;

    public File(Path path) {
        super(path);
        if (Files.exists(path) && !Files.isRegularFile(path)) {
            throw new FilesystemException("Given path is not a file. Given: " + path);
        }
    }

    protected void initiateFields() {
        String[] parts = path.toAbsolutePath().toString().split("/");
        String[] nameParts = parts[parts.length - 1].split("\\.");
        filename = nameParts[0];
        if (nameParts.length > 1) {
            extension = new ExtensionImpl(nameParts[nameParts.length - 1]);
        }
        charset = StandardCharsets.UTF_8; //TODO: try autodetect used charset
    }

    protected void loadContent() {
        if (contentChanged) {
            return;
        }

        try {
            content = Files.readString(path, charset);
        } catch (IOException ex) {
            throw new FilesystemException("An error occurred while reading the body of a file", ex);
        }
    }

    public void changeContent(String newContent) {
        content = newContent;
        contentChanged = true;
    }

    public boolean isSupported() {
        return SupportedExtension.isGivenOneOfSupported(extension);
    }

    public boolean isNotSupported() {
        return !isSupported();
    }

    public String getFilename() {
        return filename;
    }

    public String getContent() {
        return content;
    }

    public Extension getExtension() {
        return extension;
    }

    public String getFullName() {
        if (extension.isBlank()) {
            return filename;
        }

        return filename + "." + extension;
    }

    public void save() {
        try {
            ensureDirectoriesExist();
            Files.writeString(path, content, charset, StandardOpenOption.CREATE);
            contentChanged = false;
        } catch (IOException ex) {
            throw new FilesystemException("An error occurred while saving data into file", ex);
        }
    }

    private void ensureDirectoriesExist() throws IOException {
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
    }
}
