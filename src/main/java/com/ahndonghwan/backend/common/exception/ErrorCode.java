package com.ahndonghwan.backend.common.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorCode {

    // Member : 1000 ~ 1999
    INVALID_GENDER(HttpStatus.BAD_REQUEST, false, 1001, "유효하지 않은 성별입니다."),
    MEMBER_CREATE_FAILED(HttpStatus.BAD_REQUEST, false, 1002, "멤버 생성에 실패하였습니다."),
    MEMBER_NOT_FOUND(HttpStatus.NOT_FOUND, false, 1003, "존재하지 않는 회원입니다."),
    INVALID_TOKEN(HttpStatus.UNAUTHORIZED, false, 1004, "유효하지 않은 토큰입니다.")
    ;

    private final HttpStatus httpStatus;
    private final boolean success;
    private final int code;
    private final String message;
}
