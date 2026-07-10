package com.recipick.member.application.port.in;

import com.recipick.member.domain.model.MemberStatus;

import java.time.LocalDateTime;
import java.util.UUID;

// 회원가입 완료 후 외부에 제공할 수 있는 회원 정보만 전달한다.
public record RegisterMemberResult(
        UUID uuid,
        String email,
        String nickname,
        MemberStatus status,
        LocalDateTime createdAt
) {
}
