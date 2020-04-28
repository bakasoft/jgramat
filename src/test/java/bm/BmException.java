package bm;

public class BmException extends RuntimeException {
    public BmException(String message) {
        super(message);
    }
    public BmException(String message, Exception cause) {
        super(message, cause);
    }
}
