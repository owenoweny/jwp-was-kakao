package exceptions;

public class HttpRequestFormatException extends RuntimeException {
    public HttpRequestFormatException(String message) {
        super(message);
    }
}
