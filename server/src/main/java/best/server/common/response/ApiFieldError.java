package best.server.common.response;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.validation.BindingResult;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * Api request field binding errors
 */
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ApiFieldError {

    private String field;
    private String value;
    private String reason;

    /**
     * Returns {@link ApiFieldError} singleton list given args
     */
    public static List<ApiFieldError> of(final String field, final String value, final String reason) {
        return Collections.singletonList(new ApiFieldError(field, value, reason));
    }

    /**
     * Returns {@link ApiFieldError} list given binding result
     */
    public static List<ApiFieldError> of(final BindingResult result) {
        if (result == null) {
            return Collections.emptyList();
        }

        return result.getFieldErrors()
                     .stream()
                     .map(error -> new ApiFieldError(
                             error.getField(),
                             error.getRejectedValue() == null ? "" : error.getRejectedValue().toString(),
                             error.getDefaultMessage())
                     )
                     .collect(Collectors.toList());
    }

    private ApiFieldError(String field, String value, String reason) {
        this.field = field;
        this.value = value;
        this.reason = reason;
    }
}
