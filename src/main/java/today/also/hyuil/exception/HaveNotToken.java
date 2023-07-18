package today.also.hyuil.exception;

public class HaveNotToken extends Exception {
    public HaveNotToken() {
    }

    public HaveNotToken(String message) {
        super(message);
    }

    public HaveNotToken(String message, Throwable cause) {
        super(message, cause);
    }
}
