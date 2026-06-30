package com.ahndonghwan.backend.common.response;

import lombok.Builder;
import org.springframework.http.HttpStatus;

@Builder
public record ApiResponse<T>(
        HttpStatus httpStatus, boolean success, int code, String message, T data
) {

//    public ApiResponse(String message, T data) {
//        this(HttpStatus.OK, true, 200, message, data);
//    }
//
//    public ApiResponse(String message) {
//        this(HttpStatus.OK, true, 200, message, null);
//    }

    public ApiResponse(ResponseMessage message, T data) {
        this(HttpStatus.OK, true, 200, message.toString(), data);
    }

    public ApiResponse(ResponseMessage message) {
        this(HttpStatus.OK, true, 200, message.toString(), null);
    }
}
