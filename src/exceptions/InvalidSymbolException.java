package exceptions;

public class InvalidSymbolException extends RuntimeException {

    public InvalidSymbolException() {
        super();
    }

    public InvalidSymbolException(String message) {
        super(message);
    }

    public InvalidSymbolException(String message, Throwable cause) {
        super(message, cause);
    }

    public InvalidSymbolException(Throwable cause) {
        super(cause);
    }
}
