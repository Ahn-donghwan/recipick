package com.ahndonghwan.backend.member.dto.request;

import com.ahndonghwan.backend.member.entity.Member;

import java.time.LocalDate;

public record CreateMemberReqDto(
        String email,
        String password,
        String phone,
        String nickname,
        String name,
        LocalDate birth,
        String gender
) {

    public Member toEntity() {

        return Member.builder()
                .email(email)
                .password(password)
                .phone(phone)
                .nickname(nickname)
                .name(name)
                .birth(birth)
                .gender(gender)
                .build();
    }
}
