/*
 * Copyright (c) Worldline 2022 - All Rights Reserved.
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 *
 */

package com.yar.iot.queryportal.exception;

import org.springframework.http.HttpStatus;

/**
 * Defines how error codes on the platform are defined.
 */
public interface ErrorMessage {

    /**
    * Http Status
    * */
    HttpStatus getHttpStatus();

    /**
     * Error Message
     * */
    String getMessage();

}
