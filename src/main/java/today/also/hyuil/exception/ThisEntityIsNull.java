package today.also.hyuil.exception;

public class ThisEntityIsNull extends Exception {

    public ThisEntityIsNull() {
    }

    public ThisEntityIsNull(String message) {
        super(message);
    }

    public ThisEntityIsNull(String message, Throwable cause) {
        super(message, cause);
    }
}
