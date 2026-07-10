package com.ahndonghwan.backend.common.security;

import com.ahndonghwan.backend.common.exception.BaseException;
import com.ahndonghwan.backend.common.exception.ErrorCode;
import com.ahndonghwan.backend.common.properties.JwtProperties;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.security.Key;
import java.util.Date;
import java.util.function.Function;

@Service
@RequiredArgsConstructor
public class JwtProvider {

    private final JwtProperties jwtProperties;
    private static final String BEARER_PREFIX = "Bearer ";

    public String generateAccessToken(Authentication authentication) {

        Claims claims = Jwts.claims().subject(authentication.getName()).build();
        Date now = new Date();
        Date expiration = new Date(now.getTime() + jwtProperties.accessExpireTime());

        return BEARER_PREFIX + Jwts.builder()
                .signWith(getSignKey())
                .claims(claims)
                .issuedAt(now)
                .expiration(expiration)
                .compact();
    }

    private Key getSignKey() {
        return Keys.hmacShaKeyFor(jwtProperties.secretKey().getBytes());
    }

    public String validateAndGetMemberUuid(String token) throws AuthenticationException {
        try {
            return extractClaim(token, Claims::getSubject);
        } catch (NullPointerException e) {
            throw new BaseException(ErrorCode.INVALID_TOKEN);
        }
    }

    private <T> T extractClaim(String token, Function<Claims, T> claimsResolver) throws AuthenticationException {
        Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parser()
                .verifyWith((SecretKey) getSignKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
}
