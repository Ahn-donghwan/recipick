package com.ahndonghwan.backend.common.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BaseException.class)
    public ResponseEntity<ErrorResponse> handleBaseException(BaseException e) {

        ErrorCode errorCode = e.getErrorCode();
        log.error("errorCode: {}, message: {}", errorCode, e.getMessage());

        ErrorResponse response = ErrorResponse.of(
                errorCode.getHttpStatus(),
                errorCode.isSuccess(),
                errorCode.getCode(),
                errorCode.getMessage());

        return new ResponseEntity<>(response, errorCode.getHttpStatus());
    }
}
