package bg.sofia.uni.fmi.mjt.glovo.exception;

public class MapLayoutIsMissingDeliveryGuysException extends RuntimeException {
    public MapLayoutIsMissingDeliveryGuysException(String message) {
        super(message);
    }

    public MapLayoutIsMissingDeliveryGuysException(String message, Throwable cause) {
        super(message, cause);
    }
}