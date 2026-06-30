package com.ahndonghwan.backend.common.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorCode {

    ;

    private final HttpStatus httpStatus;
    private final boolean success;
    private final int code;
    private final String message;
}
