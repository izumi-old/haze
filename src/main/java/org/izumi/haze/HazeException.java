package org.izumi.haze;

public class HazeException extends RuntimeException {
    public HazeException(String message) {
        super(message);
    }

    public HazeException(String message, Throwable cause) {
        super(message, cause);
    }
}
