package bg.sofia.uni.fmi.mjt.glovo.exception;

public class NullMapLayoutException extends IllegalArgumentException {
    public NullMapLayoutException(String message) {
        super(message);
    }

    public NullMapLayoutException(String message, Throwable cause) {
        super(message, cause);
    }
}