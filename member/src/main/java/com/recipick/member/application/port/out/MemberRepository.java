package com.recipick.member.application.port.out;

import com.recipick.member.domain.model.Member;

public interface MemberRepository {

    boolean existsByEmail(String email);

    boolean existsByPhone(String phone);

    boolean existsByNickname(String nickname);

    Member save(Member member);
}
