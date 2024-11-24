package bg.sofia.uni.fmi.mjt.glovo.exception;

public class OutOfMapBoundsException extends RuntimeException {
    public OutOfMapBoundsException(String message) {
        super(message);
    }

    public OutOfMapBoundsException(String message, Throwable cause) {
        super(message, cause);
    }
}