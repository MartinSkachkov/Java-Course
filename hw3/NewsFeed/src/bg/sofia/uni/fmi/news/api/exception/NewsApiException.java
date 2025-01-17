package bg.sofia.uni.fmi.news.api.exception;

public class NewsApiException extends RuntimeException {
    private final int statusCode;

    public NewsApiException(int statusCode, String message) {
        super(message);
        this.statusCode = statusCode;
    }

    public NewsApiException(int statusCode, String message, Throwable cause) {
        super(message, cause);
        this.statusCode = statusCode;
    }

    public int getStatusCode() {
        return statusCode;
    }
}