package com.ahndonghwan.backend.batch.member.properties;

import jakarta.validation.constraints.Min;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

@Validated
@ConfigurationProperties(prefix = "batch.member")
public record MemberBatchProperties(

        @Min(value = 1, message = "chunk 의 크기는 1 이상이어야 합니다.")
        int chunkSize
) {

    public MemberBatchProperties {
        if (chunkSize < 1) {
            chunkSize = 100;
        }
    }
}
