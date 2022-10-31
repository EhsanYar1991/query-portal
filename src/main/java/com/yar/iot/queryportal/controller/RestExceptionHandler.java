package com.yar.iot.queryportal.controller;


import com.yar.iot.queryportal.common.ErrorResponse;
import com.yar.iot.queryportal.exception.BusinessException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * The rest controller advice for managing the application exceptions
 */
@RestControllerAdvice
@Slf4j
public class RestExceptionHandler implements BaseController {

    /**
     * Manage {@link Exception}
     *
     * @param exception The exception
     * @return {@link ResponseEntity<ErrorResponse>}
     */
    @ExceptionHandler(value = Exception.class)
    public ResponseEntity<ErrorResponse> exceptionHandler(Exception exception) {
        log.error(exception.getLocalizedMessage(), exception);
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(makeExceptionResponse(exception));
    }

    /**
     * Manage {@link ServletRequestBindingException}
     *
     * @param exception The exception
     * @return {@link ResponseEntity<ErrorResponse>}
     */
    @ExceptionHandler(value = ServletRequestBindingException.class)
    public ResponseEntity<ErrorResponse> exceptionHandler(ServletRequestBindingException exception) {
        log.error(exception.getLocalizedMessage(), exception);
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(makeExceptionResponse(exception));
    }

    /**
     * Manage {@link BusinessException}
     *
     * @param businessException The exception
     * @return {@link ResponseEntity<ErrorResponse>}
     */
    @ExceptionHandler(value = BusinessException.class)
    public ResponseEntity<ErrorResponse> businessExceptionHandler(BusinessException businessException) {
        log.error(businessException.getErrorMessage() != null ?
                        businessException.getErrorMessage().getMessage() :
                        businessException.getMessage(),
                businessException);
        return responseBuilder(
                businessException.getErrorMessage() != null ?
                        businessException.getErrorMessage().getHttpStatus() :
                        HttpStatus.INTERNAL_SERVER_ERROR,
                makeExceptionResponse(businessException)
        );
    }

    private ErrorResponse makeExceptionResponse(Throwable throwable) {
        return ErrorResponse.builder()
                .errorMessage(throwable.getLocalizedMessage())
                .build();
    }

}
