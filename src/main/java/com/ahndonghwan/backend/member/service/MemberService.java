package com.ahndonghwan.backend.member.service;

import com.ahndonghwan.backend.member.dto.request.MemberCreateReqDto;

public interface MemberService {

    void createMember(MemberCreateReqDto dto);
}
