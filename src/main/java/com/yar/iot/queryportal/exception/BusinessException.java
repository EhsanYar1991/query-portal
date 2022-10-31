package com.yar.iot.queryportal.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;


/**
 * The business exception class for managing business exceptions
 * */

@Getter
public class BusinessException extends RuntimeException {

    private ErrorMessage errorMessage;

    public BusinessException(ErrorMessage errorMessage) {
        super(errorMessage.getMessage());
        this.errorMessage = errorMessage;
    }

    public BusinessException(String message) {
        super(message);
    }

    public BusinessException(Throwable cause) {
        super(cause.getLocalizedMessage(), cause);
    }

    public BusinessException(HttpStatus httpStatus, String message, Throwable cause) {
        super(message, cause);
    }

    public BusinessException(Throwable cause, HttpStatus httpStatus) {
        super(cause);
    }

    public BusinessException(String message, Throwable cause) {
        super(message, cause);
    }

}
