package com.recipick.member.application.port.in;

// 외부 요청이 회원가입 기능을 실행할 때 사용하는 인바운드 포트다.
public interface RegisterMemberUseCase {

    // 회원가입 입력을 받아 신규 회원을 생성하고 외부에 제공할 결과를 반환한다.
    RegisterMemberResult register(RegisterMemberCommand command);
}
