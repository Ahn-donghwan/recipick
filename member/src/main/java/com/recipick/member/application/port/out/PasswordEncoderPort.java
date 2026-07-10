package com.recipick.member.application.port.out;

public interface PasswordEncoderPort {

    // 원문 비밀번호를 안전한 단방향 해시값으로 변환한다.
    String encode(String rawPassword);
}
