package com.ahndonghwan.backend.common.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "jwt")
public record JwtProperties(

        String secretKey,
        long accessExpireTime
) {
}
