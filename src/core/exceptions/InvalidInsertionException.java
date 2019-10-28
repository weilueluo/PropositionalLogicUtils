package core.exceptions;

public class InvalidInsertionException extends InternalException {
    public InvalidInsertionException() {
        super();
    }

    public InvalidInsertionException(String message) {
        super(message);
    }

    public InvalidInsertionException(String message, Throwable cause) {
        super(message, cause);
    }

    public InvalidInsertionException(Throwable cause) {
        super(cause);
    }
}
