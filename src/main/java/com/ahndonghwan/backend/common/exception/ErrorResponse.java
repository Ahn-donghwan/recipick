package com.ahndonghwan.backend.common.exception;

import org.springframework.http.HttpStatus;

public record ErrorResponse(HttpStatus httpStatus, boolean success, int code, String message) {

    public static ErrorResponse of(HttpStatus httpStatus, boolean success, int code, String message) {
        return new ErrorResponse(httpStatus, success, code, message);
    }
}
