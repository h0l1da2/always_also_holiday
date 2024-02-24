package today.also.hyuil.common.exception;

public class FileNumbersLimitExceededException extends Exception {
    public FileNumbersLimitExceededException() {
        super();
    }

    public FileNumbersLimitExceededException(String message) {
        super(message);
    }

    public FileNumbersLimitExceededException(String message, Throwable cause) {
        super(message, cause);
    }
}
