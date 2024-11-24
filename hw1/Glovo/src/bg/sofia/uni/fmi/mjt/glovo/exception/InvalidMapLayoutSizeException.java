package bg.sofia.uni.fmi.mjt.glovo.exception;

public class InvalidMapLayoutSizeException extends IllegalArgumentException {
    public InvalidMapLayoutSizeException(String message) {
        super(message);
    }

    public InvalidMapLayoutSizeException(String message, Throwable cause) {
        super(message, cause);
    }
}