package com.ahndonghwan.backend.common.response;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ResponseMessage {

    SUCCESS_TO_CREATE_PRODUCT("상품 생성 성공"),
    SUCCESS_TO_GET_PRODUCT_DETAIL("상품 상세정보 조회 성공"),
    ;

    private final String message;
}
