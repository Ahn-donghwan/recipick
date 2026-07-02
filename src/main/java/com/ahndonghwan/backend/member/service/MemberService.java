package com.ahndonghwan.backend.member.service;

import com.ahndonghwan.backend.member.dto.request.CreateMemberReqDto;

public interface MemberService {

    void createMember(CreateMemberReqDto dto);
}
