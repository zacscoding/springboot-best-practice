package best.server.common.exception.handler;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageConversionException;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import com.fasterxml.jackson.databind.ObjectMapper;

import best.server.common.response.ApiFieldError;
import best.server.common.response.CommonApiCode;
import best.server.common.response.CommonApiResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Global api exception handler
 */
@ControllerAdvice
@Slf4j
@RequiredArgsConstructor
public class ApiExceptionHandler implements AuthenticationEntryPoint, AccessDeniedHandler {

    private final ObjectMapper objectMapper;

    /**
     * handle {@link MethodArgumentNotValidException}
     *
     * - {@link Valid}, {@link Validated} binding error occur
     * - {@link HttpMessageConverter}'s binding error occur
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    protected ResponseEntity<CommonApiResponse<Void>> handleMethodArgumentNotValidException(
            MethodArgumentNotValidException e) {
        logger.warn("handleMethodArgumentNotValidException : {}", e.getMessage());

        return createBadRequestResponse(e.getBindingResult());
    }

    /**
     * handle {@link BindException}
     *
     * - {@link ModelAttribute}'s binding error
     */
    @ExceptionHandler(BindException.class)
    protected ResponseEntity<CommonApiResponse<Void>> handleBindException(BindException e) {
        logger.warn("handleBindException : {}", e.getMessage());

        return createBadRequestResponse(e.getBindingResult());
    }

    /**
     * handle {@link MethodArgumentTypeMismatchException}
     *
     * - if enum's type is mismatched
     */
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    protected ResponseEntity<CommonApiResponse<Void>> handleMethodArgumentTypeMismatchException(
            MethodArgumentTypeMismatchException e) {

        logger.warn("handleMethodArgumentTypeMismatchException : {}", e.getMessage());

        final String value = e.getValue() == null ? "" : e.getValue().toString();
        final CommonApiResponse<Void> response =
                CommonApiResponse.createException(CommonApiCode.BAD_REQUEST,
                                                  ApiFieldError.of(e.getName(), value,
                                                                   e.getErrorCode()));

        return ResponseEntity.badRequest().body(response);
    }

    /**
     * handle {@link HttpMessageConversionException}
     */
    @ExceptionHandler(HttpMessageConversionException.class)
    protected ResponseEntity<CommonApiResponse<Void>> handleHttpMessageConversionException(
            HttpMessageConversionException e) {

        logger.warn("handleHttpMessageConversionException : {}", e.getMessage());

        return createBadRequestResponse(null);
    }

    /**
     * handle {@link HttpRequestMethodNotSupportedException}
     *
     * - not supported http method
     */
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    protected ResponseEntity<CommonApiResponse<Void>> handleHttpRequestMethodNotSupportedException(
            HttpRequestMethodNotSupportedException e) {

        logger.warn("handleHttpRequestMethodNotSupportedException : {}", e.getMessage());

        return new ResponseEntity<>(CommonApiResponse.createException(CommonApiCode.METHOD_NOT_ALLOWED),
                                    HttpStatus.METHOD_NOT_ALLOWED);
    }

    /**
     * handle {@link AccessDeniedException} i.e 403 error
     */
    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response,
                       AccessDeniedException accessDeniedException) throws IOException, ServletException {

        logger.warn("handleAccessDeniedException : {}", accessDeniedException.getMessage());

        final String jsonResponse = objectMapper.writeValueAsString(
                CommonApiResponse.createException(CommonApiCode.FORBIDDEN));

        response.setStatus(403);
        response.setContentType("application/json;charset=UTF-8");
        response.getWriter().write(jsonResponse);
    }

    /**
     * handle 401 error
     */
    @Override
    public void commence(HttpServletRequest httpServletRequest,
                         HttpServletResponse httpServletResponse,
                         AuthenticationException e) throws IOException, ServletException {

        final String jsonResponse = objectMapper.writeValueAsString(
                CommonApiResponse.createException(CommonApiCode.UNAUTHORIZED));

        httpServletResponse.setStatus(401);
        httpServletResponse.setContentType("application/json;charset=UTF-8");
        httpServletResponse.getWriter().write(jsonResponse);
    }

    // ============ business exception handle

    /**
     * handle {@link Exception}
     */
    @ExceptionHandler(Exception.class)
    protected ResponseEntity<CommonApiResponse<Void>> handleException(Exception e) {
        logger.warn("handleException", e);

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                             .body(CommonApiResponse.createException(CommonApiCode.INTERNAL_SERVER_ERROR));
    }

    // ============ private

    private ResponseEntity<CommonApiResponse<Void>> createBadRequestResponse(final BindingResult result) {
        return ResponseEntity.badRequest()
                             .body(CommonApiResponse.createException(CommonApiCode.BAD_REQUEST, result));
    }
}
