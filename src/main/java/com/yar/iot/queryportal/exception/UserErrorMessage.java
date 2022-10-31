package com.yar.iot.queryportal.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

/**
 * User errors
 * */
@Getter
@AllArgsConstructor
public enum UserErrorMessage implements ErrorMessage{

    NOT_AUTHORIZATION(HttpStatus.UNAUTHORIZED, "Unauthorized"),
    USER_NOT_FOUND(HttpStatus.BAD_REQUEST, "User does not exist."),
    THE_USER_ID_MUST_BE_DETERMINED(HttpStatus.BAD_REQUEST, "id must be determined."),
    OLD_PASSWORD_IS_INCORRECT(HttpStatus.FORBIDDEN, "old password is incorrect."),
    USERNAME_IS_DUPLICATED(HttpStatus.BAD_REQUEST, "Username is duplicated."),
    NO_AUTHORITY_TO_EDIT_USER(HttpStatus.FORBIDDEN, "You are not authorized to edit user.")
    ;


    private HttpStatus httpStatus;
    private String errorMessage;


    @Override
    public HttpStatus getHttpStatus() {
        return this.httpStatus;
    }

    @Override
    public String getMessage() {
        return this.errorMessage;
    }
}
