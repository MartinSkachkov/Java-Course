package bg.sofia.uni.fmi.mjt.glovo.exception;

public class InvalidMapEntitySymbolTypeException extends IllegalArgumentException {
    public InvalidMapEntitySymbolTypeException(String message) {
        super(message);
    }

    public InvalidMapEntitySymbolTypeException(String message, Throwable cause) {
        super(message, cause);
    }
}