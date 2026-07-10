package com.recipick.member.domain.exception;

import com.recipick.member.domain.model.MemberStatus;

public class InvalidMemberStateTransitionException extends IllegalStateException {

    public InvalidMemberStateTransitionException(MemberStatus currentStatus, MemberStatus targetStatus) {
        super("회원 상태를 " + currentStatus + "에서 " + targetStatus + "(으)로 변경할 수 없습니다.");
    }
}
