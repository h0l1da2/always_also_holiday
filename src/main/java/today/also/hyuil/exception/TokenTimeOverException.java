package today.also.hyuil.exception;

public class TokenTimeOverException extends Exception {
    public TokenTimeOverException() {
    }

    public TokenTimeOverException(String message) {
        super(message);
    }

    public TokenTimeOverException(String message, Throwable cause) {
        super(message, cause);
    }
}
