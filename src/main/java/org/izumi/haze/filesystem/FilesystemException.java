package org.izumi.haze.filesystem;

public class FilesystemException extends RuntimeException {
    public FilesystemException(String message) {
        super(message);
    }

    public FilesystemException(String message, Throwable cause) {
        super(message, cause);
    }
}
