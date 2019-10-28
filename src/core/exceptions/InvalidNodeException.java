package core.exceptions;

public class InvalidNodeException extends RuntimeException {
    public InvalidNodeException() {
        super();
    }

    public InvalidNodeException(String message) {
        super(message);
    }

    public InvalidNodeException(String message, Throwable cause) {
        super(message, cause);
    }

    public InvalidNodeException(Throwable cause) {
        super(cause);
    }
}
