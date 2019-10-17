package exceptions;

public class InvalidFormulaException extends Exception {
    public InvalidFormulaException() {
        super();
    }

    public InvalidFormulaException(String message) {
        super(message);
    }

    public InvalidFormulaException(String message, Throwable cause) {
        super(message, cause);
    }

    public InvalidFormulaException(Throwable cause) {
        super(cause);
    }
}
