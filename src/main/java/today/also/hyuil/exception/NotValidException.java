package today.also.hyuil.exception;

public class NotValidException extends Exception {
    public NotValidException() {
    }

    public NotValidException(String message) {
        super(message);
    }

    public NotValidException(String message, Throwable cause) {
        super(message, cause);
    }
}
