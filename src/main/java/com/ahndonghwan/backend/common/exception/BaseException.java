package com.ahndonghwan.backend.common.exception;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class BaseException extends RuntimeException {

    private ErrorCode errorCode;

    @Builder
    public BaseException(ErrorCode errorCode) {
        this.errorCode = errorCode;
    }
}
