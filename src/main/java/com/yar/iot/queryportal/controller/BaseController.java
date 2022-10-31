package com.yar.iot.queryportal.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;

/**
 * The Base Controller interface for managing the common utilities of controllers
 * */
@CrossOrigin
@Validated
public interface BaseController {

     String ALL_AUTHORITY = "hasAnyAuthority('ADMIN','USER')";
     String ADMIN_AUTHORITY = "hasAuthority('ADMIN')";


    /**
     * Build an OK response by the given body
     * @param body The body
     * @return {@link ResponseEntity}
     * */
    default <T> ResponseEntity<T> okResponse(T body) {
        return responseBuilder(HttpStatus.OK, body);
    }

    /**
     * Build an empty OK response by the given body
     * @return {@link ResponseEntity}
     * */
    default ResponseEntity<Void> okResponse() {
        return ResponseEntity.ok().build();
    }

    /**
     * Build a custom response by the given status and body
     * @param status The http status
     * @param body The body
     * @return {@link ResponseEntity}
     * */
    default <T> ResponseEntity<T> responseBuilder(HttpStatus status, T body) {
        return ResponseEntity.status(status).body(body);
    }

}
