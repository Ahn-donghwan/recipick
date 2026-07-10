package com.recipick.member.application.port.in;

import com.recipick.member.domain.model.Gender;

import java.time.LocalDate;

// HTTP 같은 입력 기술과 무관하게 회원가입에 필요한 값을 전달한다.
public record RegisterMemberCommand(
        String email,
        String rawPassword,
        String phone,
        String nickname,
        String name,
        LocalDate birthDate,
        Gender gender
) {
}
