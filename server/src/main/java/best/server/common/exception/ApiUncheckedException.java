package best.server.common.exception;

/**
 * API common unchecked exception
 */
public class ApiUncheckedException extends RuntimeException {

    public ApiUncheckedException() {
        super();
    }

    public ApiUncheckedException(String message) {
        super(message);
    }

    public ApiUncheckedException(String message, Throwable cause) {
        super(message, cause);
    }

    public ApiUncheckedException(Throwable cause) {
        super(cause);
    }

    protected ApiUncheckedException(String message, Throwable cause, boolean enableSuppression,
                                    boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
