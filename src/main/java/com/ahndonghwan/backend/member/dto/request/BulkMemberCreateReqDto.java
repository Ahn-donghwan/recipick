package com.ahndonghwan.backend.member.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;

public class BulkMemberCreateReqDto {

    @Schema(description = "생성할 회원 수", example = "100000")
    @Min(1)
    @Max(100000)
    private Integer count;

}
