package com.recipick.member.domain.model;

public enum MemberStatus {
    ACTIVE,     // 정상적으로 서비스를 이용할 수 있는 활성 상태
    SUSPENDED,  // 운영 정책 위반 등의 사유로 서비스 이용이 일시 제한된 상태
    WITHDRAWN   // 회원 탈퇴가 처리되어 서비스를 이용할 수 없는 상태
}
