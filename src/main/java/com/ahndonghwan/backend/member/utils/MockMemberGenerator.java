package com.ahndonghwan.backend.member.utils;

import com.ahndonghwan.backend.member.dto.request.MemberCreateReqDto;

import java.util.List;

public interface MockMemberGenerator {

    MemberCreateReqDto generateMockMember();

    List<MemberCreateReqDto> generateMockMemberList(int count);
}
