package best.server.common.response;

import java.util.Collections;
import java.util.List;

import org.springframework.validation.BindingResult;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * API common response
 */
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ApiErrorResponse {

    /**
     * http status
     */
    private int status;

    /**
     * error code
     */
    private String code;

    /**
     * error message
     */
    private String message;

    /**
     * error fields
     */
    private List<ApiFieldError> errors;

    /**
     * Returns a error response given {@link ApiErrorCode}
     */
    public static ApiErrorResponse createException(final ApiErrorCode code) {
        return new ApiErrorResponse(code, null);
    }

    /**
     * Returns a error response given {@link ApiErrorCode} and {@link BindingResult}
     */
    public static ApiErrorResponse createException(final ApiErrorCode code,
                                                   final BindingResult bindingResult) {
        return createException(code, ApiFieldError.of(bindingResult));
    }

    /**
     * Returns a error response given {@link ApiErrorCode} and {@link ApiFieldError} errors
     */
    public static ApiErrorResponse createException(final ApiErrorCode code,
                                                   final List<ApiFieldError> errors) {
        return new ApiErrorResponse(code, errors);
    }

    private ApiErrorResponse(ApiErrorCode code, List<ApiFieldError> errors) {
        this.status = code.getStatus();
        this.message = code.getMessage();
        this.code = code.getCode();
        this.errors = errors == null ? Collections.emptyList() : errors;
    }
}
