package today.also.hyuil.common.exception;

public class BadRequestException extends RuntimeException {
    private final int code;
    private final String message;

    public BadRequestException(Error error) {
        this.code = error.getCode();
        this.message = error.getMessage();
    }
}
