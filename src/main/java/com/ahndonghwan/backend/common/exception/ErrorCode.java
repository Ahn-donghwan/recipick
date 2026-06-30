package com.ahndonghwan.backend.common.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorCode {

    // Member : 1000 ~ 1999
    INVALID_GENDER(HttpStatus.BAD_REQUEST, false, 1001, "유효하지 않은 성별입니다.");

    private final HttpStatus httpStatus;
    private final boolean success;
    private final int code;
    private final String message;
}
