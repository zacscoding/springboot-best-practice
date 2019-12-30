package best.server.common.response;

import java.util.Collections;
import java.util.List;

import org.springframework.validation.BindingResult;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * API common response
 */
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CommonApiResponse<T> {

    /**
     * http status
     */
    private int status;

    /**
     * data if success to request
     */
    @JsonInclude(Include.NON_NULL)
    private T data;

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
     * Returns a success response {@link CommonApiCode#SUCCESS} with given data
     */
    public static <T> CommonApiResponse<T> createOK(final T data) {
        return new CommonApiResponse<>(CommonApiCode.SUCCESS, data);
    }

    /**
     * Returns a error response given {@link CommonApiCode}
     */
    public static <T> CommonApiResponse<T> createException(final CommonApiCode code) {
        return new CommonApiResponse<>(code, null);
    }

    /**
     * Returns a error response given {@link CommonApiCode} and {@link BindingResult}
     */
    public static <T> CommonApiResponse<T> createException(final CommonApiCode code,
                                                           final BindingResult bindingResult) {
        return createException(code, ApiFieldError.of(bindingResult));
    }

    /**
     * Returns a error response given {@link CommonApiCode} and {@link ApiFieldError} errors
     */
    public static <T> CommonApiResponse<T> createException(final CommonApiCode code,
                                                           final List<ApiFieldError> errors) {
        return new CommonApiResponse<>(code, null, errors);
    }

    private CommonApiResponse(CommonApiCode code, T data) {
        this(code, data, null);
    }

    private CommonApiResponse(CommonApiCode code, T data, List<ApiFieldError> errors) {
        this.status = code.getStatus();
        this.data = data;
        this.message = code.getMessage();
        this.code = code.getCode();
        this.errors = errors == null ? Collections.emptyList() : errors;
    }
}
